DROP TABLE if EXISTS attachments;

CREATE TABLE attachments(
id BIGINT NOT NULL PRIMARY KEY ,
attachment_type VARCHAR ,
photo_id VARCHAR ,
album_id VARCHAR ,
owner_id VARCHAR ,
photo_src VARCHAR,
image_src VARCHAR ,
text VARCHAR ,
description VARCHAR ,
title VARCHAR ,
link_url VARCHAR,
item_id BIGINT,
FOREIGN KEY (item_id) REFERENCES items(id)
);

DROP SEQUENCE IF EXISTS attachment_id_seq;
CREATE SEQUENCE attachment_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
