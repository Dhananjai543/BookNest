package com.booknest.loanservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanItem {

    private String isbn;
    private Integer quantity;
}
