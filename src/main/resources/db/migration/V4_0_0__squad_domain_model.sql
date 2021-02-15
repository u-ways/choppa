CREATE TABLE squad
(
    squad_id UUID PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    color    INTEGER      NOT NULL,
    tribe    UUID         NOT NULL,

    CONSTRAINT tribe_fk1 FOREIGN KEY (tribe) REFERENCES tribe (tribe_id)
);