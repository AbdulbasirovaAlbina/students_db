# Инструкция по установке и настройке

## Шаг 1: Установка PostgreSQL JDBC драйвера

### Для IntelliJ IDEA:

1. Скачайте PostgreSQL JDBC драйвер:
   - Перейдите на https://jdbc.postgresql.org/download/
   - Скачайте последнюю версию (например, `postgresql-42.7.1.jar`)

2. Добавьте драйвер в проект:
   - File → Project Structure (Ctrl+Alt+Shift+S)
   - Libraries → + → Java
   - Выберите скачанный JAR файл
   - Нажмите OK

3. Добавьте библиотеку в модуль:
   - Modules → students → Dependencies
   - Убедитесь, что библиотека PostgreSQL JDBC добавлена

### Для Eclipse:

1. Скачайте PostgreSQL JDBC драйвер (см. выше)

2. Добавьте драйвер в проект:
   - Правой кнопкой на проекте → Properties
   - Java Build Path → Libraries → Add External JARs
   - Выберите скачанный JAR файл
   - Нажмите OK

### Для командной строки:

1. Скачайте PostgreSQL JDBC драйвер (см. выше)

2. Поместите JAR файл в папку `lib/` в корне проекта

3. Компиляция:
   ```bash
   javac -cp "lib/postgresql-42.7.1.jar" src/*.java -d out
   ```

4. Запуск:
   ```bash
   java -cp "out:lib/postgresql-42.7.1.jar" Main
   ```

## Шаг 2: Настройка базы данных

### Вариант A: Через pgAdmin (рекомендуется)

1. Запустите pgAdmin
2. Подключитесь к серверу PostgreSQL
3. Создайте новую базу данных:
   - Правой кнопкой на "Databases" → Create → Database
   - Имя: `students_db`
   - Owner: `postgres`
   - Нажмите Save

4. Выполните скрипты:
   - Выберите базу данных `students_db`
   - Tools → Query Tool
   - Откройте файл `database/create_database.sql`
   - Выполните скрипт (F5)
   - Откройте файл `database/insert_sample_data.sql`
   - Выполните скрипт (F5)

### Вариант B: Через psql (командная строка)

```bash
# Подключение к PostgreSQL
psql -U postgres

# Создание БД (в psql)
CREATE DATABASE students_db;

# Выход из psql
\q

# Выполнение скриптов
psql -U postgres -d students_db -f database/create_database.sql
psql -U postgres -d students_db -f database/insert_sample_data.sql
```

## Шаг 3: Настройка подключения

Откройте файл `src/DatabaseConnection.java` и при необходимости измените:

```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/students_db";
private static final String DB_USER = "postgres";
private static final String DB_PASSWORD = "ваш_пароль"; // Измените на ваш пароль
```

## Шаг 4: Запуск приложения

### IntelliJ IDEA:
- Правой кнопкой на `Main.java` → Run 'Main.main()'

### Eclipse:
- Правой кнопкой на `Main.java` → Run As → Java Application

### Командная строка:
```bash
java -cp "out:lib/postgresql-42.7.1.jar" Main
```

## Проверка работы

После запуска приложения вы должны увидеть окно с таблицей студентов. Если данные загрузились из БД, значит всё настроено правильно!

## Устранение проблем

### Ошибка: "PostgreSQL JDBC Driver не найден"
- Убедитесь, что JDBC драйвер добавлен в classpath проекта

### Ошибка: "Connection refused"
- Проверьте, что PostgreSQL запущен
- Проверьте правильность URL подключения в `DatabaseConnection.java`

### Ошибка: "password authentication failed"
- Проверьте правильность пароля в `DatabaseConnection.java`
- Убедитесь, что пользователь `postgres` существует

### Ошибка: "database does not exist"
- Убедитесь, что база данных `students_db` создана
- Выполните скрипт `create_database.sql`

