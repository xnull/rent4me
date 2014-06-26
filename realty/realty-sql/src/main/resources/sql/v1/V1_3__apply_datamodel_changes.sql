ALTER TABLE apartments
DROP COLUMN address;


ALTER TABLE apartments
ADD COLUMN street_address VARCHAR(255);

ALTER TABLE apartments
ADD COLUMN district VARCHAR(255);

ALTER TABLE apartments
ADD COLUMN county VARCHAR(255);

ALTER TABLE apartments
ADD COLUMN country VARCHAR(255);

ALTER TABLE apartments
ADD COLUMN zip_code VARCHAR(255);


ALTER TABLE apartments
ADD CONSTRAINT fk_owner_id FOREIGN KEY (owner_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE apartments
ADD COLUMN type_of_rent VARCHAR(255) NOT NULL;

ALTER TABLE apartments
ADD COLUMN rental_fee NUMERIC NOT NULL;

ALTER TABLE apartments
ADD COLUMN fee_period VARCHAR(255) NOT NULL;

ALTER TABLE apartments
ADD COLUMN short_desc TEXT;

ALTER TABLE apartments
ADD COLUMN extended_desc TEXT;

ALTER TABLE realty_users
ADD COLUMN email VARCHAR(1024) NOT NULL;

ALTER TABLE realty_users
ADD COLUMN phone_number VARCHAR(1024);

ALTER TABLE realty_users
ADD COLUMN first_name VARCHAR(1024);

ALTER TABLE realty_users
ADD COLUMN last_name VARCHAR(1024);

ALTER TABLE realty_users
ADD COLUMN age INTEGER;

DROP SEQUENCE IF EXISTS rental_history_id_seq;
CREATE SEQUENCE rental_history_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

DROP TABLE IF EXISTS rental_histories;
CREATE TABLE rental_histories (
  id           BIGINT NOT NULL PRIMARY KEY,
  apartment_id BIGINT NOT NULL,
  rentee_id    BIGINT NOT NULL,
  owner_id     BIGINT NOT NULL,
  rent_start   DATE,
  rent_end     DATE,
  created      TIMESTAMP WITH TIME ZONE,
  updated      TIMESTAMP WITH TIME ZONE
);

ALTER TABLE rental_histories
ADD CONSTRAINT fk_apartment_id FOREIGN KEY (apartment_id)
REFERENCES apartments (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE rental_histories
ADD CONSTRAINT fk_rentee_id FOREIGN KEY (rentee_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE rental_histories
ADD CONSTRAINT fk_owner_id FOREIGN KEY (owner_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

DROP VIEW IF EXISTS rental_histories_vw;
CREATE OR REPLACE VIEW rental_histories_vw
AS
  (
    SELECT
      rh.rentee_id AS user_id,
      rh.id        AS rental_history_id
    FROM rental_histories rh
    UNION
    SELECT
      rh.owner_id AS user_id,
      rh.id       AS rental_history_id
    FROM rental_histories rh
  );


DROP SEQUENCE IF EXISTS user_comment_id_seq;
CREATE SEQUENCE user_comment_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

DROP TABLE IF EXISTS user_comments;
CREATE TABLE user_comments (
  id                BIGINT NOT NULL PRIMARY KEY,
  created           TIMESTAMP WITH TIME ZONE,
  updated           TIMESTAMP WITH TIME ZONE,
  text              TEXT,
  author_id         BIGINT NOT NULL,
  commented_user_id BIGINT NOT NULL
);

ALTER TABLE user_comments
ADD CONSTRAINT fk_author_id FOREIGN KEY (author_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE user_comments
ADD CONSTRAINT fk_commented_user_id FOREIGN KEY (commented_user_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;


DROP SEQUENCE IF EXISTS apartment_comment_id_seq;
CREATE SEQUENCE apartment_comment_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

DROP TABLE IF EXISTS apartment_comments;
CREATE TABLE apartment_comments (
  id                     BIGINT NOT NULL PRIMARY KEY,
  created                TIMESTAMP WITH TIME ZONE,
  updated                TIMESTAMP WITH TIME ZONE,
  text                   TEXT,
  author_id              BIGINT NOT NULL,
  commented_apartment_id BIGINT NOT NULL
);

ALTER TABLE apartment_comments
ADD CONSTRAINT fk_author_id FOREIGN KEY (author_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE apartment_comments
ADD CONSTRAINT fk_commented_apartment_id FOREIGN KEY (commented_apartment_id)
REFERENCES apartments (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;