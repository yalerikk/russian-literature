package com.literature.russian_literature.books.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
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
    Page<BookEntity> findByGenres_Id(Long genreId, Pageable pageable);
    Page<BookEntity> findByTags_Id(Long tagId, Pageable pageable);
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

    // ----- Поиск -----
    @Query("SELECT b FROM BookEntity b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<BookEntity> searchByTitle(@Param("query") String query, Pageable pageable);

    @Query("SELECT b FROM BookEntity b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<BookEntity> findTopByTitleContaining(@Param("query") String query, Pageable pageable); // для автокомплита

    // ----- Остальные методы -----
    // Список книг по одному тегу (без пагинации)
    List<BookEntity> findByTags_Id(Long tagId);
    // Список книг, имеющих хотя бы один из переданных тегов
    List<BookEntity> findByTags_IdIn(Set<Long> tagIds);
    // Книги автора, имеющие хотя бы один из тегов
    List<BookEntity> findByAuthorIdAndTags_IdIn(Long authorId, Set<Long> tagIds);
    List<BookEntity> findByTitleContainingIgnoreCase(String title);
    List<BookEntity> findByPublicationYear(Integer year);

    @Query("SELECT COUNT(b) > 0 FROM BookEntity b WHERE b.title = :title AND b.author.id = :authorId")
    boolean existsByTitleAndAuthorId(@Param("title") String title, @Param("authorId") Long authorId);

    @Query("SELECT COUNT(b) > 0 FROM BookEntity b WHERE b.title = :title AND b.author.id = :authorId AND b.id != :excludeId")
    boolean existsByTitleAndAuthorIdExcludingId(@Param("title") String title,
                                                @Param("authorId") Long authorId,
                                                @Param("excludeId") Long excludeId);

    boolean existsByAuthorId(Long authorId);
    boolean existsByGenres_Id(Long genreId);
    boolean existsByTags_Id(Long tagId);

    @Modifying
    @Query(value = "DELETE FROM book_genres WHERE genre_id = :genreId", nativeQuery = true)
    void deleteAllGenreLinks(@Param("genreId") Long genreId);

    @Modifying
    @Query(value = "DELETE FROM book_tags WHERE tag_id = :tagId", nativeQuery = true)
    void deleteAllTagLinks(@Param("tagId") Long tagId);
}
