package com.literature.russian_literature.ratings.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRatingEntity, Long> {
    Optional<BookRatingEntity> findByBookIdAndUserId(Long bookId, Long userId);
    List<BookRatingEntity> findByUserId(Long userId);

    @Query("SELECT AVG(r.rating) FROM BookRatingEntity r WHERE r.book.id = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(r) FROM BookRatingEntity r WHERE r.book.id = :bookId")
    Integer countByBookId(@Param("bookId") Long bookId);

    boolean existsByBookIdAndUserId(Long bookId, Long userId);
    void deleteByBookIdAndUserId(Long bookId, Long userId);
}
