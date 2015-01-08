DROP TABLE IF EXISTS cities;

CREATE TABLE cities (
  id          BIGINT                   NOT NULL PRIMARY KEY,
  country_id  BIGINT                   NOT NULL,
  name        TEXT                     NOT NULL
);


DROP SEQUENCE IF EXISTS cities_id_seq;

CREATE SEQUENCE cities_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE INDEX cities$name__idx ON cities USING BTREE (name);
CREATE INDEX cities$country_id__idx ON cities USING BTREE (country_id);

ALTER TABLE cities
ADD CONSTRAINT fk_cities_county_id FOREIGN KEY (country_id)
REFERENCES countries (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;
