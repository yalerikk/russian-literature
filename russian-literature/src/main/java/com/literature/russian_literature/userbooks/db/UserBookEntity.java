package com.literature.russian_literature.userbooks.db;

import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.users.db.UserEntity;
import com.literature.russian_literature.userbooks.domain.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_books", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "book_id", "status"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    private boolean isFavorite = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status; // null, WISHLIST, READING, READ

    private Integer progress; // 0-100

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
