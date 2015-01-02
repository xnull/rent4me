DROP TABLE IF EXISTS facebook_page_to_scrap;

CREATE TABLE facebook_page_to_scrap (
  id BIGINT NOT NULL PRIMARY KEY,
  external_id VARCHAR(1024) NOT NULL UNIQUE,
  created_dt timestamp with time zone not null,
  updated_dt timestamp with time zone not null
);


DROP SEQUENCE IF EXISTS fb_pg2scrap_id_seq;

CREATE SEQUENCE fb_pg2scrap_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;



DROP TABLE IF EXISTS facebook_scraped_posts;

CREATE TABLE facebook_scraped_posts (
  id BIGINT NOT NULL PRIMARY KEY,
  external_id VARCHAR(1024) NOT NULL UNIQUE,
  message TEXT,
  picture TEXT,
  link TEXT,
  type VARCHAR(255),
  imported_dt timestamp with time zone not null,
  ext_created_dt timestamp with time zone not null,
  ext_updated_dt timestamp with time zone not null,
  fb_pg2_scrap_id BIGINT NOT NULL
);

-- CREATE INDEX apartment_deltas$apartment_id__idx ON apartment_deltas USING BTREE (apartment_id);
ALTER TABLE facebook_scraped_posts
ADD CONSTRAINT fk_fb_pg2_scrap_id FOREIGN KEY (fb_pg2_scrap_id)
REFERENCES facebook_page_to_scrap (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

DROP SEQUENCE IF EXISTS fb_scr_post_id_seq;

CREATE SEQUENCE fb_scr_post_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;



DROP FUNCTION IF EXISTS add_fb_pg2scrap(in_external_id VARCHAR(1024));

CREATE OR REPLACE FUNCTION add_fb_pg2scrap(in_external_id VARCHAR(1024))
  RETURNS VOID
LANGUAGE plpgsql
AS $$
DECLARE
BEGIN
  INSERT INTO facebook_page_to_scrap (id, external_id, created_dt, updated_dt)
  VALUES (nextval('fb_pg2scrap_id_seq'), in_external_id, now(), now());
END
$$;