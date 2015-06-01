DROP INDEX IF EXISTS apartments$country_code_idx;
CREATE INDEX apartments$country_code_idx ON apartments USING BTREE (country_code);