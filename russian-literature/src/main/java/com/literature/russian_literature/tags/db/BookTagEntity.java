package com.literature.russian_literature.tags.db;

import com.literature.russian_literature.tags.domain.TagType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "book_tags")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagType type;
}