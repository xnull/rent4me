CREATE UNIQUE INDEX apartments$unique_external_id_idx ON apartments USING BTREE (external_id, data_source)
  WHERE external_id IS NOT NULL;

CREATE INDEX apartments$description__idx ON apartments USING GIN (lower(description) gin_trgm_ops);
CREATE INDEX apartments$rental_fee__idx ON apartments USING BTREE (rental_fee);
CREATE INDEX apartments$room_count__idx ON apartments USING BTREE (room_count);
CREATE INDEX apartments$system_created_time__idx ON apartments USING BTREE (created_dt);
CREATE INDEX apartments$logical_created_time__idx ON apartments USING BTREE (logical_created_dt);

UPDATE apartments
SET fee_period = 'MONTHLY';