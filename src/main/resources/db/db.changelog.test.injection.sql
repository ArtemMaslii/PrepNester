--Liquibase formatted sql

-- User Details
-- changeset testdata:1
INSERT INTO user_details (id, full_name, email, password_hash, phone_number, gender)
VALUES ('a1f42067-8f71-4be1-bc9d-95adf4f5c423', 'Alice Johnson', 'alice@example.com',
        '$2a$10$.I3EBER.hGDpVDJ6ocOjF.0D1OLJqg9FvqbiQDAyEvT6TvnofMcIm', '123456789',
        'F'),
       ('db50f01e-2a8d-4be4-ae60-d9ad89fc4b72', 'Bob Smith', 'bob@example.com',
        '$2a$10$o5kkXypxj7bWO31nbIG2L.UiuSbDdKY2otBS/3EfojclKeCa47zBe', '987654321', 'M');

-- User Experience
-- changeset testdata:2
INSERT INTO user_experience (id, user_id, current_job_title, position_prefix)
VALUES ('f15c8c70-3d31-4f23-a35c-285f5874be56', 'a1f42067-8f71-4be1-bc9d-95adf4f5c423',
        'Software Engineer', 'Senior'),
       ('77b1cfd3-ff9d-4265-b7a9-c6161b9fe466', 'db50f01e-2a8d-4be4-ae60-d9ad89fc4b72',
        'Data Analyst', 'Junior');

-- User Education
-- changeset testdata:3
INSERT INTO user_education (id, user_id, education_institute_name, education_degree)
VALUES ('d4fd75a3-05a4-47d3-9d2f-dac51f1f9f75', 'a1f42067-8f71-4be1-bc9d-95adf4f5c423', 'MIT',
        'Computer Science'),
       ('a28a9573-bc91-41ad-800b-bf24818b5d8f', 'db50f01e-2a8d-4be4-ae60-d9ad89fc4b72', 'Harvard',
        'Data Science');

-- Role
-- changeset testdata:4
INSERT INTO role (id, access_type)
VALUES ('3ba02c1a-7cc4-4a2b-8d85-19394d8f7a32', 'CANDIDATE'),
       ('e11f5c72-0f8a-47b0-bb7a-72b52238a41d', 'ADMIN');

-- User Role
-- changeset testdata:5
INSERT INTO user_roles (user_id, role_id)
VALUES ('a1f42067-8f71-4be1-bc9d-95adf4f5c423',
        '3ba02c1a-7cc4-4a2b-8d85-19394d8f7a32'),
       ('db50f01e-2a8d-4be4-ae60-d9ad89fc4b72',
        'e11f5c72-0f8a-47b0-bb7a-72b52238a41d');

-- Category
-- changeset testdata:6
INSERT INTO category (id, title)
VALUES ('1e52f3b1-b2cf-470d-8338-bdbba6988e7b', 'Algorithms'),
       ('e36fdba0-f12d-4a51-83a9-60e37452a831', 'Data Structures');

-- Question
-- changeset testdata:7
INSERT INTO question (id, title, is_public, category_id, created_by)
VALUES ('784bfa2b-6fc4-4288-a8ae-0579c4cc0b18', 'What is QuickSort?', TRUE,
        '1e52f3b1-b2cf-470d-8338-bdbba6988e7b',
        'a1f42067-8f71-4be1-bc9d-95adf4f5c423'),
       ('784bfa2b-6fc4-4288-a8ae-0579c4cc0b17', 'How does OpenSearch Works', TRUE,
        '1e52f3b1-b2cf-470d-8338-bdbba6988e7b', 'a1f42067-8f71-4be1-bc9d-95adf4f5c423'),
       ('9345ef2f-cd8f-4eae-97cc-6f4e81ed995d', 'Explain Binary Trees', TRUE,
        'e36fdba0-f12d-4a51-83a9-60e37452a831',
        'db50f01e-2a8d-4be4-ae60-d9ad89fc4b72');

-- Sub Questions
-- changeset testdata:8
INSERT INTO sub_questions (id, parent_question_id, title, created_by)
VALUES ('f5d1b9fe-2b50-4b5c-befb-7b4f82fa1b5e', '784bfa2b-6fc4-4288-a8ae-0579c4cc0b18',
        'What is the time complexity of QuickSort?',
        'a1f42067-8f71-4be1-bc9d-95adf4f5c423'),
       ('6c7ab890-501b-4e97-bb90-0045b021bf8b', '9345ef2f-cd8f-4eae-97cc-6f4e81ed995d',
        'What are AVL Trees?',
        'db50f01e-2a8d-4be4-ae60-d9ad89fc4b72');

-- Cheat Sheet
-- changeset testdata:9
INSERT INTO cheat_sheet (id, title, created_by)
VALUES ('f03236c3-5746-4d2b-9c97-6b93c340e79f', 'Sorting Algorithms',
        'a1f42067-8f71-4be1-bc9d-95adf4f5c423'),
       ('cfdb8f27-94a2-4cfc-8c8f-e5404db96869', 'Data Structures Guide',
        'db50f01e-2a8d-4be4-ae60-d9ad89fc4b72');

