DROP TABLE IF EXISTS es_river_stats;

CREATE TABLE es_river_stats (
  last_run timestamp with time zone not null
);

DELETE FROM es_river_stats;
INSERT INTO es_river_stats (last_run) VALUES (date '1970-01-01');