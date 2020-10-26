CREATE TABLE iteration
(
    iteration_id UUID PRIMARY KEY,
    number       INTEGER   NOT NULL,
    timebox      INTEGER   NOT NULL,
    date TIMESTAMP WITH TIME ZONE NOT NULL
);