CREATE TABLE IF NOT EXISTS course (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    course_number CHAR(10) UNIQUE NOT NULL,
    course_title VARCHAR(255),
    credits FLOAT,
    subject VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS course_core (
    course_id INTEGER NOT NULL,
    core_code CHAR(3) NOT NULL,
    PRIMARY KEY (course_id, core_code),
    FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE IF NOT EXISTS app_user (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_course (
    user_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);