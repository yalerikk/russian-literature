package com.literature.russian_literature.catalog.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;

@Configuration
public class CatalogCategorySeeder {
    private static final Logger log = LoggerFactory.getLogger(CatalogCategorySeeder.class);

    @Bean
    CommandLineRunner initCatalogCategories(CatalogCategoryRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                // Новинки
                CatalogCategoryEntity newBooks = new CatalogCategoryEntity();
                newBooks.setName("Новинки");
                newBooks.setCode("new");
                newBooks.setDisplayOrder(0);
                newBooks.setIsActive(true);
                newBooks.setBooksToShow(7);
                newBooks.setCriteriaType("NEW");
                newBooks.setDaysInterval(30); // Книги за последние 30 дней
                newBooks.setCreatedAt(LocalDateTime.now());
                newBooks.setUpdatedAt(LocalDateTime.now());
                repository.save(newBooks);

                // Популярное
                CatalogCategoryEntity popular = new CatalogCategoryEntity();
                popular.setName("Популярное");
                popular.setCode("popular");
                popular.setDisplayOrder(1);
                popular.setIsActive(true);
                popular.setBooksToShow(7);
                popular.setCriteriaType("POPULAR");
                popular.setMinRating(4.0);
                popular.setCreatedAt(LocalDateTime.now());
                popular.setUpdatedAt(LocalDateTime.now());
                repository.save(popular);

                // Русская литература 19 века
                CatalogCategoryEntity russian19th = new CatalogCategoryEntity();
                russian19th.setName("Русская литература XIX века");
                russian19th.setCode("russian_19th_century");
                russian19th.setDisplayOrder(2);
                russian19th.setIsActive(true);
                russian19th.setBooksToShow(7);
                russian19th.setCriteriaType("BY_PERIOD");
                russian19th.setMinPublicationYear(1800);
                russian19th.setMaxPublicationYear(1899);
                russian19th.setCreatedAt(LocalDateTime.now());
                russian19th.setUpdatedAt(LocalDateTime.now());
                repository.save(russian19th);

                log.info("Добавлены начальные категории каталога");
            }
        };
    }
}
