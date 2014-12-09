DROP TABLE IF EXISTS apartment_location_deltas;

DROP SEQUENCE IF EXISTS ap_loc_delta_id_seq;


DROP TABLE IF EXISTS apartment_location_deltas;

CREATE TABLE apartment_deltas (
  id                BIGINT,
  location          GEOMETRY(POINT, 4326),
  created_dt        TIMESTAMP WITH TIME ZONE,
  updated_dt        TIMESTAMP WITH TIME ZONE,
  apartment_id      BIGINT NOT NULL,
-- address info
  formatted_address TEXT,
  street_address    VARCHAR(255),
  zip_code          VARCHAR(255),
  district          VARCHAR(255),
  city              VARCHAR(255),
  county            VARCHAR(255),
  country           VARCHAR(255),
  country_code      VARCHAR(5),
  --is delta applied or not
  applied           BOOL   NOT NULL DEFAULT FALSE,

  room_count        INTEGER,
  floor_number      INTEGER,
  floors_total      INTEGER,
  area              NUMERIC,

  PRIMARY KEY (id)
);

ALTER TABLE apartment_deltas
ADD CONSTRAINT fk_apartment_id FOREIGN KEY (apartment_id)
REFERENCES apartments (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

CREATE INDEX apartment_deltas$apartment_id__idx ON apartment_deltas USING BTREE (apartment_id);

DROP SEQUENCE IF EXISTS apt_deltas_id_seq;
CREATE SEQUENCE apt_deltas_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;