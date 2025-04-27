-- Liquibase formatted SQL
-- Database schema creation

-- User Details
CREATE TABLE user_details
(
    id            UUID PRIMARY KEY,
    full_name     VARCHAR(255)        NOT NULL,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL,
    phone_number  VARCHAR(255),
    gender        CHAR(1) CHECK (gender IN ('F', 'M', 'X')),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Role
CREATE TABLE role
(
    id          UUID PRIMARY KEY,
    access_type VARCHAR(20) NOT NULL UNIQUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- User Roles
CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user_details (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);

-- User Experience
CREATE TABLE user_experience
(
    id                UUID PRIMARY KEY,
    user_id           UUID NOT NULL,
    current_job_title TEXT,
    position_prefix   TEXT,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_details (id)
);

-- User Education
CREATE TABLE user_education
(
    id                       UUID PRIMARY KEY,
    user_id                  UUID NOT NULL,
    education_institute_name TEXT,
    education_degree         TEXT,
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_details (id)
);

-- Category
CREATE TABLE category
(
    id         UUID PRIMARY KEY,
    title      TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Question
CREATE TABLE question
(
    id          UUID PRIMARY KEY,
    title       TEXT    NOT NULL,
    is_public   BOOLEAN NOT NULL,
    category_id UUID    NOT NULL,
    created_by  UUID,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by  UUID,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id)
);

-- Sub Questions
CREATE TABLE sub_questions
(
    id                 UUID PRIMARY KEY,
    parent_question_id UUID NOT NULL,
    title              TEXT NOT NULL,
    created_by         UUID NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by         UUID,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_question_id) REFERENCES question (id),
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id)
);

-- Cheat Sheet
CREATE TABLE cheat_sheet
(
    id         UUID PRIMARY KEY,
    title      TEXT NOT NULL,
    created_by UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id)
);

-- Candidate
CREATE TABLE candidate
(
    id             UUID PRIMARY KEY,
    full_name      VARCHAR(255) NOT NULL,
    email          VARCHAR(255) NOT NULL,
    phone_name     VARCHAR(255) NOT NULL,
    cheat_sheet_id UUID,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cheat_sheet_id) REFERENCES cheat_sheet (id)
);

-- Interview
CREATE TABLE interview
(
    id                 UUID PRIMARY KEY,
    candidate_id       UUID         NOT NULL,
    status             VARCHAR(255) NOT NULL,
    process_start_date TIMESTAMP,
    department_name    VARCHAR(255),
    notes              TEXT,
    created_by         UUID,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by         UUID,
    FOREIGN KEY (candidate_id) REFERENCES candidate (id),
    FOREIGN KEY (created_by) REFERENCES user_details (id)
);

-- Comment
CREATE TABLE comment
(
    id              UUID PRIMARY KEY,
    question_id     UUID,
    sub_question_id UUID,
    parent_id       UUID,
    message         TEXT NOT NULL,
    created_by      UUID NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by      UUID,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id),
    FOREIGN KEY (question_id) REFERENCES question (id),
    FOREIGN KEY (sub_question_id) REFERENCES sub_questions (id),
    FOREIGN KEY (parent_id) REFERENCES comment (id)
);

CREATE TABLE likes
(
    id              UUID PRIMARY KEY,
    user_id         UUID NOT NULL,
    question_id     UUID,
    sub_question_id UUID,
    comment_id      UUID,
    cheat_sheet_id  UUID,
    FOREIGN KEY (user_id) REFERENCES user_details (id),
    FOREIGN KEY (question_id) REFERENCES question (id),
    FOREIGN KEY (sub_question_id) REFERENCES sub_questions (id),
    FOREIGN KEY (comment_id) REFERENCES comment (id),
    FOREIGN KEY (cheat_sheet_id) REFERENCES cheat_sheet (id)
);

-- Many-to-Many: Categories & Cheat Sheets
CREATE TABLE categories_cheat_sheets
(
    category_id    UUID NOT NULL,
    cheat_sheet_id UUID NOT NULL,
    PRIMARY KEY (category_id, cheat_sheet_id),
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (cheat_sheet_id) REFERENCES cheat_sheet (id)
);

-- Many-to-Many: Questions & Cheat Sheets
CREATE TABLE questions_cheat_sheets
(
    question_id    UUID NOT NULL,
    cheat_sheet_id UUID NOT NULL,
    PRIMARY KEY (question_id, cheat_sheet_id),
    FOREIGN KEY (question_id) REFERENCES question (id),
    FOREIGN KEY (cheat_sheet_id) REFERENCES cheat_sheet (id)
);

-- Many-to-Many: Questions & Categories
CREATE TABLE categories_questions
(
    question_id UUID NOT NULL,
    category_id UUID NOT NULL,
    PRIMARY KEY (question_id, category_id),
    FOREIGN KEY (question_id) REFERENCES question (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);
