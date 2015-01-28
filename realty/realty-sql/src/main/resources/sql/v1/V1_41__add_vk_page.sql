-- clean up old pl/pgsql crap
DROP FUNCTION IF EXISTS add_fb_pg2scrap(in_external_id VARCHAR(1024) );

DROP TABLE IF EXISTS vk_page;

CREATE TABLE vk_page (
  id          BIGINT                   NOT NULL PRIMARY KEY,
  external_id VARCHAR(1024)            NOT NULL UNIQUE,
  link        TEXT,
  created_dt  TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_dt  TIMESTAMP WITH TIME ZONE NOT NULL
);


DROP SEQUENCE IF EXISTS vk_page_id_seq;

CREATE SEQUENCE vk_page_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
