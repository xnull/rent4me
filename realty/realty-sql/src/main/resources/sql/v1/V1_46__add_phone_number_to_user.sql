ALTER TABLE realty_users
ADD COLUMN national_formatted_number TEXT;

ALTER TABLE facebook_scraped_posts
ADD COLUMN phone_number TEXT;
ALTER TABLE facebook_scraped_posts
ADD COLUMN national_formatted_number TEXT;


ALTER TABLE vk_posts
ADD COLUMN phone_number TEXT;
ALTER TABLE vk_posts
ADD COLUMN national_formatted_number TEXT;

DROP VIEW IF EXISTS social_net_posts_vw;

CREATE OR REPLACE VIEW
  social_net_posts_vw
AS
  SELECT
    id   AS post_id,
    'FB' AS social_network,
    external_id,
    message,
    picture,
    link,
    rental_fee,
    room_count,
    phone_number,
    national_formatted_number,
    imported_dt,
    ext_created_dt,
    ext_updated_dt
  FROM facebook_scraped_posts
  UNION ALL
  SELECT
    id   AS post_id,
    'VK' AS social_network,
    external_id,
    message,
    picture,
    link,
    rental_fee,
    room_count,
    phone_number,
    national_formatted_number,
    imported_dt,
    ext_created_dt,
    ext_updated_dt
  FROM vk_posts;