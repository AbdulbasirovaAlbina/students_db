# Быстрый старт

## Минимальные шаги для запуска

### 1. Создание базы данных

```sql
-- В pgAdmin или psql выполните:
CREATE DATABASE students_db;

-- Затем выполните скрипты:
-- database/create_database.sql
-- database/insert_sample_data.sql
```

### 2. Настройка подключения

Откройте `src/DatabaseConnection.java` и измените пароль:
```java
private static final String DB_PASSWORD = "ваш_пароль";
```

### 3. Добавление JDBC драйвера

Скачайте: https://jdbc.postgresql.org/download/

**IntelliJ IDEA:**
- File → Project Structure → Libraries → + → выберите JAR

**Eclipse:**
- Properties → Java Build Path → Add External JARs

### 4. Запуск

Запустите `Main.java` из вашей IDE.

## Проверка работы

После запуска вы должны увидеть:
- Окно с таблицей студентов
- Данные загружены из БД
- Кнопки работают (Добавить, Редактировать, Удалить)

## Основные функции

1. **Просмотр студентов** - автоматическая загрузка из БД
2. **Поиск** - по ФИО или номеру зачётной книжки
3. **Добавление/Редактирование** - сохранение в БД
4. **Оценки и посещаемость** - просмотр и добавление записей
5. **Справка** - экспорт данных студента в текстовый файл

## Структура БД

- **8 таблиц**: faculties, specialties, groups, teachers, disciplines, students, grades, attendance
- **Связи**: через внешние ключи с каскадным удалением
- **Индексы**: для ускорения поиска
- **Ограничения**: проверка целостности данных

## Дополнительная информация

- Подробная инструкция: `INSTALLATION.md`
- Описание БД: `database/DATABASE_SCHEMA.md`
- SQL скрипты: `database/`

