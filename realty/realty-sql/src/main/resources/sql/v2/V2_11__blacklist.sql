DROP TABLE IF EXISTS blacklist;

DROP TABLE IF EXISTS identification;

--It would contain one of possible id that may be used to auth in service or could be used to identification someone
CREATE TABLE identification (
  id           BIGINT NOT NULL,
  user_id      BIGINT,
  facebook_id  BIGINT,
  vkontakte_id BIGINT,
  phone_number BIGINT,
  email        TEXT,
  PRIMARY KEY (id)
);

CREATE INDEX identification$user_id ON identification USING BTREE (user_id);
CREATE INDEX identification$facebook_id ON identification USING BTREE (facebook_id);
CREATE INDEX identification$vkontakte_id ON identification USING BTREE (vkontakte_id);
CREATE INDEX identification$phone_number ON identification USING BTREE (phone_number);


CREATE TABLE blacklist (
  id                BIGINT NOT NULL,
  identification_id BIGINT,
  apartment_id      BIGINT,
  PRIMARY KEY (id)
);

ALTER TABLE blacklist
ADD CONSTRAINT blacklist_fk_identification_id FOREIGN KEY (identification_id)
REFERENCES identification (id) MATCH SIMPLE;

ALTER TABLE blacklist
ADD CONSTRAINT blacklist_fk_apartment_id FOREIGN KEY (apartment_id)
REFERENCES apartments (id) MATCH SIMPLE;

CREATE INDEX blacklist$identification_id ON blacklist USING BTREE (identification_id);