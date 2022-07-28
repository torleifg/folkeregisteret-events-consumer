CREATE TABLE IF NOT EXISTS sequence
(
    id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS person
(
    id   VARCHAR(11),
    name VARCHAR(50),
    PRIMARY KEY (id)
);