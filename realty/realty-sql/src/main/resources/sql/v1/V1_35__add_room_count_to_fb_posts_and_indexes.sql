ALTER TABLE facebook_scraped_posts
DROP COLUMN IF EXISTS room_count;

ALTER TABLE facebook_scraped_posts
ADD COLUMN room_count INTEGER;

DROP INDEX IF EXISTS fb_scrapped_posts$fb_post_room_cnt__idx;
CREATE INDEX fb_scrapped_posts$fb_post_room_cnt__idx ON facebook_scraped_posts USING BTREE (room_count);

DROP INDEX IF EXISTS fb_scrapped_posts$fb_post_rental_fee__idx;
CREATE INDEX fb_scrapped_posts$fb_post_rental_fee__idx ON facebook_scraped_posts USING BTREE (rental_fee);
