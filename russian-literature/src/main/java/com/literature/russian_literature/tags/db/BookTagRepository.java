package com.literature.russian_literature.tags.db;

import com.literature.russian_literature.tags.domain.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookTagRepository extends JpaRepository<BookTagEntity, Long> {
    Optional<BookTagEntity> findByName(String name);
    boolean existsByName(String name);
    List<BookTagEntity> findByType(TagType type);

    // Новый метод для получения тегов по нескольким типам
    List<BookTagEntity> findByTypeIn(List<TagType> types);

    // Метод для проверки существования тегов по ID
    @Query("SELECT COUNT(t) FROM BookTagEntity t WHERE t.id IN :tagIds")
    long countExistingTagsByIds(List<Long> tagIds);
}