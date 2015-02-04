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
    imported_dt,
    ext_created_dt,
    ext_updated_dt
  FROM facebook_scraped_posts
  UNION
  SELECT
    id   AS post_id,
    'VK' AS social_network,
    external_id,
    message,
    picture,
    link,
    rental_fee,
    room_count,
    imported_dt,
    ext_created_dt,
    ext_updated_dt
  FROM vk_posts;


DROP VIEW IF EXISTS posts_to_metros_vw;

CREATE OR REPLACE VIEW
  posts_to_metros_vw
AS
  SELECT
    vk_post_id AS post_id,
    'VK'       AS social_network,
    metro_station_id
  FROM vk_posts_to_metros
  UNION
  SELECT
    fb_post_id AS post_id,
    'FB'       AS social_network,
    metro_station_id
  FROM fb_posts_to_metros;