-- Candidate
-- changeset testdata:10
INSERT INTO candidate (id, full_name, password_hash, raw_password, email, phone_number,
                       cheat_sheet_id,
                       created_by)
VALUES ('f98e3db5-c055-4681-81f7-2bc01018479b', 'Charlie Brown',
        '$2a$10$vhAYi9CvzaiFLgDjPWwAB.jvGMGcfSBfi8fLgbs7Nyf8WZUIvvcSe',
        'f98e3db5-c055-4681-81f7-2bc01018479a',
        'charlie@example.com',
        '1122334455', 'f03236c3-5746-4d2b-9c97-6b93c340e79f',
        'a1f42067-8f71-4be1-bc9d-95adf4f5c423'),
       ('9c54ff62-11bc-4d39-9b78-2648c9f42fa7', 'Dana White',
        '$2a$10$uqXtqWiVCuwpFqCvVUEazO75gVwT7ikMnWNjowXK99qHl.tLgVhe6',
        '9c54ff62-11bc-4d39-9b78-2648c9f42fa8',
        'dana@example.com',
        '9988776655', 'cfdb8f27-94a2-4cfc-8c8f-e5404db96869',
        'db50f01e-2a8d-4be4-ae60-d9ad89fc4b72');

-- Interview
-- changeset testdata:11
INSERT INTO interview (id, candidate_id, open_position, status, department_name, notes,
                       created_by)
VALUES ('c5f3bfae-2314-48e1-9a72-bb0281775d34', 'f98e3db5-c055-4681-81f7-2bc01018479b',
        'Software Engineer', 'IN_PROGRESS', 'Kaunas Office', null,
        'a1f42067-8f71-4be1-bc9d-95adf4f5c423'),
       ('3ad72cd1-bfc3-4b65-84db-651bfa214a7f', '9c54ff62-11bc-4d39-9b78-2648c9f42fa7',
        'Data Scientist', 'CANCELLED', 'Remote',
        'Candidate needs to improve SQL knowledge', 'db50f01e-2a8d-4be4-ae60-d9ad89fc4b72');

-- Comment
-- changeset testdata:14
INSERT INTO comment (id, question_id, sub_question_id, message, parent_id, created_by)
VALUES ('f8bff2a2-e87f-4f07-8b70-7cf9780b7e9f', '784bfa2b-6fc4-4288-a8ae-0579c4cc0b18', null,
        'QuickSort is an efficient sorting algorithm.', null,
        'db50f01e-2a8d-4be4-ae60-d9ad89fc4b72'),
       ('f8bff2a2-e87f-4f07-8b70-7cf9780b7e91', null, 'f5d1b9fe-2b50-4b5c-befb-7b4f82fa1b5e',
        'Yeap!', null, 'a1f42067-8f71-4be1-bc9d-95adf4f5c423'),
       ('a03526e9-dcfe-45db-9749-7d5a8310c906', '784bfa2b-6fc4-4288-a8ae-0579c4cc0b18', null,
        'Yes, it is commonly used for large datasets.', 'f8bff2a2-e87f-4f07-8b70-7cf9780b7e9f',
        'a1f42067-8f71-4be1-bc9d-95adf4f5c423'),
       ('e4f1ff79-4a6b-4fba-9c27-b77200d1e0cd', '9345ef2f-cd8f-4eae-97cc-6f4e81ed995d', null,
        'AVL Trees are a self-balancing binary search tree.', null,
        'a1f42067-8f71-4be1-bc9d-95adf4f5c423');

INSERT INTO categories_cheat_sheets (category_id, cheat_sheet_id)
VALUES ('1e52f3b1-b2cf-470d-8338-bdbba6988e7b',
        'f03236c3-5746-4d2b-9c97-6b93c340e79f'),
       ('e36fdba0-f12d-4a51-83a9-60e37452a831',
        'cfdb8f27-94a2-4cfc-8c8f-e5404db96869');

INSERT INTO questions_cheat_sheets (question_id, cheat_sheet_id)
VALUES ('784bfa2b-6fc4-4288-a8ae-0579c4cc0b18', 'f03236c3-5746-4d2b-9c97-6b93c340e79f'),
       ('9345ef2f-cd8f-4eae-97cc-6f4e81ed995d', 'cfdb8f27-94a2-4cfc-8c8f-e5404db96869');

INSERT INTO categories_questions (category_id, question_id)
VALUES ('1e52f3b1-b2cf-470d-8338-bdbba6988e7b',
        '784bfa2b-6fc4-4288-a8ae-0579c4cc0b18'),
       ('1e52f3b1-b2cf-470d-8338-bdbba6988e7b', '784bfa2b-6fc4-4288-a8ae-0579c4cc0b17'),
       ('e36fdba0-f12d-4a51-83a9-60e37452a831',
        '9345ef2f-cd8f-4eae-97cc-6f4e81ed995d');
