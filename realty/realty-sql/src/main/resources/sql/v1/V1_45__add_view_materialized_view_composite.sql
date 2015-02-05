DROP MATERIALIZED VIEW IF EXISTS posts_to_metros_mvw;

CREATE MATERIALIZED VIEW
  posts_to_metros_mvw
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

CREATE INDEX posts_to_metros_mvw$post_id__soc_net_idx ON posts_to_metros_mvw USING BTREE (post_id, social_network);

DROP VIEW IF EXISTS posts_to_metros_vw;

--enhance indexes, hibernate don't know how to work with materialized views
CREATE OR REPLACE VIEW
  posts_to_metros_vw
AS
  SELECT * FROM posts_to_metros_mvw;