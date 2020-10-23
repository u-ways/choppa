CREATE TABLE chapter
(
    chapter_id UUID PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,

    CONSTRAINT chapter_u1 UNIQUE (name)
);