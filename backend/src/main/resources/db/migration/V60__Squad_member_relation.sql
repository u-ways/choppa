CREATE TABLE squad_current_members (
    squad_id      INTEGER NOT NULL,
    member_id     INTEGER NOT NULL,
    rotation_date TIMESTAMP NOT NULL,

    PRIMARY KEY (squad_id, member_id),
    CONSTRAINT squad_current_members_fk_1 FOREIGN KEY (squad_id) REFERENCES squad (id),
    CONSTRAINT squad_current_members_fk_2 FOREIGN KEY (member_id) REFERENCES member (id)
);