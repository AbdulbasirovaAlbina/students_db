-- Заполнение базы данных тестовыми данными
-- Лабораторная работа №4

\c students_db;

-- Очистка данных (осторожно!)
-- TRUNCATE TABLE attendance, grades, students, disciplines, groups, specialties, faculties, teachers CASCADE;

-- 1. Вставка факультетов
INSERT INTO faculties (code, full_name, short_name, dean_fio) VALUES
('ФИТиУ', 'Факультет информационных технологий и управления', 'ФИТиУ', 'Иванов Иван Иванович'),
('ЭК', 'Экономический факультет', 'Экономический', 'Петрова Анна Сергеевна'),
('ЮР', 'Юридический факультет', 'Юридический', 'Сидоров Пётр Алексеевич'),
('ФИЗ', 'Физический факультет', 'Физический', 'Козлова Мария Дмитриевна')
ON CONFLICT (code) DO NOTHING;

-- 2. Вставка специальностей
INSERT INTO specialties (code, name, qualification, form_of_study, duration_years, faculty_id) VALUES
('09.03.01', 'Информатика и вычислительная техника', 'Бакалавр', 'Очная', 4, (SELECT id FROM faculties WHERE code = 'ФИТиУ')),
('09.03.02', 'Информационные системы и технологии', 'Бакалавр', 'Очная', 4, (SELECT id FROM faculties WHERE code = 'ФИТиУ')),
('38.03.01', 'Экономика', 'Бакалавр', 'Очная', 4, (SELECT id FROM faculties WHERE code = 'ЭК')),
('40.03.01', 'Юриспруденция', 'Бакалавр', 'Очная', 4, (SELECT id FROM faculties WHERE code = 'ЮР')),
('03.03.01', 'Физика', 'Бакалавр', 'Очная', 4, (SELECT id FROM faculties WHERE code = 'ФИЗ'))
ON CONFLICT (code) DO NOTHING;

-- 3. Вставка групп
INSERT INTO groups (number, course, year_of_admission, graduation_year, curator_fio, specialty_id) VALUES
('ИТ-31', 3, 2022, 2026, 'Смирнов Алексей Викторович', (SELECT id FROM specialties WHERE code = '09.03.01')),
('ИТ-32', 3, 2022, 2026, 'Кузнецова Елена Петровна', (SELECT id FROM specialties WHERE code = '09.03.01')),
('ИС-21', 2, 2023, 2027, 'Волков Дмитрий Сергеевич', (SELECT id FROM specialties WHERE code = '09.03.02')),
('ЭК-21', 2, 2023, 2027, 'Новикова Ольга Ивановна', (SELECT id FROM specialties WHERE code = '38.03.01')),
('ЮР-11', 1, 2024, 2028, 'Лебедев Сергей Александрович', (SELECT id FROM specialties WHERE code = '40.03.01')),
('ФИЗ-41', 4, 2021, 2025, 'Морозова Татьяна Владимировна', (SELECT id FROM specialties WHERE code = '03.03.01'))
ON CONFLICT (number) DO NOTHING;

-- 4. Вставка преподавателей
INSERT INTO teachers (fio, birth_date, gender, phone, email, tab_number, position, academic_degree, department) VALUES
('Иванов Пётр Петрович', '1975-05-15', 'м', '+79991234567', 'ivanov.pp@university.ru', 'Т001', 'Доцент', 'Кандидат технических наук', 'Кафедра информатики'),
('Петров Сергей Сергеевич', '1980-08-22', 'м', '+79997654321', 'petrov.ss@university.ru', 'Т002', 'Старший преподаватель', NULL, 'Кафедра программирования'),
('Смирнова Анна Александровна', '1985-11-10', 'ж', '+79995556677', 'smirnova.aa@university.ru', 'Т003', 'Доцент', 'Кандидат физико-математических наук', 'Кафедра физики'),
('Кузнецов Дмитрий Иванович', '1978-03-18', 'м', '+79993332211', 'kuznetsov.di@university.ru', 'Т004', 'Профессор', 'Доктор экономических наук', 'Кафедра экономики'),
('Волкова Елена Сергеевна', '1982-07-25', 'ж', '+79994445566', 'volkova.es@university.ru', 'Т005', 'Доцент', 'Кандидат юридических наук', 'Кафедра права')
ON CONFLICT (tab_number) DO NOTHING;

