DROP TABLE IF EXISTS blacklist;
DROP TABLE IF EXISTS ident;
DROP TABLE IF EXISTS id_relations;

CREATE TABLE ident (
  id      BIGSERIAL NOT NULL,
  value   TEXT,
  -- USER_ID, FB_ID, VK_ID, PHONE, EMAIL, APARTMENT
  id_type TEXT,
  PRIMARY KEY (id)
);

CREATE INDEX ident$value ON ident USING BTREE (value);

CREATE TABLE id_relations (
  id        BIGSERIAL NOT NULL,
  source_id BIGINT NOT NULL REFERENCES ident MATCH SIMPLE,
  adjacent_id BIGINT NOT NULL REFERENCES ident MATCH SIMPLE,
  PRIMARY KEY (id)
);

CREATE TABLE blacklist (
  id     BIGSERIAL NOT NULL,
  ident_id BIGINT NOT NULL REFERENCES ident,
  PRIMARY KEY (id)
);

CREATE INDEX blacklist$ident_id ON blacklist USING BTREE (ident_id);