package com.literature.russian_literature.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogCategoryRepository extends JpaRepository<CatalogCategoryEntity, Long> {
    Optional<CatalogCategoryEntity> findByCode(String code);

    List<CatalogCategoryEntity> findByIsActiveTrueOrderByDisplayOrderAsc();

    boolean existsByCode(String code);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<CatalogCategoryEntity> findByIsActive(Boolean isActive);

    @Query("SELECT MAX(c.displayOrder) FROM CatalogCategoryEntity c")
    Integer findMaxDisplayOrder();
}
