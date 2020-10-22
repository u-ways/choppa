CREATE TABLE tribe_current_squads (
    tribe_id SERIAL NOT NULL,
    squad_id SERIAL NOT NULL,

    PRIMARY KEY (tribe_id, squad_id),
    CONSTRAINT tribe_current_members_fk_1 FOREIGN KEY (tribe_id) REFERENCES tribe (id),
    CONSTRAINT tribe_current_members_fk_2 FOREIGN KEY (squad_id) REFERENCES squad (id)
);