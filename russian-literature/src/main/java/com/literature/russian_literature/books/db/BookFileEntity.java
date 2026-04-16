package com.literature.russian_literature.books.db;

import com.literature.russian_literature.books.domain.BookFormat;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_files")
@Data
public class BookFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @Column(nullable = false)
    private String fileUrl; // ссылка на Cloudinary

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookFormat format; // "PDF", "EPUB", "FB2", "TXT"

    @Column(name = "public_id")
    private String publicId;

    private LocalDateTime createdAt;
}
