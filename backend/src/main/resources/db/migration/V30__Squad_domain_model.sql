CREATE TABLE squad (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,

    CONSTRAINT squad_unique_1 UNIQUE (name)
);