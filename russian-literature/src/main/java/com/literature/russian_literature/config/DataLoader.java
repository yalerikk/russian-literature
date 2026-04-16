package com.literature.russian_literature.config;

import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.genres.db.GenreEntity;
import com.literature.russian_literature.genres.db.GenreRepository;
import com.literature.russian_literature.tags.db.TagEntity;
import com.literature.russian_literature.tags.db.TagRepository;
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
    private final TagRepository tagRepository;
    private final BookRepository bookRepository;

    public DataLoader(AuthorRepository authorRepository, GenreRepository genreRepository,
                      TagRepository tagRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        if (authorRepository.count() > 0) {
            System.out.println("✅ Данные уже загружены, пропускаем инициализацию");
            return;
        }

        System.out.println("🔄 Начало загрузки тестовых данных...");

        // ----- Жанры -----
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

        // ----- Теги -----
        List<TagEntity> tags = List.of(
                new TagEntity(null, "10 класс", TagType.GRADE),
                new TagEntity(null, "11 класс", TagType.GRADE),
                new TagEntity(null, "База", TagType.LEVEL),
                new TagEntity(null, "Профиль", TagType.LEVEL),
                new TagEntity(null, "Русская литература", TagType.CATEGORY),
                new TagEntity(null, "Иностранная литература", TagType.CATEGORY),
                new TagEntity(null, "Основное чтение", TagType.READING_TYPE),
                new TagEntity(null, "Летнее чтение", TagType.READING_TYPE),
                new TagEntity(null, "Дополнительное чтение", TagType.READING_TYPE)
        );
        tagRepository.saveAll(tags);

        // ----- Авторы -----
        List<AuthorEntity> authors = List.of(
                new AuthorEntity(null, "Александр", "Пушкин", "Сергеевич",
                        LocalDate.of(1799, 6, 6), LocalDate.of(1837, 2, 10),
                        "Великий русский поэт...",
                        "https://upload.wikimedia.org/wikipedia/commons/5/56/Kiprensky_Pushkin.jpg"),
                new AuthorEntity(null, "Лев", "Толстой", "Николаевич",
                        LocalDate.of(1828, 9, 9), LocalDate.of(1910, 11, 20),
                        "Один из наиболее известных русских писателей...",
                        "https://upload.wikimedia.org/wikipedia/commons/c/c6/L.N.Tolstoy_Prokudin-Gorsky.jpg"),
                new AuthorEntity(null, "Фёдор", "Достоевский", "Михайлович",
                        LocalDate.of(1821, 11, 11), LocalDate.of(1881, 2, 9),
                        "Русский писатель, мыслитель...",
                        "https://upload.wikimedia.org/wikipedia/commons/7/78/Fedor_Dostoevsky_1872.jpg"),
                new AuthorEntity(null, "Антон", "Чехов", "Павлович",
                        LocalDate.of(1860, 1, 29), LocalDate.of(1904, 7, 15),
                        "Русский писатель, драматург...",
                        "https://upload.wikimedia.org/wikipedia/commons/b/ba/Anton_Tschechow_1902.jpg"),
                new AuthorEntity(null, "Николай", "Гоголь", "Васильевич",
                        LocalDate.of(1809, 4, 1), LocalDate.of(1852, 3, 4),
                        "Русский прозаик, драматург...",
                        "https://upload.wikimedia.org/wikipedia/commons/0/07/Nikolay_Gogol.jpg"),
                new AuthorEntity(null, "Михаил", "Лермонтов", "Юрьевич",
                        LocalDate.of(1814, 10, 15), LocalDate.of(1841, 7, 27),
                        "Русский поэт, прозаик...",
                        "https://upload.wikimedia.org/wikipedia/commons/4/4e/Mikhail_Lermontov_%281837%29.jpg"),
                new AuthorEntity(null, "Иван", "Тургенев", "Сергеевич",
                        LocalDate.of(1818, 11, 9), LocalDate.of(1883, 9, 3),
                        "Русский писатель-реалист...",
                        "https://upload.wikimedia.org/wikipedia/commons/4/4b/Ivan_Turgenev_1867.jpg"),
                new AuthorEntity(null, "Александр", "Грибоедов", "Сергеевич",
                        LocalDate.of(1795, 1, 15), LocalDate.of(1829, 2, 11),
                        "Русский дипломат, поэт...",
                        "https://upload.wikimedia.org/wikipedia/commons/8/89/Alexander_Griboyedov_1820s.jpg"),
                new AuthorEntity(null, "Михаил", "Булгаков", "Афанасьевич",
                        LocalDate.of(1891, 5, 15), LocalDate.of(1940, 3, 10),
                        "Русский писатель, драматург...",
                        "https://upload.wikimedia.org/wikipedia/commons/e/ec/Mikhail_Bulgakov_2.jpg"),
                new AuthorEntity(null, "Максим", "Горький", "Алексеевич",
                        LocalDate.of(1868, 3, 28), LocalDate.of(1936, 6, 18),
                        "Русский писатель, прозаик, драматург...",
                        "https://example.com/gorky.jpg"),
                new AuthorEntity(null, "Иван", "Бунин", "Алексеевич",
                        LocalDate.of(1870, 10, 22), LocalDate.of(1953, 11, 8),
                        "Русский писатель, поэт, лауреат Нобелевской премии...",
                        "https://example.com/bunin.jpg"),
                new AuthorEntity(null, "Андрей", "Платонов", "Платонович",
                        LocalDate.of(1899, 9, 1), LocalDate.of(1951, 1, 5),
                        "Русский советский писатель, драматург...",
                        "https://example.com/platonov.jpg"),
                new AuthorEntity(null, "Михаил", "Шолохов", "Александрович",
                        LocalDate.of(1905, 5, 24), LocalDate.of(1984, 2, 21),
                        "Русский советский писатель, лауреат Нобелевской премии...",
                        "https://example.com/sholokhov.jpg"),
                new AuthorEntity(null, "Александр", "Вампилов", "Валентинович",
                        LocalDate.of(1937, 8, 19), LocalDate.of(1972, 8, 17),
                        "Русский драматург...",
                        "https://example.com/vampilov.jpg"),
                new AuthorEntity(null, "Евгений", "Замятин", "Иванович",
                        LocalDate.of(1884, 2, 1), LocalDate.of(1937, 3, 10),
                        "Русский писатель, критик...",
                        "https://example.com/zamyatin.jpg"),
                new AuthorEntity(null, "Эрнест", "Хемингуэй", "Миллер",
                        LocalDate.of(1899, 7, 21), LocalDate.of(1961, 7, 2),
                        "Американский писатель, лауреат Нобелевской премии...",
                        "https://example.com/hemingway.jpg")
        );
        authorRepository.saveAll(authors);

        // Удобные ссылки на авторов
        AuthorEntity pushkin = authors.get(0);
        AuthorEntity tolstoy = authors.get(1);
        AuthorEntity dostoevsky = authors.get(2);
        AuthorEntity chekhov = authors.get(3);
        AuthorEntity gogol = authors.get(4);
        AuthorEntity lermontov = authors.get(5);
        AuthorEntity turgenev = authors.get(6);
        AuthorEntity griboedov = authors.get(7);
        AuthorEntity bulgakov = authors.get(8);
        AuthorEntity gorky = authors.get(9);
        AuthorEntity bunin = authors.get(10);
        AuthorEntity platonov = authors.get(11);
        AuthorEntity sholokhov = authors.get(12);
        AuthorEntity vampilov = authors.get(13);
        AuthorEntity zamyatin = authors.get(14);
        AuthorEntity hemingway = authors.get(15);

        // Удобные ссылки на жанры
        GenreEntity novel = genres.get(0);
        GenreEntity poetry = genres.get(1);
        GenreEntity drama = genres.get(2);
        GenreEntity comedy = genres.get(5);

        // Удобные ссылки на теги (по индексам из списка)
        TagEntity grade10 = tags.get(0);
        TagEntity grade11 = tags.get(1);
        TagEntity levelBase = tags.get(2);
        TagEntity levelProfile = tags.get(3);
        TagEntity categoryRussian = tags.get(4);
        TagEntity categoryForeign = tags.get(5);
        TagEntity readingMain = tags.get(6);      // Основное чтение
        TagEntity readingSummer = tags.get(7);    // Летнее чтение
        TagEntity readingExtra = tags.get(8);     // Дополнительное чтение

        LocalDateTime now = LocalDateTime.now();
        String dummyFileUrl = "https://example.com/book.pdf";

        // ----- Книги с правильными тегами (согласно программам 10–11 классов) -----
        // ----- Книги (без файлов – только обложки и метаданные) -----
        List<BookEntity> books = List.of(
                createBook(null, "Евгений Онегин", 1833,
                        "Роман в стихах Александра Пушкина...",
                        pushkin,
                        "https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/6732071-aleksandr-pushkin-evgeneiy-onegin.jpg",
                        Set.of(novel, poetry), Set.of(grade10, levelBase, categoryRussian, readingSummer),
                        now.minusDays(5), now.minusDays(5)),

                createBook(null, "Война и мир", 1869,
                        "Роман-эпопея Льва Толстого...",
                        tolstoy,
                        "https://cv1.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715112-lev-tolstoy-voyna-i-mir-tom-1.jpg",
                        Set.of(novel), Set.of(grade10, levelBase, categoryRussian, readingMain),
                        now.minusDays(10), now.minusDays(10)),

                createBook(null, "Преступление и наказание", 1866,
                        "Социально-психологический роман Фёдора Достоевского...",
                        dostoevsky,
                        "https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715260-fedor-dostoevskiy-prestuplenie-i-nakazanie.jpg",
                        Set.of(novel), Set.of(grade10, levelBase, categoryRussian, readingMain),
                        now.minusDays(15), now.minusDays(15)),

                createBook(null, "Герой нашего времени", 1840,
                        "Роман Михаила Лермонтова...",
                        lermontov,
                        "https://cv5.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715145-mihail-lermontov-geroy-nashego-vremeni.jpg",
                        Set.of(novel), Set.of(grade10, levelBase, categoryRussian, readingSummer),
                        now.minusDays(40), now.minusDays(40)),

                createBook(null, "Мёртвые души", 1842,
                        "Поэма Николая Гоголя...",
                        gogol,
                        "https://cv8.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715259-nikolay-gogol-mertvye-dushi.jpg",
                        Set.of(novel), Set.of(grade10, levelBase, categoryRussian, readingMain),
                        now.minusDays(50), now.minusDays(50)),

                createBook(null, "Отцы и дети", 1862,
                        "Роман Ивана Тургенева...",
                        turgenev,
                        "https://cv5.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715139-ivan-turgenev-otcy-i-deti.jpg",
                        Set.of(novel), Set.of(grade10, levelBase, categoryRussian, readingMain),
                        now.minusDays(70), now.minusDays(70)),

                createBook(null, "Горе от ума", 1825,
                        "Комедия Александра Грибоедова...",
                        griboedov,
                        "https://cv4.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715138-aleksandr-griboedov-gore-ot-uma.jpg",
                        Set.of(comedy), Set.of(grade10, levelBase, categoryRussian, readingSummer),
                        now.minusDays(80), now.minusDays(80)),

                createBook(null, "Мастер и Маргарита", 1967,
                        "Роман Михаила Булгакова...",
                        bulgakov,
                        "https://cv5.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715123-mihail-bulgakov-master-i-margarita.jpg",
                        Set.of(novel), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(90), now.minusDays(90)),

                createBook(null, "Анна Каренина", 1877,
                        "Роман Льва Толстого...",
                        tolstoy,
                        "https://cv2.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715114-lev-tolstoy-anna-karenina.jpg",
                        Set.of(novel), Set.of(grade10, levelProfile, categoryRussian, readingExtra),
                        now.minusDays(100), now.minusDays(100)),

                createBook(null, "Вишнёвый сад", 1904,
                        "Пьеса Антона Чехова...",
                        chekhov,
                        "https://cv8.litres.ru/pub/c/elektronnaya-kniga/cover_415/6715136-anton-chehov-vishnevyy-sad.jpg",
                        Set.of(drama, comedy), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(110), now.minusDays(110)),

                createBook(null, "На дне", 1902,
                        "Пьеса Максима Горького о ночлежке, философские споры о правде и человеке...",
                        gorky,
                        "https://example.com/cover_na_dne.jpg",
                        Set.of(drama), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(120), now.minusDays(120)),

                createBook(null, "Мы", 1924,
                        "Роман-антиутопия Евгения Замятина, предвосхитивший тоталитарные режимы...",
                        zamyatin,
                        "https://example.com/cover_my.jpg",
                        Set.of(novel), Set.of(grade11, levelBase, categoryRussian, readingExtra),
                        now.minusDays(130), now.minusDays(130)),

                createBook(null, "Челкаш", 1895,
                        "Рассказ Максима Горького о босяке, контрабандисте и его нравственном выборе...",
                        gorky,
                        "https://example.com/cover_chelkash.jpg",
                        Set.of(genres.get(3)), // Рассказ
                        Set.of(grade11, levelProfile, categoryRussian, readingMain),
                        now.minusDays(140), now.minusDays(140)),

                createBook(null, "Старик и море", 1952,
                        "Повесть Эрнеста Хемингуэя о рыбаке Сантьяго и его борьбе с морем...",
                        hemingway,
                        "https://example.com/cover_oldman.jpg",
                        Set.of(novel), Set.of(grade11, levelProfile, categoryForeign, readingExtra),
                        now.minusDays(150), now.minusDays(150)),

                createBook(null, "Фро", 1936,
                        "Рассказ Андрея Платонова о любви, разлуке и вере в чудо...",
                        platonov,
                        "https://example.com/cover_fro.jpg",
                        Set.of(genres.get(3)), // Рассказ
                        Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(200), now.minusDays(200)),

                createBook(null, "На заре туманной юности", 1936,
                        "Рассказ Андрея Платонова о юности, выборе пути и преодолении...",
                        platonov,
                        "https://example.com/cover_zare.jpg",
                        Set.of(genres.get(3)), // Рассказ
                        Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(210), now.minusDays(210)),

                createBook(null, "Тихий Дон", 1940,
                        "Роман-эпопея Михаила Шолохова о судьбе казачества в годы войн и революций (избранные главы)...",
                        sholokhov,
                        "https://example.com/cover_tihiy_don.jpg",
                        Set.of(novel), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(300), now.minusDays(300)),

                createBook(null, "Утиная охота", 1970,
                        "Пьеса Александра Вампилова о кризисе советского интеллигента, экзистенциальном выборе...",
                        vampilov,
                        "https://example.com/cover_utinaya.jpg",
                        Set.of(drama), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(400), now.minusDays(400))
        );

        bookRepository.saveAll(books);

        System.out.println("✅ Тестовые данные загружены успешно!");
        System.out.println("📚 Авторов: " + authors.size());
        System.out.println("📖 Жанров: " + genres.size());
        System.out.println("🏷️ Тегов: " + tags.size());
        System.out.println("📕 Книг: " + books.size());
    }

    private BookEntity createBook(Long id, String title, Integer publicationYear,
                                  String description, AuthorEntity author,
                                  String coverUrl, Set<GenreEntity> genres, Set<TagEntity> tags,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        BookEntity book = new BookEntity();
        book.setId(id);
        book.setTitle(title);
        book.setPublicationYear(publicationYear);
        book.setDescription(description);
        book.setAuthor(author);
        book.setCoverUrl(coverUrl);
        book.setGenres(genres);
        book.setTags(tags);
        book.setCreatedAt(createdAt);
        book.setUpdatedAt(updatedAt);
        return book;
    }
}
