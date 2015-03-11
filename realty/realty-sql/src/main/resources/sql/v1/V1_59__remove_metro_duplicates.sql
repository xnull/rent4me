DROP FUNCTION IF EXISTS remove_metro_duplicates();

CREATE OR REPLACE FUNCTION remove_metro_duplicates()
  RETURNS VOID
LANGUAGE plpgsql
AS $$
DECLARE
  cnt INT;
  rec RECORD;
BEGIN
  cnt := 0;

  RAISE NOTICE 'Current cnt: (%)', cnt;

  FOR rec IN (SELECT DISTINCT ON (lower(name))
                id,
                lower(name) AS name
              FROM metro_stations
              WHERE lower(name) IN (
                SELECT name
                FROM (SELECT
                        count(lower(name)) cnt,
                        lower(name) AS     name
                      FROM metro_stations
                      WHERE name IS NOT NULL AND name <> ''
                      GROUP BY lower(name)) tmp
                WHERE tmp.cnt >= 2
              )
              ORDER BY lower(name)) LOOP

    cnt := cnt + 1;
    RAISE NOTICE 'Current cnt: (%)', cnt;

    DELETE FROM metro_stations
    WHERE lower(name) LIKE lower(rec.name) AND id <> rec.id;

  END LOOP;

END
$$;

SELECT remove_metro_duplicates();
DROP FUNCTION IF EXISTS remove_metro_duplicates();