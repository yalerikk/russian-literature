package com.literature.russian_literature.tags.db;

import com.literature.russian_literature.tags.domain.TagType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByName(String name);

    boolean existsByName(String name);

    List<TagEntity> findByType(TagType type);

    @Query("SELECT COUNT(t) FROM TagEntity t WHERE t.id IN :tagIds")
    long countExistingTagsByIds(@Param("tagIds") Set<Long> tagIds);
}
