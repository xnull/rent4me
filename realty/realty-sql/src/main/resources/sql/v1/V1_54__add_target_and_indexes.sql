ALTER TABLE apartments
ADD COLUMN target VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN';

CREATE INDEX apartments$rental_target__idx ON apartments USING BTREE (target);
CREATE INDEX apartments$rental_ds__idx ON apartments USING BTREE (data_source);

UPDATE apartments
SET target = 'RENTER'
WHERE data_source = 'INTERNAL';