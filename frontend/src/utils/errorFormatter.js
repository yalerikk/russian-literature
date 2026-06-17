export function formatErrorMessage(error, context = "default") {
  const rawMessage = error?.details || error?.message || String(error) || "";
  const trimmed = rawMessage.trim();

  // Маппинг общих фраз
  const commonMappings = {
    "already exists": "такая запись уже существует",
    "not found": "запись не найдена",
    "access denied": "доступ запрещён",
    "unauthorized": "необходимо войти в систему",
    "internal server error": "внутренняя ошибка сервера, попробуйте позже",
  };

  // ===== АВТОРЫ =====
  const authorMappings = {
    'first name must not exceed 50 characters': 'Имя не должно превышать 50 символов',
    'last name must not exceed 50 characters': 'Фамилия не должна превышать 50 символов',
    'middle name must not exceed 50 characters': 'Отчество не должно превышать 50 символов',
    'an author with the same full name already exists': 'Автор с таким ФИО уже существует',
    'biography must contain at least 10 characters': 'Биография должна содержать не менее 10 символов',
    'biography must not exceed 2000 characters': 'Биография не должна превышать 2000 символов',
    'birth date is required': 'Дата рождения обязательна',
    'birth date cannot be in the future': 'Дата рождения не может быть в будущем',
    'death date cannot be earlier than the birth date': 'Дата смерти не может быть раньше даты рождения',
    'death date cannot be in the future': 'Дата смерти не может быть в будущем',
    'death date cannot be the same as the birth date': 'Дата смерти не может совпадать с датой рождения',
    'death date is unrealistic — the author could not die at this age': 'Нереалистичная дата смерти – проверьте возраст',
    'unable to delete author': 'Нельзя удалить автора, у которого есть книги. Сначала удалите или переназначьте книги.',
  };

  // ===== КНИГИ =====
  const bookMappings = {
    'title is required': 'Название обязательно',
    'title must not exceed 255 characters': 'Название не должно превышать 255 символов',
    'description is required': 'Описание обязательно',
    'description must not exceed 1000 characters': 'Описание не должно превышать 1000 символов',
    'book with title .* already exists for this author': 'Книга с таким названием уже есть у этого автора',
    'publication year cannot be earlier than 1500': 'Год издания не может быть раньше 1500',
    'publication year cannot be in the future': 'Год издания не может быть в будущем',
    'book must have at least one genre': 'Выберите хотя бы один жанр',
    'book must have at least one tag': 'Выберите хотя бы один тег',
    'author with id = .* does not exist': 'Указанный автор не найден',
    'genre with id = .* does not exist': 'Указанный жанр не существует',
    'tag with id = .* does not exist': 'Указанный тег не существует',
    'book must have a grade tag': 'Книга должна иметь тег класса (10 или 11 класс)',
    'book must have a level tag': 'Книга должна иметь тег уровня (База или Профиль)',
    'book must have a category tag': 'Книга должна иметь тег категории (Русская или Иностранная литература)',
    'book can have only one reading type tag': 'Книга может иметь только один тег типа чтения',
    'book can belong to only one grade': 'Книга может принадлежать только одному классу',
    'book can have only one level': 'Книга может иметь только один уровень',
    'book can belong to only one category': 'Книга может принадлежать только одной категории',
    'file of format .* already exists for this book': 'Файл этого формата уже загружен для книги',
    'You must upload EPUB file first': 'Сначала загрузите EPUB-файл',
  };

  // ===== КАТЕГОРИИ =====
  const categoryMappings = {
    'category with code .* already exists': 'Категория с таким кодом уже существует',
    'category with name .* already exists': 'Категория с таким названием уже существует',
    'a custom category must have at least one tag': 'Выберите хотя бы один тег для категории',
    'some tags were not found': 'Некоторые теги не найдены',
    'a category cannot have two tags of the same type': 'Категория не может иметь два тега одного типа',
    'category name must be between 2 and 100 characters': 'Название категории должно быть от 2 до 100 символов',
    'base categories cannot be edited': 'Базовые категории нельзя редактировать',
    'cannot delete a base category': 'Базовые категории нельзя удалить',
  };

  // ===== ЖАНРЫ =====
  const genreMappings = {
    'genre name must not exceed 50 characters': 'Название жанра не должно превышать 50 символов',
    'genre with name .* already exists': 'Жанр с названием "{name}" уже существует',
  };

  // ===== ТЕГИ =====
  const tagMappings = {
    'tag name must not exceed 50 characters': 'Название тега не должно превышать 50 символов',
    'tag type is required': 'Тип тега обязателен',
    'tag with name .* already exists': 'Тег с названием "{name}" уже существует',
  };

  // ===== ПОЛЬЗОВАТЕЛИ =====
  const userMappings = {
    // Username
    'user with username .* already exists': 'Пользователь с логином "{username}" уже существует',
    'user with email .* already exists': 'Пользователь с email "{email}" уже существует',
    'invalid email format': 'Неверный формат email',
    // Password
    'password must be at least 6 characters long': 'Пароль должен быть не менее 6 символов',
    'password must not contain cyrillic characters': 'Пароль не должен содержать кириллицу',
    'password must contain at least one uppercase letter': 'Пароль должен содержать хотя бы одну заглавную букву (A-Z)',
    'password must contain at least one lowercase letter': 'Пароль должен содержать хотя бы одну строчную букву (a-z)',
    'password must contain at least one digit': 'Пароль должен содержать хотя бы одну цифру',
    // Общие
    'you can only edit your own profile': 'Вы можете редактировать только свой профиль',
    'you can only delete your own account': 'Вы можете удалить только свой аккаунт',
    'cannot delete the last admin user': 'Нельзя удалить последнего администратора',
    'cannot demote the last admin user': 'Нельзя понизить последнего администратора',
    'only an admin can delete another admin': 'Только администратор может удалить другого администратора',
  };

  // ===== АВТОРИЗАЦИЯ =====
  const authMappings = {
    'user not found': 'Пользователь не найден',
    'invalid password': 'Неверный пароль',
    'invalid login or password': 'Неверный логин или пароль',
    'username .* already exists': 'Пользователь с логином "{username}" уже существует',
    'email .* already exists': 'Пользователь с email "{email}" уже существует',
  };

  const contextMappings = {
    author: authorMappings,
    book: bookMappings,
    category: categoryMappings,
    genre: genreMappings,
    tag: tagMappings,
    user: userMappings,
    auth: authMappings,
  };

  const allMappings = {
    ...commonMappings,
    ...(contextMappings[context] || {}),
  };

  const lowerMsg = trimmed.toLowerCase();

  for (const [key, value] of Object.entries(allMappings)) {
    if (key.includes(".*")) {
      try {
        const regex = new RegExp(key, "i");
        if (regex.test(trimmed)) {
          // Если значение содержит {placeholder}, пытаемся подставить
          if (value.includes("{") && value.includes("}")) {
            const match = trimmed.match(regex);
            if (match && match[1]) {
              return value.replace(/\{(username|email|name|code)\}/, match[1]);
            }
          }
          return value;
        }
      } catch (e) {}
    } else {
        if (lowerMsg.includes(key.toLowerCase())) {
          return value;
      }
    }
  }

  if (/[а-яА-Я]/.test(trimmed) && !trimmed.includes('id=') && !trimmed.includes('=')) {
    return trimmed;
  }
  
  return trimmed;
}
