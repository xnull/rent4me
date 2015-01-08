DROP TABLE IF EXISTS metro_stations;

CREATE TABLE metro_stations (
  id          BIGINT                   NOT NULL PRIMARY KEY,
  city_id     BIGINT                   NOT NULL,
  name        TEXT                     NOT NULL,
  location   geometry (POINT, 4326)
);


DROP SEQUENCE IF EXISTS metro_stations_id_seq;

CREATE SEQUENCE metro_stations_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE INDEX metro_stations$name__idx ON metro_stations USING BTREE (name);
CREATE INDEX metro_stations$country_id__idx ON metro_stations USING BTREE (city_id);

ALTER TABLE metro_stations
ADD CONSTRAINT fk_metro_stations_city_id FOREIGN KEY (city_id)
REFERENCES cities (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;
