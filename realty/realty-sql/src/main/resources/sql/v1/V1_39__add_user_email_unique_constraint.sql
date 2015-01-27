DROP INDEX IF EXISTS realty_users$email_id__idx;

DROP INDEX IF EXISTS realty_users$email__unique_id__idx;

CREATE UNIQUE INDEX realty_users$email__unique_id__idx ON realty_users USING BTREE (lower(email));