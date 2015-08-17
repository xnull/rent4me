DROP TABLE IF EXISTS apartment_ident;

CREATE TABLE apartment_ident (
  id      BIGSERIAL NOT NULL,
  apartment_id BIGINT NOT NULL REFERENCES apartments MATCH SIMPLE,
  ident_id BIGINT NOT NULL REFERENCES ident MATCH SIMPLE,
  PRIMARY KEY (id),
  unique (apartment_id, ident_id)
);

CREATE INDEX ON apartment_ident(apartment_id);