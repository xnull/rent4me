DROP TABLE IF EXISTS chat_messages;

CREATE TABLE chat_messages (
  id          BIGINT                   NOT NULL PRIMARY KEY,
  sender_id   BIGINT                   NOT NULL,
  receiver_id BIGINT                   NOT NULL,
  message     TEXT                     NOT NULL,
  chat_key    VARCHAR(255)             NOT NULL,
  created_dt  TIMESTAMP WITH TIME ZONE NOT NULL
);


DROP SEQUENCE IF EXISTS chat_msgs_id_seq;

CREATE SEQUENCE chat_msgs_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE INDEX chat_messages$chat_key__idx ON chat_messages USING BTREE (chat_key);
CREATE INDEX chat_messages$sender_id__idx ON chat_messages USING BTREE (sender_id);
CREATE INDEX chat_messages$receiver_id__idx ON chat_messages USING BTREE (receiver_id);

ALTER TABLE chat_messages
ADD CONSTRAINT fk_sender_id FOREIGN KEY (sender_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE chat_messages
ADD CONSTRAINT fk_receiver_id FOREIGN KEY (receiver_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;