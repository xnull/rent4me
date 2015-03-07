DROP FUNCTION IF EXISTS remove_metro_duplicates();

CREATE OR REPLACE FUNCTION remove_metro_duplicates()
  RETURNS VOID
LANGUAGE plpgsql
AS $$
DECLARE
  rec RECORD;
BEGIN

  FOR rec IN (SELECT *
              FROM metro_stations) LOOP

    DELETE FROM metro_stations
    WHERE name = rec.name AND id <> rec.id;

  END LOOP;

END
$$;

SELECT remove_metro_duplicates();
DROP FUNCTION IF EXISTS remove_metro_duplicates();