ALTER TABLE apartments
ADD COLUMN external_id TEXT;

DROP TABLE IF EXISTS contacts;

CREATE TABLE contacts (
  id                        BIGINT                   NOT NULL,
  type                      VARCHAR(20)              NOT NULL,
  phone_number              TEXT                     NOT NULL,
  national_formatted_number TEXT                     NOT NULL,
  created_dt                TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_dt                TIMESTAMP WITH TIME ZONE NOT NULL,
  PRIMARY KEY (id)
);

DROP SEQUENCE IF EXISTS contact_id_seq;
CREATE SEQUENCE contact_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

DROP TABLE IF EXISTS apartments_contacts;
CREATE TABLE apartments_contacts (
  apartment_id BIGINT NOT NULL,
  contact_id   BIGINT NOT NULL,
  UNIQUE (apartment_id, contact_id)
);

CREATE INDEX apartments_contacts$apt_id__idx ON apartments_contacts USING BTREE (apartment_id);
CREATE INDEX apartments_contacts$contact_id__idx ON apartments_contacts USING BTREE (contact_id);

ALTER TABLE apartments_contacts
ADD CONSTRAINT fk_apartment_id FOREIGN KEY (apartment_id)
REFERENCES apartments (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE apartments_contacts
ADD CONSTRAINT fk_contact_id FOREIGN KEY (contact_id)
REFERENCES contacts (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE apartments
ADD COLUMN ext_link TEXT;

ALTER TABLE apartments
ADD COLUMN logical_created_dt TIMESTAMP WITH TIME ZONE;

UPDATE apartments
SET logical_created_dt = created_dt;

ALTER TABLE apartments
ALTER COLUMN logical_created_dt SET NOT NULL;
