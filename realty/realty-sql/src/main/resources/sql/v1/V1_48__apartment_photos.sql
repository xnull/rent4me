DROP TABLE IF EXISTS apartment_external_photos;

CREATE TABLE apartment_external_photos (
  id           BIGINT NOT NULL,
  image_url    TEXT   NOT NULL,
  apartment_id BIGINT NOT NULL,
  created_dt   TIMESTAMP WITH TIME ZONE,
  updated_dt   TIMESTAMP WITH TIME ZONE,
  PRIMARY KEY (id)
);

DROP SEQUENCE IF EXISTS ext_apt_photo_id_seq;
CREATE SEQUENCE ext_apt_photo_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

ALTER TABLE apartment_external_photos
ADD CONSTRAINT fk_apartment_id FOREIGN KEY (apartment_id)
REFERENCES apartments (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

DROP TABLE IF EXISTS apartments_metros;
CREATE TABLE apartments_metros (
  apartment_id     BIGINT NOT NULL,
  metro_station_id BIGINT NOT NULL,
  UNIQUE (apartment_id, metro_station_id)
);

CREATE INDEX apartments_metros$apt_id__idx ON apartments_metros USING BTREE (apartment_id);
CREATE INDEX apartments_metros$metro_st_id__idx ON apartments_metros USING BTREE (metro_station_id);

ALTER TABLE apartments_metros
ADD CONSTRAINT fk_apartment_id FOREIGN KEY (apartment_id)
REFERENCES apartments (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE apartments_metros
ADD CONSTRAINT metro_stations FOREIGN KEY (metro_station_id)
REFERENCES metro_stations (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;