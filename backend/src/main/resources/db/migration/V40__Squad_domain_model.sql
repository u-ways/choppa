CREATE TABLE squad
(
    squad_id UUID PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    tribe    UUID,

    CONSTRAINT tribe_fk1 FOREIGN KEY (tribe) REFERENCES tribe (tribe_id)
);