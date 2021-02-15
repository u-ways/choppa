CREATE TABLE member
(
    member_id UUID PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    chapter   UUID         NOT NULL,
    active    BOOLEAN DEFAULT TRUE,

    CONSTRAINT member_fk1 FOREIGN KEY (chapter) REFERENCES chapter (chapter_id)
);