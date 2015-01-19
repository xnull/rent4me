ALTER TABLE facebook_scraped_posts
DROP COLUMN IF EXISTS metro_station_id;

ALTER TABLE facebook_scraped_posts
ADD COLUMN metro_station_id BIGINT REFERENCES metro_stations (id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE facebook_scraped_posts
DROP COLUMN IF EXISTS rental_fee;

ALTER TABLE facebook_scraped_posts
ADD COLUMN rental_fee NUMERIC;

