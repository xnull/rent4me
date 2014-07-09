DROP TABLE IF EXISTS user_tokens;

CREATE TABLE user_tokens (
  id            BIGINT PRIMARY KEY,
  user_id       BIGINT        NOT NULL,
  token         VARCHAR(1024) NOT NULL,
  created_dt TIMESTAMP WITH TIME ZONE NOT NULL ,
  updated_dt  TIMESTAMP WITH TIME ZONE NOT NULL
);

DROP SEQUENCE IF EXISTS user_tokens_id_seq;
CREATE SEQUENCE user_tokens_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

ALTER TABLE user_tokens
ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;