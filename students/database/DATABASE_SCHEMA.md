# Описание схемы базы данных

## Инфологическая модель

Предметная область: система учета студентов, их успеваемости и посещаемости в учебном заведении.

### Основные сущности:
- **Факультет** - структурное подразделение университета
- **Специальность** - направление подготовки, принадлежит факультету
- **Группа** - учебная группа студентов, принадлежит специальности
- **Студент** - обучающийся, принадлежит группе
- **Преподаватель** - сотрудник, ведущий занятия
- **Дисциплина** - учебный предмет, принадлежит специальности
- **Оценка** - результат аттестации студента по дисциплине
- **Посещаемость** - запись о присутствии/отсутствии студента на занятии

## Логическая модель (реляционная)

### Нормализация

Все таблицы нормализованы до **3NF (третьей нормальной формы)**:

1. **1NF** - все атрибуты атомарны, нет повторяющихся групп
2. **2NF** - нет частичных зависимостей от составного ключа
3. **3NF** - нет транзитивных зависимостей

### Таблицы и связи

#### 1. faculties (Факультеты)
- `id` (PK, SERIAL) - первичный ключ
- `code` (VARCHAR, UNIQUE) - код факультета
- `full_name` (VARCHAR) - полное название
- `short_name` (VARCHAR) - краткое название
- `dean_fio` (VARCHAR) - ФИО декана

#### 2. specialties (Специальности)
- `id` (PK, SERIAL)
- `code` (VARCHAR, UNIQUE) - код специальности
- `name` (VARCHAR) - название
- `qualification` (VARCHAR) - квалификация
- `form_of_study` (VARCHAR) - форма обучения
- `duration_years` (INTEGER) - длительность обучения
- `faculty_id` (FK → faculties.id) - связь с факультетом

#### 3. groups (Группы)
- `id` (PK, SERIAL)
- `number` (VARCHAR, UNIQUE) - номер группы
- `course` (INTEGER) - курс
- `year_of_admission` (INTEGER) - год поступления
- `graduation_year` (INTEGER) - год выпуска
- `curator_fio` (VARCHAR) - ФИО куратора
- `specialty_id` (FK → specialties.id) - связь со специальностью

#### 4. teachers (Преподаватели)
- `id` (PK, SERIAL)
- `fio` (VARCHAR) - ФИО
- `birth_date` (DATE) - дата рождения
- `gender` (VARCHAR) - пол
- `phone` (VARCHAR) - телефон
- `email` (VARCHAR) - email
- `tab_number` (VARCHAR, UNIQUE) - табельный номер
- `position` (VARCHAR) - должность
- `academic_degree` (VARCHAR) - ученая степень
- `department` (VARCHAR) - кафедра

#### 5. disciplines (Дисциплины)
- `id` (PK, SERIAL)
- `index_code` (VARCHAR, UNIQUE) - индекс дисциплины
- `name` (VARCHAR) - название
- `total_hours` (INTEGER) - общее количество часов
- `lecture_hours` (INTEGER) - лекционные часы
- `practice_hours` (INTEGER) - практические часы
- `lab_hours` (INTEGER) - лабораторные часы
- `control_type` (VARCHAR) - тип контроля (экзамен/зачёт)
- `semester` (INTEGER) - семестр
- `specialty_id` (FK → specialties.id) - связь со специальностью

#### 6. students (Студенты)
- `id` (PK, SERIAL)
- `fio` (VARCHAR) - ФИО
- `birth_date` (DATE) - дата рождения
- `gender` (VARCHAR) - пол
- `phone` (VARCHAR) - телефон
- `email` (VARCHAR) - email
- `record_book_number` (VARCHAR, UNIQUE) - номер зачётной книжки
- `student_id_number` (VARCHAR) - номер студенческого билета
- `enrollment_order_date` (DATE) - дата приказа о зачислении
- `enrollment_order_number` (VARCHAR) - номер приказа о зачислении
- `status` (VARCHAR) - статус (обучается, отчислен и т.д.)
- `is_budget` (BOOLEAN) - бюджетная/контрактная форма
- `group_id` (FK → groups.id) - связь с группой

