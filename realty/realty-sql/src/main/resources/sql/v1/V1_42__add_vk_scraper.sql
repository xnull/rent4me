DROP TABLE IF EXISTS vk_posts;

CREATE TABLE vk_posts (
  id             BIGINT                   NOT NULL PRIMARY KEY,
  external_id    VARCHAR(1024)            NOT NULL UNIQUE,
  message        TEXT,
  picture        TEXT,
  link           TEXT,
  type           VARCHAR(255),
  rental_fee     NUMERIC,
  room_count     INTEGER,
  imported_dt    TIMESTAMP WITH TIME ZONE NOT NULL,
  ext_created_dt TIMESTAMP WITH TIME ZONE NOT NULL,
  ext_updated_dt TIMESTAMP WITH TIME ZONE NOT NULL,
  vk_page_id     BIGINT                   NOT NULL
);

-- CREATE INDEX apartment_deltas$apartment_id__idx ON apartment_deltas USING BTREE (apartment_id);
ALTER TABLE vk_posts
ADD CONSTRAINT fk_vk_page_id FOREIGN KEY (vk_page_id)
REFERENCES vk_page (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

DROP SEQUENCE IF EXISTS vk_post_id_seq;

CREATE SEQUENCE vk_post_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;


-- relation between vk post & metro stations
DROP TABLE IF EXISTS vk_posts_to_metros;
CREATE TABLE vk_posts_to_metros (
  vk_post_id       BIGINT NOT NULL,
  metro_station_id BIGINT NOT NULL,
  UNIQUE (vk_post_id, metro_station_id)
);

CREATE INDEX vk_posts_to_metros$vk_post_id__idx ON vk_posts_to_metros USING BTREE (vk_post_id);
CREATE INDEX vk_posts_to_metros$vk_st_id__idx ON vk_posts_to_metros USING BTREE (metro_station_id);

ALTER TABLE vk_posts_to_metros
ADD CONSTRAINT vk_fb_post_id FOREIGN KEY (vk_post_id)
REFERENCES vk_posts (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE vk_posts_to_metros
ADD CONSTRAINT vk_metro_station_id FOREIGN KEY (metro_station_id)
REFERENCES metro_stations (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

-- indexes

DROP INDEX IF EXISTS vk_posts$vk_post_message__idx;
CREATE INDEX vk_posts$vk_post_message__idx ON vk_posts USING GIN (lower(message) gin_trgm_ops);

DROP INDEX IF EXISTS vk_posts$vk_post_room_cnt__idx;
CREATE INDEX vk_posts$vk_post_room_cnt__idx ON facebook_scraped_posts USING BTREE (room_count);

DROP INDEX IF EXISTS vk_posts$vk_post_rental_fee__idx;
CREATE INDEX vk_posts$vk_post_rental_fee__idx ON vk_posts USING BTREE (rental_fee);

DROP INDEX IF EXISTS vk_posts$vk_post_ext_created_dt__idx;
CREATE INDEX vk_posts$vk_post_ext_created_dt__idx ON vk_posts USING BTREE (ext_created_dt);