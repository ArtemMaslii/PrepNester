-- Liquibase formatted SQL
-- Database schema creation

-- User Details
CREATE TABLE user_details
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
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
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    accessType VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- User Roles
CREATE TABLE user_roles
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_details (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);

-- User Experience
CREATE TABLE user_experience
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id           BIGINT NOT NULL,
    current_job_title TEXT,
    position_prefix   TEXT,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_details (id)
);

-- User Education
CREATE TABLE user_education
(
    id                       BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id                  BIGINT NOT NULL,
    education_institute_name TEXT,
    education_degree         TEXT,
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_details (id)
);

-- Category
CREATE TABLE category
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    title      TEXT   NOT NULL UNIQUE,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id)
);

-- Question
CREATE TABLE question
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       TEXT    NOT NULL,
    is_public   BOOLEAN NOT NULL,
    category_id BIGINT  NOT NULL,
    created_by  BIGINT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by  BIGINT,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id)
);

-- Sub Questions
CREATE TABLE sub_questions
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_question_id BIGINT NOT NULL,
    title              TEXT   NOT NULL,
    created_by         BIGINT NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by         BIGINT NOT NULL,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_question_id) REFERENCES question (id),
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id)
);

-- Cheat Sheet
CREATE TABLE cheat_sheet
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    title      TEXT   NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id)
);

-- Candidate
CREATE TABLE candidate
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name      VARCHAR(255) NOT NULL,
    email          VARCHAR(255) NOT NULL,
    phone_name     VARCHAR(255) NOT NULL,
    cheat_sheet_id BIGINT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cheat_sheet_id) REFERENCES cheat_sheet (id)
);

-- Interview
CREATE TABLE interview
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id       BIGINT       NOT NULL,
    status             VARCHAR(255) NOT NULL,
    process_start_date TIMESTAMP,
    department_name    VARCHAR(255),
    notes              TEXT,
    created_by         BIGINT,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES candidate (id),
    FOREIGN KEY (created_by) REFERENCES user_details (id)
);

-- Question Likes
CREATE TABLE question_likes
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_details (id),
    FOREIGN KEY (question_id) REFERENCES question (id)
);

-- Cheat Sheet Likes
CREATE TABLE cheat_sheet_likes
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id        BIGINT NOT NULL,
    cheat_sheet_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_details (id),
    FOREIGN KEY (cheat_sheet_id) REFERENCES cheat_sheet (id)
);

-- Comment
CREATE TABLE comment
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    message     TEXT   NOT NULL,
    created_by  BIGINT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by  BIGINT NOT NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id),
    FOREIGN KEY (question_id) REFERENCES question (id)
);

-- Comment Replies
CREATE TABLE comment_replies
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    comment_id    BIGINT NOT NULL,
    reply_message TEXT   NOT NULL,
    created_by    BIGINT NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by    BIGINT NOT NULL,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user_details (id),
    FOREIGN KEY (updated_by) REFERENCES user_details (id),
    FOREIGN KEY (comment_id) REFERENCES comment (id)
);

-- Many-to-Many: Questions & Cheat Sheets
CREATE TABLE questions_cheat_sheets
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id    BIGINT NOT NULL,
    cheat_sheet_id BIGINT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question (id),
    FOREIGN KEY (cheat_sheet_id) REFERENCES cheat_sheet (id)
);

-- Many-to-Many: Categories & Cheat Sheets
CREATE TABLE categories_cheat_sheets
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id    BIGINT NOT NULL,
    cheat_sheet_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (cheat_sheet_id) REFERENCES cheat_sheet (id)
);

-- Many-to-Many: Questions & Categories
CREATE TABLE questions_categories
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);
