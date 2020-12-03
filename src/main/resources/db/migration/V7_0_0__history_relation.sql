CREATE TABLE history
(
    iteration_id UUID NOT NULL,
    tribe_id     UUID NOT NULL,
    squad_id     UUID NOT NULL,
    member_id    UUID NOT NULL,
    create_date  TIMESTAMP WITH TIME ZONE NOT NULL,

    PRIMARY KEY (iteration_id, tribe_id, squad_id, member_id, create_date),
    CONSTRAINT history_fk1 FOREIGN KEY (iteration_id) REFERENCES iteration (iteration_id),
    CONSTRAINT history_fk2 FOREIGN KEY (tribe_id) REFERENCES tribe (tribe_id),
    CONSTRAINT history_fk3 FOREIGN KEY (squad_id) REFERENCES squad (squad_id),
    CONSTRAINT history_fk4 FOREIGN KEY (member_id) REFERENCES member (member_id)
);