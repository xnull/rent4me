DROP FUNCTION IF EXISTS remove_apartment_duplicates();

CREATE OR REPLACE FUNCTION remove_apartment_duplicates()
  RETURNS VOID
LANGUAGE plpgsql
AS $$
DECLARE
  cnt INT;
  rec RECORD;
BEGIN
  cnt := 0;

  RAISE NOTICE 'Current cnt: (%)', cnt;

  FOR rec IN (SELECT DISTINCT ON (id, description)
                id,
                description
              FROM apartments
              WHERE description IN (
                SELECT description
                FROM (SELECT
                        count(description) cnt,
                        description
                      FROM apartments
                      WHERE description IS NOT NULL AND description <> ''
                      GROUP BY description) tmp
                WHERE tmp.cnt > 2
              )
              ORDER BY id DESC, description) LOOP

    cnt := cnt + 1;
    RAISE NOTICE 'Current cnt: (%)', cnt;

    DELETE FROM apartments
    WHERE lower(description) LIKE lower(rec.description) AND id <> rec.id;

  END LOOP;

END
$$;

SELECT remove_apartment_duplicates();
DROP FUNCTION IF EXISTS remove_apartment_duplicates();