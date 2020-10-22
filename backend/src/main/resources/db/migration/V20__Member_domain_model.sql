CREATE TABLE member (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    chapter_id INTEGER,

    CONSTRAINT member_fk1 FOREIGN KEY (chapter_id) REFERENCES chapter (id)
);