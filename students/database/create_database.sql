-- Создание базы данных для системы учета студентов
-- Лабораторная работа №4

-- Удаление БД если существует (осторожно!)
-- DROP DATABASE IF EXISTS students_db;

-- Создание БД
CREATE DATABASE students_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Подключение к БД
-- ВНИМАНИЕ: Команда \c работает только в psql!
-- Если используете pgAdmin, выберите базу данных students_db вручную перед выполнением остальных команд
\c students_db;

-- Создание таблиц

-- 1. Факультеты
CREATE TABLE IF NOT EXISTS faculties (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    short_name VARCHAR(100) NOT NULL,
    dean_fio VARCHAR(255)
);

-- 2. Специальности
CREATE TABLE IF NOT EXISTS specialties (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    qualification VARCHAR(255),
    form_of_study VARCHAR(50),
    duration_years INTEGER,
    faculty_id INTEGER REFERENCES faculties(id) ON DELETE SET NULL
);

-- 3. Группы
CREATE TABLE IF NOT EXISTS groups (
    id SERIAL PRIMARY KEY,
    number VARCHAR(20) UNIQUE NOT NULL,
    course INTEGER NOT NULL,
    year_of_admission INTEGER NOT NULL,
    graduation_year INTEGER,
    curator_fio VARCHAR(255),
    specialty_id INTEGER REFERENCES specialties(id) ON DELETE SET NULL
);

-- 4. Преподаватели
CREATE TABLE IF NOT EXISTS teachers (
    id SERIAL PRIMARY KEY,
    fio VARCHAR(255) NOT NULL,
    birth_date DATE,
    gender VARCHAR(10),
    phone VARCHAR(20),
    email VARCHAR(100),
    tab_number VARCHAR(50) UNIQUE,
    position VARCHAR(100),
    academic_degree VARCHAR(100),
    department VARCHAR(255)
);

-- 5. Дисциплины
CREATE TABLE IF NOT EXISTS disciplines (
    id SERIAL PRIMARY KEY,
    index_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    total_hours INTEGER NOT NULL,
    lecture_hours INTEGER DEFAULT 0,
    practice_hours INTEGER DEFAULT 0,
    lab_hours INTEGER DEFAULT 0,
    control_type VARCHAR(50) NOT NULL,
    semester INTEGER NOT NULL,
    specialty_id INTEGER REFERENCES specialties(id) ON DELETE CASCADE
);

-- 6. Студенты (наследуют от Person)
CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY,
    fio VARCHAR(255) NOT NULL,
    birth_date DATE,
    gender VARCHAR(10),
    phone VARCHAR(20),
    email VARCHAR(100),
    record_book_number VARCHAR(50) UNIQUE NOT NULL,
    student_id_number VARCHAR(50),
    enrollment_order_date DATE,
    enrollment_order_number VARCHAR(50),
    status VARCHAR(50) DEFAULT 'обучается',
    is_budget BOOLEAN DEFAULT TRUE,
    group_id INTEGER REFERENCES groups(id) ON DELETE SET NULL
);

-- 7. Оценки
CREATE TABLE IF NOT EXISTS grades (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    discipline_id INTEGER NOT NULL REFERENCES disciplines(id) ON DELETE CASCADE,
    value INTEGER NOT NULL CHECK (value >= 2 AND value <= 5),
    in_words VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    attempt_number INTEGER DEFAULT 1,
    teacher_fio VARCHAR(255),
    is_debt BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 8. Посещаемость
CREATE TABLE IF NOT EXISTS attendance (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    discipline_id INTEGER NOT NULL REFERENCES disciplines(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    lesson_number INTEGER NOT NULL,
    present BOOLEAN NOT NULL DEFAULT TRUE,
    reason_if_absent VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание индексов для ускорения поиска
CREATE INDEX IF NOT EXISTS idx_students_group ON students(group_id);
CREATE INDEX IF NOT EXISTS idx_students_record_book ON students(record_book_number);
CREATE INDEX IF NOT EXISTS idx_grades_student ON grades(student_id);
CREATE INDEX IF NOT EXISTS idx_grades_discipline ON grades(discipline_id);
CREATE INDEX IF NOT EXISTS idx_attendance_student ON attendance(student_id);
CREATE INDEX IF NOT EXISTS idx_attendance_discipline ON attendance(discipline_id);
CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendance(date);

-- Комментарии к таблицам
COMMENT ON TABLE faculties IS 'Факультеты университета';
COMMENT ON TABLE specialties IS 'Специальности';
COMMENT ON TABLE groups IS 'Учебные группы';
COMMENT ON TABLE teachers IS 'Преподаватели';
COMMENT ON TABLE disciplines IS 'Учебные дисциплины';
COMMENT ON TABLE students IS 'Студенты';
COMMENT ON TABLE grades IS 'Оценки студентов';
COMMENT ON TABLE attendance IS 'Посещаемость занятий';

