ALTER TABLE apartments
ADD COLUMN description_hash VARCHAR(512);

CREATE INDEX apartments$desc_hash__idx ON apartments USING BTREE (description_hash);