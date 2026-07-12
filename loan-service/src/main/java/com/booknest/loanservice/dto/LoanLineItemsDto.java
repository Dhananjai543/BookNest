package com.booknest.loanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanLineItemsDto {

    private Long id;
    private String isbn;
    private Integer quantity;
}
