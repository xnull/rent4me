DROP TABLE IF EXISTS notifications;
CREATE TABLE notifications (
  id BIGINT NOT NULL,
  sender_id BIGINT,
  receiver_id BIGINT NOT NULL,
  resolved BOOLEAN NOT NULL,
  created_dt TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_dt TIMESTAMP WITH TIME ZONE NOT NULL,
  type INT NOT NULL,
  PRIMARY KEY (id)
);

DROP SEQUENCE IF EXISTS notification_id_seq;
CREATE SEQUENCE notification_id_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;


ALTER TABLE notifications
ADD CONSTRAINT fk_sender_id FOREIGN KEY (sender_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE notifications
ADD CONSTRAINT fk_receiver_id FOREIGN KEY (receiver_id)
REFERENCES realty_users (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

CREATE INDEX notifications$sender_id ON notifications USING BTREE(sender_id);
CREATE INDEX notifications$receiver_id ON notifications USING BTREE(receiver_id);
CREATE INDEX notifications$resolved ON notifications USING BTREE(resolved);
CREATE INDEX notifications$created ON notifications USING BTREE(created_dt);


ALTER TABLE notifications
    ADD COLUMN chat_message_id BIGINT;

ALTER TABLE notifications
ADD CONSTRAINT fk_chat_message_id FOREIGN KEY (chat_message_id)
REFERENCES chat_messages (id) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE CASCADE;

CREATE INDEX notifications$chat_message_id ON notifications USING BTREE(chat_message_id);