ALTER TABLE realty_users
DROP COLUMN IF EXISTS vk_access_token;


ALTER TABLE realty_users
ADD COLUMN vk_access_token TEXT;


ALTER TABLE realty_users
DROP COLUMN IF EXISTS vk_access_token_expiration;

ALTER TABLE realty_users
ADD COLUMN vk_access_token_expiration TIMESTAMP WITH TIME ZONE;

