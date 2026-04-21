package com.literature.russian_literature.userbooks.db;

import com.literature.russian_literature.userbooks.domain.UserBook;
import org.springframework.stereotype.Component;

@Component
public class UserBookMapper {
    public UserBook toDomain(UserBookEntity entity) {
        return new UserBook(
                entity.getId(),
                entity.getBook().getId(),
                entity.getBook().getTitle(),
                entity.getBook().getCoverUrl(),
                entity.getBook().getAuthor().getShortName(),
                entity.getStatus(),
                entity.getProgress(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
