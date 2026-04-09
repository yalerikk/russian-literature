package com.literature.russian_literature.config;

import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.genres.db.GenreEntity;
import com.literature.russian_literature.genres.db.GenreRepository;
import com.literature.russian_literature.tags.db.BookTagEntity;
import com.literature.russian_literature.tags.db.BookTagRepository;
import com.literature.russian_literature.tags.domain.TagType;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookTagRepository bookTagRepository;
    private final BookRepository bookRepository;

    public DataLoader(AuthorRepository authorRepository, GenreRepository genreRepository,
                      BookTagRepository bookTagRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookTagRepository = bookTagRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли уже данные
        if (authorRepository.count() > 0) {
            System.out.println("✅ Данные уже загружены, пропускаем инициализацию");
            return;
        }

        System.out.println("🔄 Начало загрузки тестовых данных...");

        // Создаем жанры
        List<GenreEntity> genres = List.of(
                new GenreEntity(null, "Роман"),
                new GenreEntity(null, "Поэзия"),
                new GenreEntity(null, "Драма"),
                new GenreEntity(null, "Рассказ"),
                new GenreEntity(null, "Басня"),
                new GenreEntity(null, "Комедия"),
                new GenreEntity(null, "Трагедия"),
                new GenreEntity(null, "Фантастика")
        );
        genreRepository.saveAll(genres);

        // Создаем теги
        List<BookTagEntity> tags = List.of(
                new BookTagEntity(null, "10 класс", TagType.GRADE),
                new BookTagEntity(null, "11 класс", TagType.GRADE),
                new BookTagEntity(null, "База", TagType.LEVEL),
                new BookTagEntity(null, "Профиль", TagType.LEVEL),
                new BookTagEntity(null, "Русская литература", TagType.CATEGORY),
                new BookTagEntity(null, "Иностранная литература", TagType.CATEGORY),
                new BookTagEntity(null, "Основное", TagType.READING_TYPE),
                new BookTagEntity(null, "Летнее", TagType.READING_TYPE),
                new BookTagEntity(null, "Дополнительное", TagType.READING_TYPE)
        );
        bookTagRepository.saveAll(tags);

        // Создаем авторов
        List<AuthorEntity> authors = List.of(
                new AuthorEntity(null, "Александр", "Пушкин", "Сергеевич",
                        LocalDate.of(1799, 6, 6), LocalDate.of(1837, 2, 10),
                        "Великий русский поэт, драматург и прозаик, создатель современного русского литературного языка. Автор романа в стихах «Евгений Онегин», множества стихотворений, поэм и драматических произведений.",
                        "https://upload.wikimedia.org/wikipedia/commons/5/56/Kiprensky_Pushkin.jpg"),
                new AuthorEntity(null, "Лев", "Толстой", "Николаевич",
                        LocalDate.of(1828, 9, 9), LocalDate.of(1910, 11, 20),
                        "Один из наиболее известных русских писателей и мыслителей, один из величайших писателей-романистов мира. Автор романов «Война и мир», «Анна Каренина», «Воскресение».",
                        "https://upload.wikimedia.org/wikipedia/commons/c/c6/L.N.Tolstoy_Prokudin-Gorsky.jpg"),
                new AuthorEntity(null, "Фёдор", "Достоевский", "Михайлович",
                        LocalDate.of(1821, 11, 11), LocalDate.of(1881, 2, 9),
                        "Русский писатель, мыслитель, философ и публицист. Автор романов «Преступление и наказание», «Идиот», «Братья Карамазовы», «Бесы».",
                        "https://upload.wikimedia.org/wikipedia/commons/7/78/Fedor_Dostoevsky_1872.jpg"),
                new AuthorEntity(null, "Антон", "Чехов", "Павлович",
                        LocalDate.of(1860, 1, 29), LocalDate.of(1904, 7, 15),
                        "Русский писатель, прозаик, драматург. Классик мировой литературы. Автор пьес «Чайка», «Три сестры», «Вишнёвый сад», множества рассказов.",
                        "https://upload.wikimedia.org/wikipedia/commons/b/ba/Anton_Tschechow_1902.jpg"),
                new AuthorEntity(null, "Николай", "Гоголь", "Васильевич",
                        LocalDate.of(1809, 4, 1), LocalDate.of(1852, 3, 4),
                        "Русский прозаик, драматург, поэт, критик, публицист. Автор «Мёртвых душ», «Ревизора», «Вечеров на хуторе близ Диканьки», «Тараса Бульбы».",
                        "https://upload.wikimedia.org/wikipedia/commons/0/07/Nikolay_Gogol.jpg"),
                new AuthorEntity(null, "Михаил", "Лермонтов", "Юрьевич",
                        LocalDate.of(1814, 10, 15), LocalDate.of(1841, 7, 27),
                        "Русский поэт, прозаик, драматург, художник. Автор романа «Герой нашего времени», поэмы «Демон», стихотворения «Смерть поэта».",
                        "https://upload.wikimedia.org/wikipedia/commons/4/4e/Mikhail_Lermontov_%281837%29.jpg"),
                new AuthorEntity(null, "Иван", "Тургенев", "Сергеевич",
                        LocalDate.of(1818, 11, 9), LocalDate.of(1883, 9, 3),
                        "Русский писатель-реалист, поэт, публицист, драматург, переводчик. Автор романов «Отцы и дети», «Дворянское гнездо», «Накануне».",
                        "https://upload.wikimedia.org/wikipedia/commons/4/4b/Ivan_Turgenev_1867.jpg"),
                new AuthorEntity(null, "Александр", "Грибоедов", "Сергеевич",
                        LocalDate.of(1795, 1, 15), LocalDate.of(1829, 2, 11),
                        "Русский дипломат, поэт, драматург, пианист и композитор. Автор комедии «Горе от ума».",
                        "https://upload.wikimedia.org/wikipedia/commons/8/89/Alexander_Griboyedov_1820s.jpg"),
                new AuthorEntity(null, "Михаил", "Булгаков", "Афанасьевич",
                        LocalDate.of(1891, 5, 15), LocalDate.of(1940, 3, 10),
                        "Русский писатель, драматург, театральный режиссёр и актёр. Автор романов «Мастер и Маргарита», «Белая гвардия», «Собачье сердце».",
                        "https://upload.wikimedia.org/wikipedia/commons/e/ec/Mikhail_Bulgakov_2.jpg")
        );
        authorRepository.saveAll(authors);

        // Создаем тестовые книги
        AuthorEntity pushkin = authors.get(0);
        AuthorEntity tolstoy = authors.get(1);
        AuthorEntity dostoevsky = authors.get(2);
        AuthorEntity chekhov = authors.get(3);
        AuthorEntity gogol = authors.get(4);
        AuthorEntity lermontov = authors.get(5);
        AuthorEntity turgenev = authors.get(6);
        AuthorEntity griboedov = authors.get(7);
        AuthorEntity bulgakov = authors.get(8);

        Set<GenreEntity> novelGenres = Set.of(genres.get(0)); // Роман
        Set<GenreEntity> poetryGenres = Set.of(genres.get(1)); // Поэзия
        Set<GenreEntity> dramaGenres = Set.of(genres.get(2)); // Драма
        Set<GenreEntity> storyGenres = Set.of(genres.get(4)); // Рассказ
        Set<GenreEntity> comedyGenres = Set.of(genres.get(6)); // Комедия

        // Готовим наборы тегов
        Set<BookTagEntity> grade10BaseRussian = Set.of(tags.get(0), tags.get(2), tags.get(4)); // 10 класс, База, Русская
        Set<BookTagEntity> grade10ProfileRussian = Set.of(tags.get(0), tags.get(3), tags.get(4)); // 10 класс, Профиль, Русская
        Set<BookTagEntity> grade11BaseRussian = Set.of(tags.get(1), tags.get(2), tags.get(4)); // 11 класс, База, Русская
        Set<BookTagEntity> grade11ProfileRussian = Set.of(tags.get(1), tags.get(3), tags.get(4)); // 11 класс, Профиль, Русская

        LocalDateTime now = LocalDateTime.now();

        // Создаем тестовые книги с разными датами создания для тестирования "новинок"
        List<BookEntity> books = List.of(
                // Новые книги (последние 30 дней)
                createBook(null, "Евгений Онегин", 1833,
                        "Роман в стихах Александра Пушкина, одно из самых значительных произведений русской литературы. Рассказывает о жизни светского молодого человека.",
                        pushkin, StorageType.TEXT,
                        "Мой дядя самых честных правил,\nКогда не в шутку занемог,\nОн уважать себя заставил\nИ лучше выдумать не мог...",
                        null,
                        "https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/6732071-aleksandr-pushkin-evgeneiy-onegin.jpg",
                        novelGenres, grade10BaseRussian,
                        now.minusDays(5), now.minusDays(5)),

                createBook(null, "Война и мир", 1869,
                        "Роман-эпопея Льва Толстого, описывающий русское общество в эпоху войн против Наполеона в 1805—1812 годах.",
                        tolstoy, StorageType.TEXT,
                        "— Eh bien, mon prince. Gênes et Lucques ne sont plus que des apanages, des поместья, de la famille Buonaparte...",
                        null,
                        "https://cv1.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715112-lev-tolstoy-voyna-i-mir-tom-1.jpg",
                        novelGenres, grade11ProfileRussian,
                        now.minusDays(10), now.minusDays(10)),

                createBook(null, "Преступление и наказание", 1866,
                        "Социально-психологический и социально-философский роман Фёдора Достоевского, посвящённый нравственным проблемам.",
                        dostoevsky, StorageType.TEXT,
                        "В начале июля, в чрезвычайно жаркое время, под вечер, один молодой человек вышел из своей каморки...",
                        null,
                        "https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715260-fedor-dostoevskiy-prestuplenie-i-nakazanie.jpg",
                        novelGenres, grade11BaseRussian,
                        now.minusDays(15), now.minusDays(15)),

                // Книги среднего возраста (30-60 дней)
                createBook(null, "Герой нашего времени", 1840,
                        "Роман Михаила Лермонтова, состоящий из нескольких повестей, объединённых главным героем — Печориным.",
                        lermontov, StorageType.TEXT,
                        "— Аксинья! — крикнул я, входя в сени. — Нет дома! — отвечал мне из-за дверей грубый голос.",
                        null,
                        "https://cv5.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715145-mihail-lermontov-geroy-nashego-vremeni.jpg",
                        novelGenres, grade10ProfileRussian,
                        now.minusDays(40), now.minusDays(40)),

                createBook(null, "Мёртвые души", 1842,
                        "Поэма Николая Гоголя, повествующая о похождениях Чичикова, скупающего «мёртвые души» крестьян.",
                        gogol, StorageType.TEXT,
                        "В ворота гостиницы губернского города NN въехала довольно красивая рессорная небольшая бричка...",
                        null,
                        "https://cv8.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715259-nikolay-gogol-mertvye-dushi.jpg",
                        novelGenres, grade10BaseRussian,
                        now.minusDays(50), now.minusDays(50)),

                // Старые книги (более 60 дней)
                createBook(null, "Отцы и дети", 1862,
                        "Роман Ивана Тургенева, затрагивающий тему конфликта поколений и нигилизма.",
                        turgenev, StorageType.TEXT,
                        "— Что, Петр? Не видать еще? — спрашивал 20 мая 1859 года, выходя без шапки на низкое крылечко постоялого двора...",
                        null,
                        "https://cv5.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715139-ivan-turgenev-otcy-i-deti.jpg",
                        novelGenres, grade11BaseRussian,
                        now.minusDays(70), now.minusDays(70)),

                createBook(null, "Горе от ума", 1825,
                        "Комедия в стихах Александра Грибоедова, сатира на аристократическое московское общество.",
                        griboedov, StorageType.TEXT,
                        "День чудесный!..\nВо всю жизнь такую ночь не видывал!..\nА уж я люблю, признаться, почитать...",
                        null,
                        "https://cv4.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715138-aleksandr-griboedov-gore-ot-uma.jpg",
                        comedyGenres, grade10BaseRussian,
                        now.minusDays(80), now.minusDays(80)),

                createBook(null, "Мастер и Маргарита", 1967,
                        "Роман Михаила Булгакова, сочетающий в себе элементы сатиры, фантастики и философской притчи.",
                        bulgakov, StorageType.TEXT,
                        "В час жаркого весеннего заката на Патриарших прудах появились два гражданина.",
                        null,
                        "https://cv5.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715123-mihail-bulgakov-master-i-margarita.jpg",
                        novelGenres, grade11ProfileRussian,
                        now.minusDays(90), now.minusDays(90)),

                createBook(null, "Анна Каренина", 1877,
                        "Роман Льва Толстого о трагической любви замужней Анны Карениной к блестящему офицеру Вронскому.",
                        tolstoy, StorageType.TEXT,
                        "Все счастливые семьи похожи друг на друга, каждая несчастливая семья несчастлива по-своему.",
                        null,
                        "https://cv2.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715114-lev-tolstoy-anna-karenina.jpg",
                        novelGenres, grade11BaseRussian,
                        now.minusDays(100), now.minusDays(100)),

                createBook(null, "Вишнёвый сад", 1904,
                        "Лирическая пьеса Антона Чехова в четырёх действиях, рассказывающая о жизни зажиточных дворян.",
                        chekhov, StorageType.TEXT,
                        "Комната, которая до сих пор называется детскою...",
                        null,
                        "https://cv8.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715136-anton-chehov-vishnevyy-sad.jpg",
                        dramaGenres, grade10BaseRussian,
                        now.minusDays(110), now.minusDays(110))
        );
        bookRepository.saveAll(books);

        System.out.println("✅ Тестовые данные загружены успешно!");
        System.out.println("📚 Создано авторов: " + authors.size());
        System.out.println("📖 Создано жанров: " + genres.size());
        System.out.println("🏷️ Создано тегов: " + tags.size());
        System.out.println("📕 Создано книг: " + books.size());

        // Для проверки новинок
        System.out.println("\n📊 Для тестирования категории 'Новинки' (последние 30 дней):");
        books.stream()
                .filter(book -> book.getCreatedAt().isAfter(now.minusDays(30)))
                .forEach(book -> System.out.println("   • " + book.getTitle() + " (" + book.getCreatedAt().toLocalDate() + ")"));
    }

    // Вспомогательный метод для создания BookEntity
    private BookEntity createBook(Long id, String title, Integer publicationYear,
                                  String description, AuthorEntity author, StorageType storageType,
                                  String content, String filePath, String coverUrl,
                                  Set<GenreEntity> genres, Set<BookTagEntity> tags,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        BookEntity book = new BookEntity();
        book.setId(id);
        book.setTitle(title);
        book.setPublicationYear(publicationYear);
        book.setDescription(description);
        book.setAuthor(author);
        book.setStorageType(storageType);
        book.setContent(content);
        book.setFilePath(filePath);
        book.setCoverUrl(coverUrl);
        book.setGenres(genres);
        book.setEducationalTags(tags);
        book.setCreatedAt(createdAt);
        book.setUpdatedAt(updatedAt);
        return book;
    }
}