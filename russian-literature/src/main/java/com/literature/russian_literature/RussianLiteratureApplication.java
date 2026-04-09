package com.literature.russian_literature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RussianLiteratureApplication {

	public static void main(String[] args) {
		SpringApplication.run(RussianLiteratureApplication.class, args);
	}

}

// TODO: Фичи для будущей реализации
// 1. Пагинация для списка авторов, пользователей, произведений
// 2. Умный поиск с транслитерацией и учетом опечаток
// 3. Расширенная фильтрация произведений
// 4. Статистика по авторам и произведениям
// 5. Экспорт данных
// 6. Кэширование данных
// БЕЗ кэширования (медленно):
// Каждый запрос → База данных → Поиск → Возврат

// С кэшированием (быстро):
// Первый запрос → База данных → Сохранить в кэш → Возврат
// Следующие запросы → Кэш → Возврат (мгновенно)