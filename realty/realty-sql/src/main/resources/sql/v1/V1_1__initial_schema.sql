DROP TABLE IF EXISTS apartments;

CREATE TABLE apartments (
  id         BIGINT NOT NULL,
  address    VARCHAR(255),
  city       VARCHAR(255),
  created_dt TIMESTAMP,
  location   geometry (POINT, 4326),
  updated_dt TIMESTAMP,
  PRIMARY KEY (id)
);

DROP SEQUENCE IF EXISTS apartment_id_seq;
CREATE SEQUENCE apartment_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
