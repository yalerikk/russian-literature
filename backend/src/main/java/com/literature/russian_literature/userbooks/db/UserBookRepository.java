package com.literature.russian_literature.userbooks.db;

import com.literature.russian_literature.userbooks.domain.BookStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBookEntity, Long> {
    Page<UserBookEntity> findByUserIdAndIsFavoriteTrue(Long userId, Pageable pageable);

    Page<UserBookEntity> findByUserIdAndStatus(Long userId, BookStatus status, Pageable pageable);

    Optional<UserBookEntity> findByUserIdAndBookId(Long userId, Long bookId);

    Optional<UserBookEntity> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, BookStatus status);

    @Modifying
    @Query("DELETE FROM UserBookEntity u WHERE u.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM UserBookEntity u WHERE u.book.id = :bookId")
    void deleteByBookId(@Param("bookId") Long bookId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserBookEntity u WHERE u.user.id = :userId AND u.book.id = :bookId AND u.isFavorite = true")
    boolean existsFavoriteByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT u FROM UserBookEntity u WHERE u.user.id = :userId AND u.book.id = :bookId AND u.status IS NOT NULL")
    Optional<UserBookEntity> findStatusByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT u.book.id FROM UserBookEntity u WHERE u.user.id = :userId AND u.status = :status")
    List<Long> findBookIdsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookStatus status);

    @Query("SELECT u.book.id FROM UserBookEntity u WHERE u.user.id = :userId AND u.isFavorite = true")
    List<Long> findBookIdsByUserIdAndFavorite(@Param("userId") Long userId);
}
