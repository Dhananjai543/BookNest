package com.booknest.loanservice.service;

import com.booknest.loanservice.dto.AvailabilityResponseDto;
import com.booknest.loanservice.dto.LoanLineItemsDto;
import com.booknest.loanservice.dto.LoanRequestDto;
import com.booknest.loanservice.dto.LoanResponseDto;
import com.booknest.loanservice.event.LoanItem;
import com.booknest.loanservice.event.LoanPlacedEvent;
import com.booknest.loanservice.model.Loan;
import com.booknest.loanservice.model.LoanLineItems;
import com.booknest.loanservice.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LoanService {

    private final LoanRepository loanRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, LoanPlacedEvent> kafkaTemplate;

    public boolean placeLoan(LoanRequestDto request) {
        Loan loan = new Loan();
        loan.setLoanNumber(UUID.randomUUID().toString());
        loan.setMemberId(request.getMemberId());
        loan.setLoanLineItemsList(
                request.getLoanLineItemsList().stream()
                        .map(this::mapToEntity)
                        .toList());

        AvailabilityResponseDto[] responses = webClientBuilder.build().post()
                .uri("http://availability-service/api/availability")
                .bodyValue(request.getLoanLineItemsList())
                .retrieve()
                .bodyToMono(AvailabilityResponseDto[].class)
                .block();

        boolean allAvailable = responses != null && responses.length > 0
                && Arrays.stream(responses).allMatch(AvailabilityResponseDto::isAvailable);

        if (allAvailable) {
            loanRepository.save(loan);
            log.info("Loan {} placed for member {}", loan.getLoanNumber(), loan.getMemberId());
            List<LoanItem> eventItems = loan.getLoanLineItemsList().stream()
                    .map(li -> new LoanItem(li.getIsbn(), li.getQuantity()))
                    .toList();
            kafkaTemplate.send("notificationTopic",
                    new LoanPlacedEvent(loan.getLoanNumber(), loan.getMemberId(), eventItems));
            log.info("LoanPlacedEvent published for loan {}", loan.getLoanNumber());
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<LoanResponseDto> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private LoanLineItems mapToEntity(LoanLineItemsDto dto) {
        LoanLineItems item = new LoanLineItems();
        item.setIsbn(dto.getIsbn());
        item.setQuantity(dto.getQuantity());
        return item;
    }

    private LoanResponseDto mapToResponse(Loan loan) {
        return LoanResponseDto.builder()
                .loanId(loan.getLoanId())
                .loanNumber(loan.getLoanNumber())
                .memberId(loan.getMemberId())
                .loanLineItemsList(
                        loan.getLoanLineItemsList().stream()
                                .map(this::mapToLineItemDto)
                                .toList())
                .build();
    }

    private LoanLineItemsDto mapToLineItemDto(LoanLineItems item) {
        return new LoanLineItemsDto(item.getId(), item.getIsbn(), item.getQuantity());
    }
}
