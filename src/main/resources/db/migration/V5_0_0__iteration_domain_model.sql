CREATE TABLE iteration
(
    iteration_id UUID PRIMARY KEY,
    number       INTEGER                  NOT NULL,
    start_date   TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date     TIMESTAMP WITH TIME ZONE NOT NULL
);