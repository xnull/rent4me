DROP TABLE IF EXISTS blacklist;
DROP TABLE IF EXISTS ident;
DROP TABLE IF EXISTS id_relations;

CREATE TABLE ident (
  id      BIGSERIAL NOT NULL,
  ident_value   TEXT NOT NULL,
  -- USER_ID, FB_ID, VK_ID, PHONE, EMAIL, APARTMENT
  id_type TEXT,
  PRIMARY KEY (id),
  unique (ident_value, id_type)
);

CREATE TABLE id_relations (
  id        BIGSERIAL NOT NULL,
  source_id BIGINT NOT NULL REFERENCES ident MATCH SIMPLE,
  adjacent_id BIGINT NOT NULL REFERENCES ident MATCH SIMPLE,
  PRIMARY KEY (id),
  unique (source_id, adjacent_id)
);

CREATE TABLE blacklist (
  id     BIGSERIAL NOT NULL,
  ident_id BIGINT NOT NULL UNIQUE REFERENCES ident,
  PRIMARY KEY (id)
);