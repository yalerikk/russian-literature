package com.literature.russian_literature.books.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity> {
    // ----- Пагинируемые запросы (Page) -----
    Page<BookEntity> findByAuthorId(Long authorId, Pageable pageable);
    Page<BookEntity> findByGenreId(Long genreId, Pageable pageable);
    Page<BookEntity> findByTagId(Long tagId, Pageable pageable);
    Page<BookEntity> findByPublicationYearBetween(Integer minYear, Integer maxYear, Pageable pageable);
    Page<BookEntity> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime startDate, Pageable pageable);

    @Query("SELECT b FROM BookEntity b " +
            "LEFT JOIN b.ratings r " +
            "GROUP BY b.id " +
            "ORDER BY COALESCE(AVG(r.rating), 0) DESC, COUNT(r.id) DESC")
    Page<BookEntity> findTopBooksByRating(Pageable pageable);

    @Query(value = "SELECT * FROM books ORDER BY RANDOM()",
            countQuery = "SELECT COUNT(*) FROM books",
            nativeQuery = true)
    Page<BookEntity> findRandomBooksPage(Pageable pageable);

    // ----- Непагинируемые запросы (List) для слайдеров -----
    @Query(value = "SELECT * FROM books WHERE created_at >= :startDate ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true)
    List<BookEntity> findRecentBooks(@Param("startDate") LocalDateTime startDate, @Param("limit") int limit);

    @Query(value = "SELECT * FROM books ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<BookEntity> findRandomBooks(@Param("limit") int limit);

    @Query(value = "SELECT b.* FROM books b " +
            "LEFT JOIN book_ratings r ON b.id = r.book_id " +
            "GROUP BY b.id " +
            "ORDER BY COALESCE(AVG(r.rating), 0) DESC, COUNT(r.id) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<BookEntity> findTopBooksByRating(@Param("limit") int limit);

    // ----- Остальные методы -----
    List<BookEntity> findByTagId(Long tagId);
    List<BookEntity> findByTagIds(Set<Long> tagIds);
    List<BookEntity> findByTitleContainingIgnoreCase(String title);
    List<BookEntity> findByPublicationYear(Integer year);
    List<BookEntity> findByAuthorIdAndTagIds(Long authorId, Set<Long> tagIds);

    boolean existsByTitleAndAuthorId(String title, Long authorId);
    boolean existsByTitleAndAuthorIdExcludingId(String title, Long authorId, Long excludeId);
    boolean existsByGenreId(Long genreId);
    boolean existsByTagId(Long tagId);
}
