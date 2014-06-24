DROP TABLE IF EXISTS realty_users;

CREATE TABLE realty_users (
  id       BIGINT PRIMARY KEY,
  username VARCHAR(50)  NOT NULL,
  password_hash VARCHAR(255) NOT NULL
);

DROP SEQUENCE IF EXISTS realty_users_id_seq;
CREATE SEQUENCE realty_users_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

DROP TABLE IF EXISTS realty_authorities;
CREATE TABLE realty_authorities (
  id        BIGINT PRIMARY KEY,
  authority VARCHAR(50) NOT NULL
);

ALTER TABLE realty_authorities
ADD CONSTRAINT unique_authority UNIQUE (authority);

DROP SEQUENCE IF EXISTS realty_authorities_id_seq;
CREATE SEQUENCE realty_authorities_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

DROP TABLE IF EXISTS realty_users_authorities;

CREATE TABLE realty_users_authorities (
  user_id      BIGINT NOT NULL,
  authority_id BIGINT NOT NULL
);

ALTER TABLE realty_users_authorities
ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE realty_users_authorities
ADD CONSTRAINT fk_authority_id FOREIGN KEY (authority_id)
REFERENCES realty_authorities (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;