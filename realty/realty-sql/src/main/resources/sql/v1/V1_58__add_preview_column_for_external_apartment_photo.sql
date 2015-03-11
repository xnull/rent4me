ALTER TABLE apartment_external_photos
ADD COLUMN preview_image_url TEXT;

UPDATE apartment_external_photos
SET preview_image_url = image_url;