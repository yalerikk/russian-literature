package com.literature.russian_literature.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogCategoryRepository extends JpaRepository<CatalogCategoryEntity, Long> {
    Optional<CatalogCategoryEntity> findByCode(String code);

    List<CatalogCategoryEntity> findByIsActiveTrueOrderByDisplayOrderAsc();

    boolean existsByCode(String code);
    boolean existsByName(String name);
    boolean existsByCodeAndIdNot(String code, Long id);
    boolean existsByNameAndIdNot(String name, Long id);
    List<CatalogCategoryEntity> findByIsActive(Boolean isActive);

    @Query("SELECT MAX(c.displayOrder) FROM CatalogCategoryEntity c")
    Integer findMaxDisplayOrder();

    @Query("SELECT c FROM CatalogCategoryEntity c WHERE c.isActive = true AND c.criteriaType = :type ORDER BY c.displayOrder")
    List<CatalogCategoryEntity> findActiveByCriteriaType(@Param("type") String type);
}
