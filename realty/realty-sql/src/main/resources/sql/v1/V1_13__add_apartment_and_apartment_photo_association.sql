DROP TABLE IF EXISTS apartment_apartment_photos;

CREATE TABLE apartment_apartment_photos (
  apartment_id                        BIGINT,
  apartment_photo_id                        BIGINT,
  unique (apartment_id, apartment_photo_id)
);

CREATE INDEX apartment_apartment_photos$ap_id__idx ON apartment_apartment_photos USING btree(apartment_id);
CREATE INDEX apartment_apartment_photos$ap_photo_id__idx ON apartment_apartment_photos USING btree(apartment_photo_id);

ALTER TABLE apartment_apartment_photos
ADD CONSTRAINT fk_apartment_id FOREIGN KEY (apartment_id)
REFERENCES apartments (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE apartment_apartment_photos
ADD CONSTRAINT fk_apartment_photo_id FOREIGN KEY (apartment_photo_id)
REFERENCES apartment_photos (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;