-- 5. Вставка дисциплин
INSERT INTO disciplines (index_code, name, total_hours, lecture_hours, practice_hours, lab_hours, control_type, semester, specialty_id) VALUES
('Б1.В.01', 'Математика', 108, 36, 36, 36, 'экзамен', 1, (SELECT id FROM specialties WHERE code = '09.03.01')),
('Б1.В.02', 'Объектно-ориентированное программирование', 72, 18, 18, 36, 'экзамен', 1, (SELECT id FROM specialties WHERE code = '09.03.01')),
('Б1.В.03', 'Физика', 90, 30, 30, 30, 'экзамен', 2, (SELECT id FROM specialties WHERE code = '09.03.01')),
('Б1.В.04', 'Базы данных', 108, 36, 36, 36, 'экзамен', 3, (SELECT id FROM specialties WHERE code = '09.03.01')),
('Б2.В.01', 'Экономика', 72, 24, 24, 24, 'зачёт', 3, (SELECT id FROM specialties WHERE code = '38.03.01')),
('Б1.В.05', 'Теория вероятностей', 72, 24, 24, 24, 'экзамен', 2, (SELECT id FROM specialties WHERE code = '09.03.01')),
('Б1.В.06', 'Операционные системы', 90, 30, 30, 30, 'экзамен', 4, (SELECT id FROM specialties WHERE code = '09.03.01')),
('Б1.В.07', 'Компьютерные сети', 72, 18, 18, 36, 'зачёт', 4, (SELECT id FROM specialties WHERE code = '09.03.01'))
ON CONFLICT (index_code) DO NOTHING;

-- 6. Вставка студентов
INSERT INTO students (fio, birth_date, gender, phone, email, record_book_number, student_id_number, enrollment_order_date, enrollment_order_number, status, is_budget, group_id) VALUES
('Иванов Иван Иванович', '2004-05-15', 'м', '+79991234567', 'ivanov@mail.ru', '18Б123456', '123', '2022-09-01', '77/к', 'обучается', TRUE, (SELECT id FROM groups WHERE number = 'ИТ-31')),
('Петрова Анна Сергеевна', '2004-08-22', 'ж', '+79997654321', 'petrova@mail.ru', '18Б123457', '124', '2022-09-01', '78/к', 'обучается', FALSE, (SELECT id FROM groups WHERE number = 'ИТ-31')),
('Сидоров Пётр Алексеевич', '2003-11-10', 'м', '+79995556677', 'sidorov@mail.ru', '19Б223311', '225', '2023-09-01', '101/к', 'обучается', TRUE, (SELECT id FROM groups WHERE number = 'ИТ-32')),
('Козлова Мария Дмитриевна', '2005-03-18', 'ж', '+79993332211', 'kozlova@mail.ru', '20К112233', '334', '2024-09-01', '45/к', 'обучается', FALSE, (SELECT id FROM groups WHERE number = 'ИС-21')),
('Смирнов Алексей Викторович', '2004-01-20', 'м', '+79994445566', 'smirnov@mail.ru', '18Б123458', '125', '2022-09-01', '79/к', 'обучается', TRUE, (SELECT id FROM groups WHERE number = 'ИТ-31')),
('Новикова Ольга Ивановна', '2004-06-30', 'ж', '+79995557788', 'novikova@mail.ru', '18Б123459', '126', '2022-09-01', '80/к', 'обучается', TRUE, (SELECT id FROM groups WHERE number = 'ИТ-31')),
('Волков Дмитрий Сергеевич', '2003-09-12', 'м', '+79996668899', 'volkov@mail.ru', '19Б223312', '226', '2023-09-01', '102/к', 'обучается', FALSE, (SELECT id FROM groups WHERE number = 'ИТ-32')),
('Лебедева Екатерина Александровна', '2005-02-14', 'ж', '+79997779900', 'lebedeva@mail.ru', '20К112234', '335', '2024-09-01', '46/к', 'обучается', TRUE, (SELECT id FROM groups WHERE number = 'ИС-21')),
('Морозов Игорь Владимирович', '2004-07-25', 'м', '+79998880011', 'morozov@mail.ru', '18Б123460', '127', '2022-09-01', '81/к', 'обучается', TRUE, (SELECT id FROM groups WHERE number = 'ИТ-31')),
('Федорова Татьяна Сергеевна', '2003-12-05', 'ж', '+79999991122', 'fedorova@mail.ru', '19Б223313', '227', '2023-09-01', '103/к', 'обучается', FALSE, (SELECT id FROM groups WHERE number = 'ИТ-32'))
ON CONFLICT (record_book_number) DO NOTHING;

