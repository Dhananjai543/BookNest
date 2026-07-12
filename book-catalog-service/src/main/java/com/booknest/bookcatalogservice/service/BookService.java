package com.booknest.bookcatalogservice.service;

import com.booknest.bookcatalogservice.dto.BookRequestDto;
import com.booknest.bookcatalogservice.dto.BookResponseDto;
import com.booknest.bookcatalogservice.model.Book;
import com.booknest.bookcatalogservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public void createBook(BookRequestDto bookRequestDto) {
        Book book = Book.builder()
                .title(bookRequestDto.getTitle())
                .author(bookRequestDto.getAuthor())
                .isbn(bookRequestDto.getIsbn())
                .genre(bookRequestDto.getGenre())
                .description(bookRequestDto.getDescription())
                .build();

        bookRepository.save(book);
        log.info("Book {} has been added to the catalog", book.getId());
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private BookResponseDto mapToResponse(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .description(book.getDescription())
                .build();
    }
}
