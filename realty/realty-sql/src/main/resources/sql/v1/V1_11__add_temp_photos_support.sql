DROP TABLE IF EXISTS photos_temp;

CREATE TABLE photos_temp (
  id BIGINT NOT NULL PRIMARY KEY,
  url TEXT NOT NULL,
  guid varchar(255) not null,
  created timestamp with time zone not null,
  author_id BIGINT NOT NULL
);


DROP SEQUENCE IF EXISTS photos_temp_seq;

CREATE SEQUENCE photos_temp_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE INDEX photos_temp$guid__idx ON photos_temp USING btree(guid);
CREATE INDEX photos_temp$created__idx ON photos_temp USING btree(created);

ALTER TABLE photos_temp
ADD CONSTRAINT fk_author_id FOREIGN KEY (author_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;