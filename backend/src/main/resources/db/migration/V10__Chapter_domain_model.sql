CREATE TABLE chapter (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,

    CONSTRAINT chapter_unique_1 UNIQUE (name)
);