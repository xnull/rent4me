DROP INDEX IF EXISTS fb_scrapped_posts$fb_post_id__idx;
CREATE INDEX fb_scrapped_posts$fb_post_id__idx ON facebook_scraped_posts USING GIN (lower(message) gin_trgm_ops);