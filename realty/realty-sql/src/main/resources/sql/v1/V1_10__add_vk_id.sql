ALTER TABLE realty_users
ADD COLUMN vkontakte_id VARCHAR(1024);

CREATE INDEX realty_users$vkontakte_id__idx ON realty_users USING BTREE (vkontakte_id);