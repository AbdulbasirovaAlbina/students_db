# SQL Скрипты для базы данных

## Порядок выполнения

1. **create_database.sql** - Создание базы данных и всех таблиц
2. **insert_sample_data.sql** - Заполнение тестовыми данными

## Выполнение через psql

```bash
# Создание БД
psql -U postgres -f create_database.sql

# Заполнение данными
psql -U postgres -d students_db -f insert_sample_data.sql
```

## Выполнение через pgAdmin

1. Откройте pgAdmin
2. Подключитесь к серверу PostgreSQL
3. Создайте новую базу данных `students_db` (или выполните скрипт create_database.sql)
4. Выберите базу данных `students_db`
5. Откройте Query Tool (Tools → Query Tool)
6. Выполните скрипт `create_database.sql` (если БД еще не создана)
7. Выполните скрипт `insert_sample_data.sql`

## Проверка данных

После выполнения скриптов можно проверить данные:

```sql
-- Количество записей в каждой таблице
SELECT 'Факультетов' as таблица, COUNT(*) as количество FROM faculties
UNION ALL
SELECT 'Специальностей', COUNT(*) FROM specialties
UNION ALL
SELECT 'Групп', COUNT(*) FROM groups
UNION ALL
SELECT 'Преподавателей', COUNT(*) FROM teachers
UNION ALL
SELECT 'Дисциплин', COUNT(*) FROM disciplines
UNION ALL
SELECT 'Студентов', COUNT(*) FROM students
UNION ALL
SELECT 'Оценок', COUNT(*) FROM grades
UNION ALL
SELECT 'Записей посещаемости', COUNT(*) FROM attendance;
```

