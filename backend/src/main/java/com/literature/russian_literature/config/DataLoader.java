package com.literature.russian_literature.config;

import com.literature.russian_literature.authors.db.AuthorEntity;
import com.literature.russian_literature.authors.db.AuthorRepository;
import com.literature.russian_literature.books.db.BookEntity;
import com.literature.russian_literature.books.db.BookFileEntity;
import com.literature.russian_literature.books.db.BookRepository;
import com.literature.russian_literature.books.db.BookFileRepository;
import com.literature.russian_literature.books.domain.BookFormat;
import com.literature.russian_literature.genres.db.GenreEntity;
import com.literature.russian_literature.genres.db.GenreRepository;
import com.literature.russian_literature.tags.db.TagEntity;
import com.literature.russian_literature.tags.db.TagRepository;
import com.literature.russian_literature.tags.domain.TagType;
import com.literature.russian_literature.users.db.UserEntity;
import com.literature.russian_literature.users.db.UserRepository;
import com.literature.russian_literature.users.domain.UserRole;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;
    private final BookRepository bookRepository;
    private final BookFileRepository bookFileRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(AuthorRepository authorRepository, GenreRepository genreRepository,
                      TagRepository tagRepository, BookRepository bookRepository, BookFileRepository bookFileRepository,
                      UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
        this.bookFileRepository = bookFileRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (authorRepository.count() > 0) {
            System.out.println("✅ Данные уже загружены, пропускаем инициализацию");
            return;
        }

        System.out.println("🔄 Начало загрузки тестовых данных...");

        // ----- Пользователи -----
        if (userRepository.count() == 0) {
            var admin = new UserEntity(null, "admin", "admin@gmail.com", passwordEncoder.encode("admin123"), UserRole.ADMIN);
            var reader = new UserEntity(null, "user", "user@mail.ru", passwordEncoder.encode("user123"), UserRole.READER);
            userRepository.saveAll(List.of(admin, reader));
            System.out.println("👥 Добавлены тестовые пользователи: admin/admin123, user/user123");
        }

        // ----- Жанры -----
        List<GenreEntity> genres = List.of(
                new GenreEntity(null, "Роман"),
                new GenreEntity(null, "Поэзия"),
                new GenreEntity(null, "Драма"),
                new GenreEntity(null, "Рассказ"),
                new GenreEntity(null, "Басня"),
                new GenreEntity(null, "Комедия"),
                new GenreEntity(null, "Трагедия"),
                new GenreEntity(null, "Фантастика"),
                new GenreEntity(null, "Повесть"),
                new GenreEntity(null, "Сатира"),
                new GenreEntity(null, "Антиутопия"),
                new GenreEntity(null, "Криминал"),
                new GenreEntity(null, "Философия"),
                new GenreEntity(null, "Психология"),
                new GenreEntity(null, "Реализм"),
                new GenreEntity(null, "История"),
                new GenreEntity(null, "Война")
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
                        "Родился в Москве в дворянской семье. Воспитывался французскими гувернерами. Учился в Царскосельском лицее, где начал писать стихи. Был близок к декабристам, за вольнодумство отправлен в южную ссылку (1820–1824), затем в михайловскую ссылку. Создал «Евгения Онегина», «Бориса Годунова», «Маленькие трагедии», «Повести Белкина», «Капитанскую дочку». Основоположник современного русского литературного языка. Погиб на дуэли с Дантесом, защищая честь жены. Его творчество охватывает романтизм и реализм.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285326/Pushkin_xjimga.jpg"),
                new AuthorEntity(null, "Лев", "Толстой", "Николаевич",
                        LocalDate.of(1828, 9, 9), LocalDate.of(1910, 11, 20),
                        "Граф, родился в усадьбе Ясная Поляна. Участвовал в Крымской войне, писал «Севастопольские рассказы». Автор эпопей «Война и мир» и «Анна Каренина». В 1880‑е пережил духовный кризис, пришел к непротивлению злу насилием, отлучен от церкви. Создал философско-религиозное учение, писал повести «Смерть Ивана Ильича», «Крейцерова соната». В конце жизни ушел из Ясной Поляны и умер на станции Астапово. Один из величайших романистов мира.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285400/Lev_Tolstoy_eulzos.jpg"),
                new AuthorEntity(null, "Федор", "Достоевский", "Михайлович",
                        LocalDate.of(1821, 11, 11), LocalDate.of(1881, 2, 9),
                        "Родился в Москве в семье врача. Учился в Петербургском инженерном училище. В молодости входил в кружок петрашевцев, приговорен к расстрелу, замененному каторгой (4 года в Омске). После каторги написал «Записки из Мертвого дома», «Преступление и наказание», «Идиота», «Бесов», «Братьев Карамазовых». Исследовал природу добра и зла, свободу, веру, безверие, русскую душу. Его творчество оказало влияние на экзистенциализм и психоанализ.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285478/Dostoevskiy_abicdb.jpg"),
                new AuthorEntity(null, "Антон", "Чехов", "Павлович",
                        LocalDate.of(1860, 1, 29), LocalDate.of(1904, 7, 15),
                        "Родился в Таганроге в купеческой семье. Окончил медицинский факультет Московского университета, работал врачом. Начинал как автор юмористических рассказов («Толстый и тонкий», «Хамелеон»). Зрелые рассказы: «Палата №6», «Человек в футляре», «Дама с собачкой». Создал новаторскую драматургию («Чайка», «Три сестры», «Вишневый сад»). Боролся с туберкулезом, много путешествовал. Умер в Германии. Его проза и драматургия полны лиризма, недосказанности, любви к «маленькому человеку».",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285527/Chekhov_t5wdyc.jpg"),
                new AuthorEntity(null, "Николай", "Гоголь", "Васильевич",
                        LocalDate.of(1809, 4, 1), LocalDate.of(1852, 3, 4),
                        "Родился на Украине, в селе Великие Сорочинцы. Учился в Нежинской гимназии. Прославился «Вечерами на хуторе близ Диканьки» (мистика и малороссийский колорит). Позже написал «Миргород», «Тарас Бульбу», «Ревизора», «Мертвые души». Жил в Италии, пережил глубокий духовный кризис, сжег второй том «Мертвых душ». Умер в Москве. Его творчество сочетает гротеск, сатиру, фантастику и лиризм.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285593/Gogol_yzi4oy.jpg"),
                new AuthorEntity(null, "Михаил", "Лермонтов", "Юрьевич",
                        LocalDate.of(1814, 10, 15), LocalDate.of(1841, 7, 27),
                        "Родился в Москве, рано потерял мать, воспитывался бабушкой. Учился в Московском университете, затем в школе гвардейских прапорщиков. После стихотворения «Смерть поэта» (на гибель Пушкина) сослан на Кавказ. Создал роман «Герой нашего времени», драму «Маскарад», поэмы «Демон», «Мцыри». Погиб на дуэли с Мартыновым. Его лирика и проза полны разочарования, бунтарства, кавказских мотивов.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285614/Mikhail_lermontov_dx4aus.jpg"),
                new AuthorEntity(null, "Иван", "Тургенев", "Сергеевич",
                        LocalDate.of(1818, 11, 9), LocalDate.of(1883, 9, 3),
                        "Родился в Орле, в богатой дворянской семье. Учился в Петербургском и Берлинском университетах. Прославился «Записками охотника», направленными против крепостничества. Автор романов «Рудин», «Дворянское гнездо», «Накануне», «Отцы и дети». Жил в основном за границей (в Баден-Бадене, Париже), пропагандировал русскую литературу на Западе. Умер во Франции, тело перевезено в Петербург.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285793/Turgenev_qte5sl.jpg"),
                new AuthorEntity(null, "Александр", "Грибоедов", "Сергеевич",
                        LocalDate.of(1795, 1, 15), LocalDate.of(1829, 2, 11),
                        "Родился в Москве, получил блестящее образование. Служил дипломатом на Кавказе и в Персии. Участвовал в русско-персидской войне. Написал одну гениальную комедию в стихах «Горе от ума» (полностью опубликована после его смерти). Убит при разгроме русского посольства в Тегеране. Тело перевезено в Тифлис. Его комедия разошлась на цитаты.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285831/Griboyedov_elleb3.jpg"),
                new AuthorEntity(null, "Михаил", "Булгаков", "Афанасьевич",
                        LocalDate.of(1891, 5, 15), LocalDate.of(1940, 3, 10),
                        "Родился в Киеве в семье профессора богословия. Окончил медицинский факультет, работал земским врачом. В 1920‑е переехал в Москву, писал повести «Роковые яйца», «Собачье сердце», пьесы «Дни Турбиных», «Бег». Был запрещен, мало печатался. Втайне создавал главный роман «Мастер и Маргарита», который вышел только после его смерти. Умер в Москве. Его творчество — сатира на советскую действительность, мистика, гротеск.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285859/Bulgakov_kgrxxw.jpg"),
                new AuthorEntity(null, "Максим", "Горький", "Алексеевич",
                        LocalDate.of(1868, 3, 28), LocalDate.of(1936, 6, 18),
                        "Родился в Нижнем Новгороде, рано осиротел, скитался по Руси. Первый рассказ «Макар Чудра». Прославился пьесой «На дне» и повестью «Фома Гордеев». После революции 1905 года эмигрировал, жил на Капри. Вернулся в СССР в 1928 году, возглавил Союз писателей. Написал романы «Мать», «Жизнь Клима Самгина», цикл автобиографических повестей («Детство», «В людях»). Умер не своей смертью (подозревают отравление). Считался основоположником социалистического реализма.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285873/Gorky_w1stzd.png"),
                new AuthorEntity(null, "Иван", "Бунин", "Алексеевич",
                        LocalDate.of(1870, 10, 22), LocalDate.of(1953, 11, 8),
                        "Родился в Воронеже в обедневшей дворянской семье. Писал стихи и прозу. Прославился повестью «Деревня» и «Господин из Сан-Франциско». Эмигрировал после революции, жил во Франции. Создал роман «Жизнь Арсеньева», сборник «Темные аллеи». Первый русский нобелевский лауреат по литературе (1933). Его проза стилистически безупречна, проникнута памятью о России и трагическим ощущением эмиграции.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285913/Bunin_zlbjwd.jpg"),
                new AuthorEntity(null, "Андрей", "Платонов", "Платонович",
                        LocalDate.of(1899, 9, 1), LocalDate.of(1951, 1, 5),
                        "Родился в Воронеже в семье слесаря. Работал помощником машиниста, мелиоратором. В 1920‑е опубликовал книги публицистики и стихов. Главные произведения: повести «Котлован», «Чевенгур», «Ювенильное море», рассказы «Фро», «На заре туманной юности». Был запрещен за критику коллективизации. При жизни почти не печатался. Умер от туберкулеза. Его уникальный «детский» язык и философия «несчастных людей» стали открытием для читателей 1960–80‑х.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285956/Platonov_shw8vr.jpg"),
                new AuthorEntity(null, "Михаил", "Шолохов", "Александрович",
                        LocalDate.of(1905, 5, 24), LocalDate.of(1984, 2, 21),
                        "Родился на хуторе Кружилин (Ростовская область) в семье служащего. Участвовал в гражданской войне. Написал «Донские рассказы». Главный труд — «Тихий Дон» (награжден Нобелевской премией 1965). Также создал роман «Поднятая целина» и рассказ «Судьба человека». Жил в станице Вешенской, депутат Верховного совета. Его эпос о казачестве остается летописью русской трагедии XX века.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779285980/Sholokhov_wsovs3.jpg"),
                new AuthorEntity(null, "Александр", "Вампилов", "Валентинович",
                        LocalDate.of(1937, 8, 19), LocalDate.of(1972, 8, 17),
                        "Родился в поселке Кутулик Иркутской области в семье учителей. Окончил Иркутский университет, работал журналистом. Автор пьес «Прощание в июне», «Старший сын», «Утиная охота», «Прошлым летом в Чулимске». Трагически погиб, утонув на Байкале. Его драматургия — одна из вершин «новой волны» в советском театре 1960–70‑х, исследующая одиночество, ложь и невозможность подлинной жизни.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779286007/Vampilov_xzairh.jpg"),
                new AuthorEntity(null, "Евгений", "Замятин", "Иванович",
                        LocalDate.of(1884, 2, 1), LocalDate.of(1937, 3, 10),
                        "Родился в Лебедяни Тамбовской губернии в семье священника. Инженер-кораблестроитель, учился в Петербургском политехническом. Писал повести «Уездное», «На куличках». Самый известный роман — «Мы» (написан в 1920‑м, запрещен, опубликован на Западе). Подвергся травле, в 1931 году эмигрировал, умер в Париже. Родоначальник жанра антиутопии в русской литературе.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779286069/Zamyatin_o5jyp1.jpg"),
                new AuthorEntity(null, "Эрнест", "Хемингуэй", "Миллер",
                        LocalDate.of(1899, 7, 21), LocalDate.of(1961, 7, 2),
                        "Американский писатель, родился в Оук-Парке (Иллинойс). Участвовал в Первой мировой войне шофером санитарной машины. В 1920‑е жил в Париже («потерянное поколение»). Автор романов «Фиеста», «Прощай, оружие!», «По ком звонит колокол», повести «Старик и море». Лауреат Нобелевской премии (1954). Покончил с собой. Его «телеграфный стиль», концепция «кодекса чести» повлияли на мировую литературу.",
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779286075/Hemingway_jbswku.jpg")
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
        GenreEntity rasskaz = genres.get(3);
        GenreEntity comedy = genres.get(5);
        GenreEntity tragedy = genres.get(6);
        GenreEntity fantasy = genres.get(7);
        GenreEntity story = genres.get(8);
        GenreEntity satire = genres.get(9);
        GenreEntity dystopia = genres.get(10);
        GenreEntity crime = genres.get(11);
        GenreEntity philosophy = genres.get(12);
        GenreEntity psychology = genres.get(13);
        GenreEntity realism = genres.get(14);
        GenreEntity history = genres.get(15);
        GenreEntity war = genres.get(16);

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

        // ----- Книги -----
        List<BookEntity> books = List.of(
                createBook(null, "Евгений Онегин", 1833,
                        "Роман в стихах, ставший энциклопедией русской жизни 1820-х годов. Главный герой — молодой петербургский аристократ Евгений Онегин, разочарованный в свете и страдающий от «хандры». Он уезжает в деревню, где знакомится с поэтом-романтиком Владимиром Ленским и семьей Лариных. Добросердечная Татьяна Ларина влюбляется в Онегина, пишет ему письмо с признанием, но получает холодный отказ. Позже, на балу в честь именин Татьяны, Онегин флиртует с ее сестрой Ольгой, что приводит к дуэли с Ленским и гибели последнего. Через несколько лет Онегин встречает Татьяну в Петербурге — теперь она замужняя княгиня, блистательная и недоступная. Онегин страстно влюбляется, но получает отказ: «Я вас люблю — к чему лукавить? — но я другому отдана; я буду век ему верна». Роман сочетает лирические отступления, иронию и глубокий психологизм.",
                        pushkin,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283039/eugene-onegin_qpwoia.webp",
                        Set.of(novel, poetry, realism), Set.of(grade10, levelBase, categoryRussian, readingSummer),
                        now.minusDays(5), now.minusDays(5)),

                createBook(null, "Война и мир", 1869,
                        "Масштабная эпопея, охватывающая события наполеоновских войн 1805–1812 годов и послевоенное время. В центре — три дворянские семьи: Болконские, Ростовы и Безуховы. Андрей Болконский ищет славы, терпит разочарование в Аустерлицком сражении и находит покой в любви к Наташе Ростовой, но их счастье рушится из-за интриг. Пьер Безухов проходит через масонство, плен и духовное прозрение. Наташа из восторженной девушки превращается в глубоко чувствующую женщину. Главное сражение — Бородино — показано глазами участников. Толстой создает сотни персонажей, размышляет о свободе воли, историческом детерминизме и роли народа в войне. Роман заканчивается мирной жизнью героев и размышлениями о движущих силах истории.",
                        tolstoy,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283173/voina-i-mir_fnsbxf.jpg",
                        Set.of(novel, philosophy, history, war), Set.of(grade10, levelBase, categoryRussian, readingMain),
                        now.minusDays(10), now.minusDays(10)),

                createBook(null, "Преступление и наказание", 1866,
                        "Социально-философский роман о студенте Родионе Раскольникове, который убивает старуху-процентщицу и ее сестру, чтобы проверить свою теорию о «праве сильной личности» переступать мораль. После преступления герой погружается в лихорадку, бред и мучительное чувство отчуждения. Встреча с Соней Мармеладовой, кроткой девушкой, вынужденной торговать собой ради семьи, становится началом духовного перелома. Следователь Порфирий Петрович ведет психологическую дуэль с Раскольниковым. В эпилоге, на каторге, герой понимает, что счастье не в теории, а в любви и вере. Роман исследует природу зла, возможность искупления, тему «униженных и оскорбленных».",
                        dostoevsky,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283085/prestuplenie-i-nakazanie_h9y4wr.jpg",
                        Set.of(novel, crime, philosophy, psychology), Set.of(grade10, levelBase, categoryRussian, readingMain),
                        now.minusDays(15), now.minusDays(15)),

                createBook(null, "Герой нашего времени", 1840,
                        "Первый русский психологический роман, состоящий из пяти повестей, расположенных не в хронологическом порядке. Главный герой — Григорий Печорин, офицер, умный, циничный и глубоко несчастный человек, приносящий страдания всем, кто его окружает. Он уничтожает жизнь Бэлы (горской княжны), разрушает мечты княжны Мери, становится причиной дуэли с Грушницким, а также обнаруживает, что сам давно потерял способность искренне любить. Повесть «Фаталист» ставит вопрос о предопределении и свободе воли. Лермонтов создает «лишнего человека» — тип, который будет повторяться в русской литературе. Печорин одновременно привлекателен и отвратителен; его трагедия — в отсутствии высокой цели.",
                        lermontov,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283225/geroj-nashego-vremeni_jmwxsr.jpg",
                        Set.of(novel, philosophy, psychology), Set.of(grade10, levelBase, categoryRussian, readingSummer),
                        now.minusDays(4), now.minusDays(4)),

                createBook(null, "Мертвые души", 1842,
                        "Поэма о мошеннике Павле Ивановиче Чичикове, который ездит по помещикам и скупает «мертвые души» — умерших крепостных, числящихся живыми по ревизским сказкам, чтобы заложить их в опекунский совет и получить кредит. Гоголь создает галерею помещиков: мечтательный Манилов, расточительная Коробочка, авантюрист Ноздрев, «кулак» Собакевич и скряга Плюшкин — каждый олицетворяет одну из человеческих страстей или пороков. Сатира обнажает гниение крепостнической системы, взяточничество чиновников, пошлость и пустоту существования. Первый том заканчивается знаменитой лирической «птицей-тройкой» — символом России, несущейся в неизвестность.",
                        gogol,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283349/mertvie-dushi_kymnzi.jpg",
                        Set.of(poetry, novel, satire), Set.of(grade10, levelBase, categoryRussian, readingMain),
                        now.minusDays(50), now.minusDays(50)),

                createBook(null, "Отцы и дети", 1862,
                        "Ключевой роман о конфликте поколений и идеологий. В имение Кирсановых приезжает студент-медик Евгений Базаров — нигилист, отрицающий искусство, любовь, авторитеты. Его друг Аркадий Кирсанов увлекается новыми веяниями, но не способен на подлинную «ломку» старого мира. Базаров сталкивается с консервативным дядей Аркадия — Павлом Петровичем, аристократом, защищающим принципы. Идейные споры перерастают в дуэль. Настоящим испытанием для Базарова становится любовь к умной и гордой помещице Анне Одинцовой. Он оказывается не способен на глубокое чувство, а в конце умирает от заражения крови, успев признать бессилие своего материализма.",
                        turgenev,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283418/otcy-i-deti_rr0wzu.jpg",
                        Set.of(novel, philosophy, psychology), Set.of(grade10, levelBase, categoryRussian, readingMain),
                        now.minusDays(70), now.minusDays(70)),

                createBook(null, "Горе от ума", 1825,
                        "Комедия в стихах, сатира на московское дворянство начала XIX века. Молодой дворянин Александр Чацкий возвращается в Москву после длительного путешествия и обнаруживает, что его возлюбленная Софья влюблена в карьериста и лицемера Молчалина. Чацкий обличает чинопочитание, крепостничество, подражание иностранному, невежество и консерватизм. Его речи полны афоризмов («А судьи кто?», «Служить бы рад, прислуживаться тошно»). В ответ общество объявляет его сумасшедшим. В финале Чацкий, поняв, что «в Москве все не свои», покидает ее, восклицая: «Карету мне, карету!». Пьеса остается острейшей сатирой на бюрократическую косность.",
                        griboedov,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283473/gore-ot-uma_alozy0.jpg",
                        Set.of(drama, comedy, satire), Set.of(grade10, levelBase, categoryRussian, readingSummer),
                        now.minusDays(80), now.minusDays(80)),

                createBook(null, "Мастер и Маргарита", 1967,
                        "Мистический роман в трех сюжетных линиях: сатирическая Москва 1930-х, куда является Воланд (дьявол) со своей свитой; любовная история Мастера и Маргариты; ершалаимские главы о Понтии Пилате и Иешуа Га-Ноцри. Воланд разоблачает жадность, трусость и мелочность советской бюрократии. Маргарита, чтобы спасти Мастера, становится королевой бала сатаны. Роман Мастера о Пилате — это вечный сюжет о силе и слабости, о прощении и невозможности покоя. Булгаков смешивает комическое и трагическое, реальное и фантастическое. В финале Мастер и Маргарита обретают покой (но не свет), а Воланд произносит: «Рукописи не горят».",
                        bulgakov,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283544/master-i-margarita_ynemgf.webp",
                        Set.of(novel, fantasy, satire), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(90), now.minusDays(90)),

                createBook(null, "Анна Каренина", 1877,
                        "Трагедия замужней женщины, которая бросает благополучную, но холодную семью (мужа-министра Каренина) ради страстной любви к блестящему офицеру Вронскому. Анна нарушает законы света, теряет положение, не может видеться с сыном. Постепенно ее мучает ревность, недоверие к Вронскому, чувство вины и одиночество. В финале она бросается под поезд. Параллельная линия — Константин Левин, ищущий смысл жизни в труде, семье и вере, — примиряет читателя с миром. Толстой исследует природу любви, ревности, брака, лицемерие высшего общества и проблему женской эмансипации. Роман знаменит «диалектикой души» героев.",
                        tolstoy,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283136/anna-karenina_moyr4q.jpg",
                        Set.of(novel, realism, tragedy, philosophy, psychology), Set.of(grade10, levelProfile, categoryRussian, readingExtra),
                        now.minusDays(100), now.minusDays(100)),

                createBook(null, "Вишневый сад", 1904,
                        "Последняя пьеса Чехова, лирическая комедия (как назвал ее сам автор) о разорении дворянского гнезда. Помещица Раневская возвращается из Парижа в свое имение с вишневым садом, которое за долги продается с торгов. Ее брат Гаев беспомощен, дочь Аня мечтает об учебе. Предприимчивый купец Лопахин предлагает вырубить сад и сдать землю под дачи. В конце сад вырубают, а бывшие хозяева разъезжаются. Чехов показывает столкновение уходящей красоты и нарождающегося практицизма. Пьеса наполнена тоской, недосказанностью, подтекстом. Фирс, старый слуга, забытый в заколоченном доме, произносит финальную фразу: «Жизнь-то прошла, словно и не жил».",
                        chekhov,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283586/visheniy-sad_c5rnzc.jpg",
                        Set.of(drama, comedy, tragedy), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(110), now.minusDays(110)),

                createBook(null, "На дне", 1902,
                        "Социально-философская драма о ночлежке для «бывших людей»: босяков, воров, проституток, ремесленников. Появляется странник Лука, который утешает обитателей ложью о лучшей жизни (бесплатной больнице для алкоголиков, праведной земле). Когда ложь раскрывается, герои терпят крушение: актер вешается, кражи становятся жестокими, надежды рушатся. Горький ставит вопрос: что лучше — правда или сострадательная ложь? Сам автор считал, что «на дне» людей держит не социальное положение, а отсутствие воли, веры и чести. Пьеса стала манифестом раннего Горького, бунтаря и гуманиста.",
                        gorky,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283614/na-dne_s5ftb5.jpg",
                        Set.of(drama, philosophy), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(120), now.minusDays(120)),

                createBook(null, "Мы", 1924,
                        "Роман-антиутопия, предвосхитивший «1984» Оруэлла и «О дивный новый мир» Хаксли. Действие в Едином Государстве после Великой Деспотии. Люди — «нумера» (например, Д‑503), живут в стеклянных домах, ходят строем, лишены личных чувств и привязанностей. Главный герой ведет дневник о своих сомнениях после встречи с женщиной I‑330, которая втягивает его в подпольное движение «Мефи». Кульминация — неудачное восстание и операция «фантазиотрония», уничтожающая воображение. Замятин создал мир, где математическая логика подменила живую душу. Роман запрещали в СССР, и он стал символом антитоталитарной литературы.",
                        zamyatin,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283636/my_jjlx4f.jpg",
                        Set.of(novel, fantasy, dystopia, philosophy), Set.of(grade11, levelBase, categoryRussian, readingExtra),
                        now.minusDays(130), now.minusDays(130)),

                createBook(null, "Челкаш", 1895,
                        "Ранний романтический рассказ о двух контрастных героях: Челкаше — старом воре и контрабандисте, свободолюбивом «босяке», и Гавриле — молодом крестьянине, который ищет легкого заработка. Ночью они совершают опасную перевозку груза через море. После успеха Челкаш получает крупную сумму. Гаврила, ослепленный деньгами, пытается убить Челкаша из жадности. Челкаш отдает почти все деньги, но презирает рабскую привязанность к богатству. Рассказ прославляет свободу, даже если она связана с преступлением, и обличает крестьянскую мелочную корысть.",
                        gorky,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283698/chelkash_d9oqaw.jpg",
                        Set.of(rasskaz, realism), Set.of(grade11, levelProfile, categoryRussian, readingMain),
                        now.minusDays(140), now.minusDays(140)),

                createBook(null, "Старик и море", 1952,
                        "Повесть, принесшая Хемингуэю Пулитцеровскую премию и повлиявшая на присуждение Нобелевской. Старый кубинский рыбак Сантьяго 84 дня не может поймать рыбу. Наконец он выходит в море и ловит огромного марлина. Однако во время возвращения акулы съедают добычу, оставляя только скелет. На первый взгляд — поражение, но старик доказывает: «Человека можно уничтожить, но его нельзя победить». Рассказ наполнен библейскими мотивами (Сантьяго как Христос, несущий крест), темой мужской стойкости («кодекс чести») и простого достоинства перед лицом природы.",
                        hemingway,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283750/starik-i-more_lb2uyw.jpg",
                        Set.of(story, realism), Set.of(grade11, levelProfile, categoryForeign, readingExtra),
                        now.minusDays(150), now.minusDays(150)),

                createBook(null, "Фро", 1936,
                        "Рассказ из цикла «Река Потудань» (или отдельная публикация) о юной девушке Фросе, чей муж-железнодорожник уехал на Дальний Восток. Она отказывается жить привычной жизнью, забрасывает дом, ходит на вокзал, ищет чуда. Отец-машинист пытается ее вразумить. В конце она смиряется и начинает трудиться, но сохраняет память о любви. Платонов исследует тему разлуки, верности и неистребимого стремления к счастью даже в самых суровых условиях. Язык рассказа странный и поэтичный, полный «платоновской» неловкости, что придает особую глубину.",
                        platonov,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283775/fro_uys7gr.jpg",
                        Set.of(rasskaz, philosophy, psychology), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(200), now.minusDays(200)),

                createBook(null, "На заре туманной юности", 1936,
                        "Рассказ о юной Ольге, которая после смерти родителей покидает родные места и становится студенткой в Москве. Она проходит через голод, унижения, но сохраняет тягу к знаниям и любовь к книгам. Центральный эпизод — экзамен по истории в университете, где она поражает профессора своей начитанностью и самостоятельным мышлением. Платонов показывает, как духовная сила и вера в образование побеждают социальные трудности. Рассказ светлый, почти автобиографичный, с верой в человека как в «маленькое солнце».",
                        platonov,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283841/na-zare-tumannoy-yunosti_knnspr.jpg",
                        Set.of(story, realism, psychology), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(21), now.minusDays(21)),

                createBook(null, "Тихий Дон", 1940,
                        "Роман-эпопея о донском казачестве в Первую мировую, революцию и гражданскую войну. Главный герой Григорий Мелехов — человек сильный, правдолюбивый, мечущийся между белыми и красными. Он любит замужнюю Аксинью, но подчиняется воле родителей, женившись на Наталье. Гражданская война разрушает его дом, убивает близких. В финале Григорий возвращается в родной хутор, потеряв почти всех. Шолохов создал живые картины быта казаков, батальные сцены и глубокий образ «человека на переломе истории». Роман удостоен Нобелевской премии.",
                        sholokhov,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283893/tihiy-don_ckrek2.jpg",
                        Set.of(novel, history, realism), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(300), now.minusDays(300)),

                createBook(null, "Утиная охота", 1970,
                        "Пьеса о «потерянном поколении» советских интеллигентов 1960–70‑х годов. Инженер Зилов получает заказное письмо с фотографией гроба и похоронным венком, подписанное «друзья». В воспоминаниях и разговорах с друзьями, любовницами и женой раскрывается его жизнь, полная лжи, алкоголя, случайных связей и нежелания отвечать за свои поступки. Единственное подлинное желание — уехать на утиную охоту, символ свободы и очищения. В финале Зилов пытается застрелиться, но ружье оказывается не заряженным. Пьеса — трагикомедия экзистенциального кризиса.",
                        vampilov,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283947/utinaya-ohota_ckdgic.jpg",
                        Set.of(drama, tragedy, comedy, psychology), Set.of(grade11, levelBase, categoryRussian, readingMain),
                        now.minusDays(400), now.minusDays(400)),

                createBook(null, "Человек в футляре", 1898,
                        "Рассказ, открывающий «маленькую трилогию» о пошлости и страхе перед жизнью. Учитель греческого языка Беликов — «человек в футляре»: даже в хорошую погоду он носит галоши и зонт, прячет вещи в чехлы, боится реальности, всегда повторяет «как бы чего не вышло». Он пытается подчинить своему страху весь город, доносит начальству. Знакомство с бойкой укротительницей Варенькой Коваленко на минуту пробуждает в нем надежду, но при виде ее брата, едущего на велосипеде, Беликов пугается и умирает от сердечного приступа. Смерть — его «футляр» окончательный. Чехов высмеивает трусость и консерватизм.",
                        chekhov,
                        "https://res.cloudinary.com/dvmseo9jp/image/upload/v1779283985/chelovek_v_futlyare_sazi30.jpg",
                        Set.of(rasskaz, satire, psychology), Set.of(grade10, levelBase, categoryRussian, readingMain),
                        now.minusDays(55), now.minusDays(55))
        );

        bookRepository.saveAll(books);

        // ----- Файлы книг -----
        // Маппинг файлов книг (название книги -> формат -> URL)
        Map<String, Map<BookFormat, String>> bookFilesMap = new HashMap<>();

        bookFilesMap.put("Война и мир", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779295509/russian-literature/books/ad0e3fdb-4cb9-4a87-8fc2-85b295751b53",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779275429/voina-i-mir_wrxdpn.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779275443/voina-i-mir_s9zw2y.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779275429/voina-i-mir_kjv42k.txt"
        ));
        bookFilesMap.put("Евгений Онегин", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779295812/russian-literature/books/0362c738-0f7b-46c5-824e-7fb9276f5ecf",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291353/eugene-onegin_m5ehwv.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291364/eugene-onegin_pjme9e.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291357/eugene-onegin_kkidek.txt"
        ));
        bookFilesMap.put("Преступление и наказание", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779295922/russian-literature/books/bc4af3b4-bf20-4005-91d6-cc856393b58e",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291510/prestyplenie-i-nakazanie_kg1yto.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291511/prestyplenie-i-nakazanie_b6rit7.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291503/prestyplenie-i-nakazanie_hjfywb.txt"
        ));
        bookFilesMap.put("Герой нашего времени", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779295975/russian-literature/books/acdc96c7-c3ba-4ea0-8038-a82f3f01addc",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291549/geroj-nashego-vremeni_kr9d84.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291553/geroj-nashego-vremeni_oiplqf.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291558/geroj-nashego-vremeni_lwdoh8.txt"
        ));
        bookFilesMap.put("Мертвые души", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296040/russian-literature/books/34efc224-33b7-4633-a874-27ed1fb209cd",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291594/mertvie-dushi_u3vhtd.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291596/mertvie-dushi_asied9.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291603/mertvie-dushi_eguhwu.txt"
        ));
        bookFilesMap.put("Отцы и дети", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296326/russian-literature/books/c45e0dfd-b232-47a7-991d-3cfa68ff2ce3",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291628/otcy-i-deti_mkrgmt.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291596/mertvie-dushi_asied9.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291632/otcy-i-deti_pojtqw.txt"
        ));
        bookFilesMap.put("Горе от ума", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296378/russian-literature/books/72176740-f939-4d8b-9ebf-da5a5ad40d97",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291662/gore-ot-uma_vfyu8u.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291659/gore-ot-uma_lblcti.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291666/gore-ot-uma_vzcfx5.txt"
        ));
        bookFilesMap.put("Мастер и Маргарита", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296423/russian-literature/books/9155f9b9-0fb6-4a04-8493-6be993e9fbdf",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291698/master-i-margarita_yz0m3t.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291707/master-i-margarita_cv3u0r.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291713/master-i-margarita_wapnec.txt"
        ));
        bookFilesMap.put("Анна Каренина", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296520/russian-literature/books/85c2f137-9355-4545-8acb-6d5c9e003422",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291743/anna-karenina_nzsflg.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291768/anna-karenina_c7akuv.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291799/anna-karenina_qcwbda.txt"
        ));
        bookFilesMap.put("Вишневый сад", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296567/russian-literature/books/c3799935-1bfa-4f9f-abb5-6fe683e5dc33",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291833/vishneviy-sad_nvjo5q.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291872/vishneviy-sad_nrta1v.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291837/vishneviy-sad_sk4rdu.txt"
        ));
        bookFilesMap.put("На дне", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296608/russian-literature/books/bbab3780-f57e-4000-af05-0fccda1156b8",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291899/na-dne_ycufbp.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291926/na-dne_yvgnry.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291900/na-dne_fcp3ft.txt"
        ));
        bookFilesMap.put("Мы", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296665/russian-literature/books/f5dba0a6-84f7-4340-9a4c-e2175dcfcfd7",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291971/my_hrkzlz.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291997/my_lir38v.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779291954/my_gbhjsd.txt"
        ));
        bookFilesMap.put("Челкаш", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296710/russian-literature/books/04b6fea9-e21a-4d18-bc38-0146ce6cc93a",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292013/chelkash_bu4kf7.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292020/chelkash_aao10a.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292016/chelkash_ke0ent.txt"
        ));
        bookFilesMap.put("Старик и море", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296761/russian-literature/books/d8c68104-b2d4-4ad9-ab10-a1a95ea67ada",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292051/starik-i-more_koacpa.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292054/starik-i-more_zicm85.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292057/starik-i-more_t4ehom.txt"
        ));
        bookFilesMap.put("Фро", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296800/russian-literature/books/b3b7be7f-7a5a-47e9-87ef-1b5493507db9",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292067/fro_ccilwg.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292072/fro_ggjakv.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292076/fro_rxwypu.txt"
        ));
        bookFilesMap.put("На заре туманной юности", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296849/russian-literature/books/8e8e84f6-1e77-466b-b7e0-544d32e61a6c",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292116/na-zare-tumannoy-yunosti_gxtvl8.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292120/na-zare-tumannoy-yunosti_bsxscg.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292122/na-zare-tumannoy-yunosti_gatjsi.txt"
        ));
        bookFilesMap.put("Тихий Дон", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296913/russian-literature/books/8f48434b-e585-4c99-b588-58fd124d80fa",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292134/tihiy-don_hzgs3e.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292163/tihiy-don_brx2tx.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292148/tihiy-don_rstouy.txt"
        ));
        bookFilesMap.put("Утиная охота", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779296974/russian-literature/books/52c48a4c-0778-49f8-82e5-b31596ae1533",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292190/utinaya-ohota_gionp5.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292194/utinaya-ohota_jtovcq.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292196/utinaya-ohota_c8johp.txt"
        ));
        bookFilesMap.put("Человек в футляре", Map.of(
                BookFormat.PDF, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779297012/russian-literature/books/36394c69-d59e-4f7b-965d-b4d4038a3d67",
                BookFormat.EPUB, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292203/chelovek-v-futlyare_j5kvvk.epub",
                BookFormat.FB2, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292213/chelovek-v-futlyare_dqgkvd.fb2",
                BookFormat.TXT, "https://res.cloudinary.com/dvmseo9jp/raw/upload/v1779292212/chelovek-v-futlyare_z7x3ni.txt"
        ));

        // Создаем записи BookFileEntity
        List<BookFileEntity> bookFiles = new ArrayList<>();
        for (BookEntity book : books) {
            Map<BookFormat, String> formats = bookFilesMap.get(book.getTitle());
            if (formats == null) {
                // Если вдруг книга не найдена в маппинге — создаем пустые записи
                formats = Map.of(BookFormat.PDF, "", BookFormat.EPUB, "", BookFormat.FB2, "", BookFormat.TXT, "");
            }
            for (Map.Entry<BookFormat, String> entry : formats.entrySet()) {
                BookFileEntity file = new BookFileEntity();
                file.setBook(book);
                file.setFormat(entry.getKey());
                file.setFileUrl(entry.getValue());
                file.setPublicId(extractPublicId(entry.getValue()));
                file.setCreatedAt(now);
                bookFiles.add(file);
            }
        }
        bookFileRepository.saveAll(bookFiles);

        System.out.println("✅ Тестовые данные загружены успешно!");
        System.out.println("📚 Авторов: " + authors.size());
        System.out.println("📖 Жанров: " + genres.size());
        System.out.println("🏷️ Тегов: " + tags.size());
        System.out.println("📕 Книг: " + books.size());
        System.out.println("📁 Добавлено файлов книг: " + bookFiles.size() + " (по 4 на книгу)");
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

    private String extractPublicId(String url) {
        if (url == null || url.isEmpty()) return null;
        String[] parts = url.split("/");
        String lastPart = parts[parts.length - 1]; // "voina-i-mir_s9zw2y.pdf"
        int dotIndex = lastPart.lastIndexOf('.');
        return (dotIndex > 0) ? lastPart.substring(0, dotIndex) : lastPart;
    }
}
