DROP INDEX IF EXISTS apartments$published__idx;
CREATE INDEX apartments$published__idx ON apartments USING BTREE (published);

DROP INDEX IF EXISTS apartments$location__idx;
CREATE INDEX apartments$location__idx ON apartments USING GIST (location)
  WHERE published = TRUE;