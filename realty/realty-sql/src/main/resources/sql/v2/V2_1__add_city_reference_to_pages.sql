ALTER TABLE facebook_page_to_scrap
ADD COLUMN city_id BIGINT;

ALTER TABLE facebook_page_to_scrap
ADD CONSTRAINT fk_city_id FOREIGN KEY (city_id)
REFERENCES cities (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE vk_page
ADD COLUMN city_id BIGINT;

ALTER TABLE vk_page
ADD CONSTRAINT fk_city_id FOREIGN KEY (city_id)
REFERENCES cities (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;