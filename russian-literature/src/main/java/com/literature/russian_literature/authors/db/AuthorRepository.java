package com.literature.russian_literature.authors.db;

import com.literature.russian_literature.authors.domain.dto.AuthorForSelect;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AuthorEntity a " +
            "WHERE LOWER(REPLACE(a.firstName, 'ё', 'е')) = LOWER(REPLACE(:firstName, 'ё', 'е')) " +
            "AND LOWER(REPLACE(a.lastName, 'ё', 'е')) = LOWER(REPLACE(:lastName, 'ё', 'е')) " +
            "AND LOWER(REPLACE(a.middleName, 'ё', 'е')) = LOWER(REPLACE(:middleName, 'ё', 'е'))")
    boolean existsByFullName(@Param("firstName") String firstName,
                             @Param("lastName") String lastName,
                             @Param("middleName") String middleName);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AuthorEntity a " +
            "WHERE LOWER(REPLACE(a.firstName, 'ё', 'е')) = LOWER(REPLACE(:firstName, 'ё', 'е')) " +
            "AND LOWER(REPLACE(a.lastName, 'ё', 'е')) = LOWER(REPLACE(:lastName, 'ё', 'е')) " +
            "AND LOWER(REPLACE(a.middleName, 'ё', 'е')) = LOWER(REPLACE(:middleName, 'ё', 'е')) " +
            "AND a.id <> :id")
    boolean existsByFullNameExcludingId(@Param("firstName") String firstName,
                                        @Param("lastName") String lastName,
                                        @Param("middleName") String middleName,
                                        @Param("id") Long id);

    @Query("SELECT a FROM AuthorEntity a WHERE " +
            "LOWER(REPLACE(a.firstName, 'ё', 'е')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'ё', 'е')) OR " +
            "LOWER(REPLACE(a.lastName, 'ё', 'е')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'ё', 'е')) OR " +
            "LOWER(REPLACE(a.middleName, 'ё', 'е')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'ё', 'е'))")
    List<AuthorEntity> findByNormalizedNameContaining(@Param("query") String query);

    @Query("SELECT a FROM AuthorEntity a WHERE " +
            "LOWER(REPLACE(a.firstName, 'ё', 'е')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'ё', 'е')) OR " +
            "LOWER(REPLACE(a.lastName, 'ё', 'е')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'ё', 'е')) OR " +
            "LOWER(REPLACE(a.middleName, 'ё', 'е')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), 'ё', 'е'))")
    Page<AuthorEntity> searchByNormalizedName(@Param("query") String query, Pageable pageable);

    @Query("SELECT new com.literature.russian_literature.authors.domain.dto.AuthorForSelect" +
            "(a.id, a.lastName, a.firstName, a.middleName) FROM AuthorEntity a ORDER BY a.lastName, a.firstName")
    List<AuthorForSelect> findAllForSelect();
}
