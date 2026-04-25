package com.literature.russian_literature.search.domain;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.db.AuthorRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public SearchService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // Автокомплит – возвращает до 10 записей (книги + авторы)
    public List<SearchSuggestion> suggest(String query) {
        List<SearchSuggestion> results = new ArrayList<>();
        Pageable limit = PageRequest.of(0, 5); // по 5 книг и авторов

        List<BookEntity> books = bookRepository.findTopByTitleContaining(query, limit);
        for (BookEntity book : books) {
            String authorName = book.getAuthor() != null ? book.getAuthor().getShortName() : "";
            results.add(new SearchSuggestion(
                    "BOOK",
                    book.getId(),
                    book.getTitle(),
                    authorName
            ));
        }

        List<AuthorEntity> authors = authorRepository.findByNormalizedNameContaining(query);
        for (AuthorEntity author : authors) {
            String lifespan = "";
            if (author.getBirthDate() != null) {
                lifespan = author.getBirthDate().getYear() +
                        (author.getDeathDate() != null ? " – " + author.getDeathDate().getYear() : "");
            }
            results.add(new SearchSuggestion(
                    "AUTHOR",
                    author.getId(),
                    author.getFullName(),
                    lifespan
            ));
        }

        return results;
    }

    public Page<AuthorEntity> searchAuthors(String query, Pageable pageable) {
        return authorRepository.searchByNormalizedName(query, pageable);
    }
}
