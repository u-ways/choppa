CREATE TABLE iteration_history(
                                  iteration_id UUID NOT NULL,
                                  tribe_id     UUID NOT NULL,
                                  squad_id     UUID NOT NULL,
                                  member_id    UUID NOT NULL,

                                  PRIMARY KEY (iteration_id, tribe_id, squad_id, member_id),
                                  CONSTRAINT iteration_history_fk_1 FOREIGN KEY (iteration_id) REFERENCES iteration (iteration_id),
                                  CONSTRAINT iteration_history_fk_2 FOREIGN KEY (tribe_id) REFERENCES tribe (tribe_id),
                                  CONSTRAINT iteration_history_fk_3 FOREIGN KEY (squad_id) REFERENCES squad (squad_id),
                                  CONSTRAINT iteration_history_fk_4 FOREIGN KEY (member_id) REFERENCES member (member_id)
);