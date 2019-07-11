CREATE TABLE IF NOT EXISTS dyno(
   id serial PRIMARY KEY,
   porta integer,
   host VARCHAR (255)
);

INSERT INTO dyno (host, porta) VALUES (?, ?);