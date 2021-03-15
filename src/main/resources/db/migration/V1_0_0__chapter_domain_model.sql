CREATE TABLE chapter
(
    chapter_id UUID PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    color      INTEGER      NOT NULL,

    CONSTRAINT chapter_u1 UNIQUE (chapter_id, name)
);