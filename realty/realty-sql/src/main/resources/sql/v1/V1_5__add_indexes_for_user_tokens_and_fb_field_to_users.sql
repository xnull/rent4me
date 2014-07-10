CREATE INDEX user_tokens$user_id__token__idx ON user_tokens USING BTREE (user_id, token);

ALTER TABLE realty_users
    ADD COLUMN facebook_id VARCHAR(1024);

CREATE INDEX realty_users$facebook_id__idx ON realty_users USING BTREE (facebook_id);