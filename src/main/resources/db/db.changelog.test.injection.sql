--liquibase formatted sql

-- User Details
-- changeset testdata:1
INSERT INTO user_details (full_name, email, phone_number, sex)
VALUES ('Alice Johnson', 'alice@example.com', '123456789', 'F'),
       ('Bob Smith', 'bob@example.com', '987654321', 'M');

-- User Experience
-- changeset testdata:2
INSERT INTO user_experience (user_id, current_job_title, position_prefix)
VALUES (1, 'Software Engineer', 'Senior'),
       (2, 'Data Analyst', 'Junior');

-- User Education
-- changeset testdata:3
INSERT INTO user_education (user_id, education_institute_name, education_degree)
VALUES (1, 'MIT', 'Computer Science'),
       (2, 'Harvard', 'Data Science');

-- Auth
-- changeset testdata:4
INSERT INTO auth (user_id, password_hash)
VALUES (1, 'hashed_password_1'),
       (2, 'hashed_password_2');

-- Role
-- changeset testdata:5
INSERT INTO role (name, user_id)
VALUES ('Admin', 1),
       ('User', 2);

-- Category
-- changeset testdata:6
INSERT INTO category (title, created_by, updated_by)
VALUES ('Algorithms', 1, 1),
       ('Data Structures', 2, 2);

-- Question
-- changeset testdata:7
INSERT INTO question (title, is_public, category_id, created_by, updated_by)
VALUES ('What is QuickSort?', TRUE, 1, 1, 1),
       ('Explain Binary Trees', TRUE, 2, 2, 2);

-- Sub Questions
-- changeset testdata:8
INSERT INTO sub_questions (parent_question_id, title, created_by, updated_by)
VALUES (1, 'What is the time complexity of QuickSort?', 1, 1),
       (2, 'What are AVL Trees?', 2, 2);

-- Cheat Sheet
-- changeset testdata:9
INSERT INTO cheat_sheet (title, created_by, updated_by)
VALUES ('Sorting Algorithms', 1, 1),
       ('Data Structures Guide', 2, 2);

-- Candidate
-- changeset testdata:10
INSERT INTO candidate (full_name, email, phone_name, cheat_sheet_id)
VALUES ('Charlie Brown', 'charlie@example.com', '1122334455', 1),
       ('Dana White', 'dana@example.com', '9988776655', 2);

-- Interview
-- changeset testdata:11
INSERT INTO interview (candidate_id, status, process_start_date, department_name, notes, created_by)
VALUES (1, 'In Progress', '2025-03-27', 'Software Engineering', 'Candidate has strong DSA skills',
        1),
       (2, 'Pending', '2025-03-27', 'Data Science', 'Candidate needs to improve SQL knowledge', 2);

-- Question Likes
-- changeset testdata:12
INSERT INTO question_likes (user_id, question_id)
VALUES (1, 1),
       (2, 2);

-- Cheat Sheet Likes
-- changeset testdata:13
INSERT INTO cheat_sheet_likes (user_id, cheat_sheet_id)
VALUES (1, 1),
       (2, 2);

-- Comment
-- changeset testdata:14
INSERT INTO comment (question_id, message, created_by, updated_by)
VALUES (1, 'QuickSort is really fast for large datasets!', 1, 1),
       (2, 'AVL Trees are self-balancing binary search trees.', 2, 2);

-- Comment Replies
-- changeset testdata:15
INSERT INTO comment_replies (comment_id, reply_message, created_by, updated_by)
VALUES (1, 'Yes, but it depends on pivot selection.', 2, 2),
       (2, 'Exactly! They maintain O(log n) height.', 1, 1);

-- Many-to-Many: Questions & Cheat Sheets
-- changeset testdata:16
INSERT INTO questions_cheat_sheets (question_id, cheat_sheet_id)
VALUES (1, 1),
       (2, 2);

-- Many-to-Many: Categories & Cheat Sheets
-- changeset testdata:17
INSERT INTO categories_cheat_sheets (category_id, cheat_sheet_id)
VALUES (1, 1),
       (2, 2);

-- Many-to-Many: Questions & Categories
-- changeset testdata:18
INSERT INTO questions_categories (question_id, category_id)
VALUES (1, 1),
       (2, 2);
