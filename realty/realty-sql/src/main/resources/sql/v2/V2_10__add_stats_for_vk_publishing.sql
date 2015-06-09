DROP TABLE IF EXISTS vk_publishing_events;

CREATE TABLE vk_publishing_events (
  id             BYTEA,
  target_group   TEXT,
  used_token     TEXT,
  text_published TEXT,
  data_source    TEXT NOT NULL,
  created_dt     TIMESTAMP WITH TIME ZONE,
  PRIMARY KEY (id)
);