DROP TABLE IF EXISTS apartment_photos;

CREATE TABLE apartment_photos (
  id                        BIGINT,
  small_thumbnail_url       TEXT,
  small_thumbnail_object_id TEXT,
  original_image_url        TEXT,
  original_image_object_id  TEXT,
  creation_dtime            TIMESTAMP WITH TIME ZONE,
  guid                      VARCHAR(255),
  author_id                 BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE INDEX apartment_photos$guid__idx ON apartment_photos USING btree(guid);

ALTER TABLE apartment_photos
ADD CONSTRAINT fk_author_id FOREIGN KEY (author_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

DROP SEQUENCE IF EXISTS apartment_photos_id_seq;

CREATE SEQUENCE apartment_photos_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;