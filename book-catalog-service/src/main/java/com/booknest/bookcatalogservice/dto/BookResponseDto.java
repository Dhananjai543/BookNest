package com.booknest.bookcatalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    private String id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String description;
}
