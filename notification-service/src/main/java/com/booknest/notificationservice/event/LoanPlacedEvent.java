package com.booknest.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanPlacedEvent {

    private String loanNumber;
    private String memberId;
    private List<LoanItem> items;
}
