package com.booknest.loanservice.controller;

import com.booknest.loanservice.dto.LoanRequestDto;
import com.booknest.loanservice.dto.LoanResponseDto;
import com.booknest.loanservice.service.LoanService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
@Slf4j
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @CircuitBreaker(name = "availability", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "availability")
    @Retry(name = "availability")
    public CompletableFuture<ResponseEntity<String>> placeLoan(@RequestBody LoanRequestDto request) {
        return CompletableFuture.supplyAsync(() -> {
            boolean placed = loanService.placeLoan(request);
            return placed
                    ? ResponseEntity.status(HttpStatus.CREATED).body("Loan placed successfully")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not available!");
        });
    }

    public CompletableFuture<ResponseEntity<String>> fallbackMethod(LoanRequestDto request, Throwable t) {
        log.error("Fallback triggered for loan request (member {}): {}", request.getMemberId(), t.toString(), t);
        return CompletableFuture.supplyAsync(() ->
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Oops! Availability service is unavailable, please try again later."));
    }

    @GetMapping("/getLoans")
    @ResponseStatus(HttpStatus.OK)
    public List<LoanResponseDto> getAllLoans() {
        return loanService.getAllLoans();
    }
}
