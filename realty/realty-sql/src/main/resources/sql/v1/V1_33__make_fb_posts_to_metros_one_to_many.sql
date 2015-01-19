ALTER TABLE facebook_scraped_posts
DROP COLUMN IF EXISTS metro_station_id;

CREATE TABLE fb_posts_to_metros (
  fb_post_id       BIGINT NOT NULL,
  metro_station_id BIGINT NOT NULL,
  UNIQUE (fb_post_id, metro_station_id)
);

CREATE INDEX fb_posts_to_metros$fb_post_id__idx ON fb_posts_to_metros USING BTREE (fb_post_id);
CREATE INDEX fb_posts_to_metros$metro_st_id__idx ON fb_posts_to_metros USING BTREE (metro_station_id);

ALTER TABLE fb_posts_to_metros
ADD CONSTRAINT fk_fb_post_id FOREIGN KEY (fb_post_id)
REFERENCES facebook_scraped_posts (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE fb_posts_to_metros
ADD CONSTRAINT fk_metro_station_id FOREIGN KEY (metro_station_id)
REFERENCES metro_stations (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;