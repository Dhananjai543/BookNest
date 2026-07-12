package com.booknest.availabilityservice.controller;

import com.booknest.availabilityservice.dto.AvailabilityRequestDto;
import com.booknest.availabilityservice.dto.AvailabilityResponseDto;
import com.booknest.availabilityservice.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AvailabilityResponseDto> isAvailable(@RequestBody List<AvailabilityRequestDto> requests) {
        return availabilityService.isAvailable(requests);
    }
}
