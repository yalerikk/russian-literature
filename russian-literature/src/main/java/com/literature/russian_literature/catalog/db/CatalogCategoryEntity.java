package com.literature.russian_literature.catalog.db;

import com.literature.russian_literature.tags.db.TagEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "catalog_categories")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String code; // Уникальный код категории

    @Column(nullable = false)
    private Integer displayOrder;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Integer booksToShow = 7;

    @Column(name = "criteria_type", nullable = false)
    private String criteriaType;

    @Column(name = "min_publication_year")
    private Integer minPublicationYear;

    @Column(name = "max_publication_year")
    private Integer maxPublicationYear;

    @Column(name = "min_rating")
    private Double minRating;

    @Column(name = "days_interval")
    private Integer daysInterval;

    @ManyToMany
    @JoinTable(
            name = "category_tags",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
