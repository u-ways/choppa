CREATE TYPE REVISION_TYPE AS ENUM (
    'ADD',
    'REMOVE'
);

-- Create an implicit conversion from varchar to the enum in the database
-- to address JPA and Postgres enums limitations.
CREATE CAST (CHARACTER VARYING as REVISION_TYPE) WITH INOUT AS IMPLICIT;

CREATE TABLE squad_member_history
(
    squad_id        UUID                     NOT NULL,
    member_id       UUID                     NOT NULL,
    revision_number INTEGER                  NOT NULL,
    revision_type   revision_type            NOT NULL,
    create_date     TIMESTAMP WITH TIME ZONE NOT NULL,

    PRIMARY KEY (squad_id, member_id, create_date),
    CONSTRAINT squad_member_history_fk1 FOREIGN KEY (squad_id) REFERENCES squad (squad_id),
    CONSTRAINT squad_member_history_fk2 FOREIGN KEY (member_id) REFERENCES member (member_id)
);