package com.booknest.availabilityservice.service;

import com.booknest.availabilityservice.dto.AvailabilityRequestDto;
import com.booknest.availabilityservice.dto.AvailabilityResponseDto;
import com.booknest.availabilityservice.model.Availability;
import com.booknest.availabilityservice.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    @Transactional(readOnly = true)
    public List<AvailabilityResponseDto> isAvailable(List<AvailabilityRequestDto> requestList) {
        List<String> isbns = requestList.stream()
                .map(AvailabilityRequestDto::getIsbn)
                .toList();

        List<Availability> availabilities = availabilityRepository.findByIsbnIn(isbns);

        return requestList.stream()
                .map(request -> availabilities.stream()
                        .filter(a -> a.getIsbn().equals(request.getIsbn()))
                        .findFirst()
                        .map(a -> AvailabilityResponseDto.builder()
                                .isbn(request.getIsbn())
                                .available(a.getCopiesAvailable() >= request.getQuantity())
                                .build())
                        .orElse(AvailabilityResponseDto.builder()
                                .isbn(request.getIsbn())
                                .available(false)
                                .build()))
                .toList();
    }
}