#### 7. grades (Оценки)
- `id` (PK, SERIAL)
- `student_id` (FK → students.id, CASCADE DELETE) - связь со студентом
- `discipline_id` (FK → disciplines.id, CASCADE DELETE) - связь с дисциплиной
- `value` (INTEGER, CHECK 2-5) - оценка (2-5)
- `in_words` (VARCHAR) - оценка прописью
- `date` (DATE) - дата получения оценки
- `attempt_number` (INTEGER) - номер попытки
- `teacher_fio` (VARCHAR) - ФИО преподавателя
- `is_debt` (BOOLEAN) - наличие долга
- `created_at` (TIMESTAMP) - время создания записи

#### 8. attendance (Посещаемость)
- `id` (PK, SERIAL)
- `student_id` (FK → students.id, CASCADE DELETE) - связь со студентом
- `discipline_id` (FK → disciplines.id, CASCADE DELETE) - связь с дисциплиной
- `date` (DATE) - дата занятия
- `lesson_number` (INTEGER) - номер пары
- `present` (BOOLEAN) - присутствовал/отсутствовал
- `reason_if_absent` (VARCHAR) - причина отсутствия
- `created_at` (TIMESTAMP) - время создания записи

## Физическая модель

### Индексы

Созданы индексы для ускорения поиска:

- `idx_students_group` - по `students.group_id`
- `idx_students_record_book` - по `students.record_book_number`
- `idx_grades_student` - по `grades.student_id`
- `idx_grades_discipline` - по `grades.discipline_id`
- `idx_attendance_student` - по `attendance.student_id`
- `idx_attendance_discipline` - по `attendance.discipline_id`
- `idx_attendance_date` - по `attendance.date`

### Ограничения целостности

1. **Первичные ключи (PRIMARY KEY)** - обеспечивают уникальность записей
2. **Внешние ключи (FOREIGN KEY)** - обеспечивают ссылочную целостность
3. **UNIQUE** - обеспечивают уникальность значений (коды, номера)
4. **CHECK** - проверка значений (оценки от 2 до 5)
5. **NOT NULL** - обязательные поля
6. **CASCADE DELETE** - каскадное удаление связанных записей

### Типы данных

- **SERIAL** - автоинкремент для первичных ключей
- **VARCHAR** - строки переменной длины
- **INTEGER** - целые числа
- **BOOLEAN** - логические значения
- **DATE** - даты
- **TIMESTAMP** - дата и время

## Диаграмма связей

```
faculties (1) ──────< (N) specialties (1) ──────< (N) groups (1) ──────< (N) students
                                                                              │
                                                                              │ 1:N
                                                                              │
                                                                        ┌─────▼──────┐
                                                                        │   grades   │
                                                                        └─────┬──────┘
                                                                              │ N:1
                                                                              │
                                                                        ┌─────▼──────────┐
                                                                        │  disciplines   │
                                                                        └─────┬──────────┘
                                                                              │ N:1
                                                                        ┌─────▼──────────┐
                                                                        │  attendance   │
                                                                        └────────────────┘
```

## Операции с данными

### Поддерживаемые операции:

1. **CRUD операции** для всех сущностей:
   - Create (INSERT)
   - Read (SELECT)
   - Update (UPDATE)
   - Delete (DELETE)

2. **Поиск и фильтрация**:
   - Поиск студентов по ФИО или номеру зачётной книжки
   - Фильтрация по группе, факультету, статусу

3. **Агрегация**:
   - Подсчет оценок по студенту
   - Подсчет посещаемости
   - Статистика по группам

4. **Целостность данных**:
   - Автоматическая проверка ограничений
   - Каскадное удаление связанных записей
   - Защита от дублирования (UNIQUE)

