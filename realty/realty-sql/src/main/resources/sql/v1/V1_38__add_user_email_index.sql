DROP INDEX IF EXISTS realty_users$email_id__idx;
CREATE INDEX realty_users$email_id__idx ON realty_users USING BTREE (lower(email));