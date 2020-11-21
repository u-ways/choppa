CREATE TABLE squad_current_members
(
    squad_id  UUID NOT NULL,
    member_id UUID NOT NULL,

    PRIMARY KEY (squad_id, member_id),
    CONSTRAINT squad_current_members_fk1 FOREIGN KEY (squad_id) REFERENCES squad (squad_id),
    CONSTRAINT squad_current_members_fk2 FOREIGN KEY (member_id) REFERENCES member (member_id)
);