-- 7. Вставка оценок
INSERT INTO grades (student_id, discipline_id, value, in_words, date, attempt_number, teacher_fio, is_debt) VALUES
-- Оценки для Иванова И.И.
((SELECT id FROM students WHERE record_book_number = '18Б123456'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), 5, 'отлично', '2025-01-12', 1, 'Иванов П.П.', FALSE),
((SELECT id FROM students WHERE record_book_number = '18Б123456'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.02'), 4, 'хорошо', '2025-01-15', 1, 'Петров С.С.', FALSE),
((SELECT id FROM students WHERE record_book_number = '18Б123456'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.03'), 5, 'отлично', '2025-02-01', 1, 'Смирнова А.А.', FALSE),
-- Оценки для Петровой А.С.
((SELECT id FROM students WHERE record_book_number = '18Б123457'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), 4, 'хорошо', '2025-01-12', 1, 'Иванов П.П.', FALSE),
((SELECT id FROM students WHERE record_book_number = '18Б123457'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.02'), 5, 'отлично', '2025-01-15', 1, 'Петров С.С.', FALSE),
-- Оценки для Сидорова П.А.
((SELECT id FROM students WHERE record_book_number = '19Б223311'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.03'), 3, 'удовлетворительно', '2025-02-01', 2, 'Смирнова А.А.', TRUE),
((SELECT id FROM students WHERE record_book_number = '19Б223311'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), 4, 'хорошо', '2025-01-12', 1, 'Иванов П.П.', FALSE),
-- Оценки для Смирнова А.В.
((SELECT id FROM students WHERE record_book_number = '18Б123458'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), 5, 'отлично', '2025-01-12', 1, 'Иванов П.П.', FALSE),
((SELECT id FROM students WHERE record_book_number = '18Б123458'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.02'), 5, 'отлично', '2025-01-15', 1, 'Петров С.С.', FALSE),
-- Оценки для Новиковой О.И.
((SELECT id FROM students WHERE record_book_number = '18Б123459'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), 4, 'хорошо', '2025-01-12', 1, 'Иванов П.П.', FALSE)
ON CONFLICT DO NOTHING;

-- 8. Вставка посещаемости
INSERT INTO attendance (student_id, discipline_id, date, lesson_number, present, reason_if_absent) VALUES
-- Посещаемость для Иванова И.И.
((SELECT id FROM students WHERE record_book_number = '18Б123456'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), '2025-03-10', 1, TRUE, NULL),
((SELECT id FROM students WHERE record_book_number = '18Б123456'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), '2025-03-12', 2, TRUE, NULL),
((SELECT id FROM students WHERE record_book_number = '18Б123456'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.02'), '2025-03-11', 1, FALSE, 'болезнь'),
((SELECT id FROM students WHERE record_book_number = '18Б123456'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.02'), '2025-03-13', 2, TRUE, NULL),
-- Посещаемость для Петровой А.С.
((SELECT id FROM students WHERE record_book_number = '18Б123457'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), '2025-03-10', 1, TRUE, NULL),
((SELECT id FROM students WHERE record_book_number = '18Б123457'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), '2025-03-12', 2, TRUE, NULL),
((SELECT id FROM students WHERE record_book_number = '18Б123457'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.02'), '2025-03-11', 1, TRUE, NULL),
-- Посещаемость для Сидорова П.А.
((SELECT id FROM students WHERE record_book_number = '19Б223311'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.03'), '2025-03-15', 3, FALSE, 'по уважительной'),
((SELECT id FROM students WHERE record_book_number = '19Б223311'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.03'), '2025-03-17', 4, TRUE, NULL),
-- Посещаемость для Козловой М.Д.
((SELECT id FROM students WHERE record_book_number = '20К112233'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), '2025-03-10', 1, TRUE, NULL),
((SELECT id FROM students WHERE record_book_number = '20К112233'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), '2025-03-12', 2, FALSE, 'болезнь'),
-- Посещаемость для Смирнова А.В.
((SELECT id FROM students WHERE record_book_number = '18Б123458'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.01'), '2025-03-10', 1, TRUE, NULL),
((SELECT id FROM students WHERE record_book_number = '18Б123458'), (SELECT id FROM disciplines WHERE index_code = 'Б1.В.02'), '2025-03-11', 1, TRUE, NULL)
ON CONFLICT DO NOTHING;

-- Вывод статистики
SELECT 'Факультетов: ' || COUNT(*) FROM faculties;
SELECT 'Специальностей: ' || COUNT(*) FROM specialties;
SELECT 'Групп: ' || COUNT(*) FROM groups;
SELECT 'Преподавателей: ' || COUNT(*) FROM teachers;
SELECT 'Дисциплин: ' || COUNT(*) FROM disciplines;
SELECT 'Студентов: ' || COUNT(*) FROM students;
SELECT 'Оценок: ' || COUNT(*) FROM grades;
SELECT 'Записей посещаемости: ' || COUNT(*) FROM attendance;

