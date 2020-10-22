CREATE TABLE iteration_history (
    iteration_id    INTEGER NOT NULL,
    tribe_id        INTEGER NOT NULL,
    squad_id        INTEGER NOT NULL,
    member_id       INTEGER NOT NULL,

    PRIMARY KEY (iteration_id, tribe_id, squad_id, member_id),
    CONSTRAINT iteration_history_fk_1 FOREIGN KEY (iteration_id) REFERENCES iteration (id),
    CONSTRAINT iteration_history_fk_2 FOREIGN KEY (tribe_id) REFERENCES tribe (id),
    CONSTRAINT iteration_history_fk_3 FOREIGN KEY (squad_id) REFERENCES squad (id),
    CONSTRAINT iteration_history_fk_4 FOREIGN KEY (member_id) REFERENCES member (id)
);