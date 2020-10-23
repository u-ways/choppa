CREATE TABLE member
(
    member_id UUID PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    chapter   UUID,

    CONSTRAINT member_fk1 FOREIGN KEY (chapter) REFERENCES chapter (chapter_id)
);