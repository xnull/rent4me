DROP FUNCTION IF EXISTS calc_daily_fee(in_fee NUMERIC, in_fee_period VARCHAR);

CREATE OR REPLACE FUNCTION calc_daily_fee(in_fee NUMERIC, in_fee_period VARCHAR)
  RETURNS NUMERIC
LANGUAGE plpgsql
IMMUTABLE
AS $$
DECLARE
BEGIN
  RETURN in_fee * (CASE in_fee_period
                   WHEN 'MONTHLY' THEN
                     1
                   WHEN 'WEEKLY' THEN
                     1
                   WHEN 'HOURLY' THEN
                     24
                   WHEN 'DAILY' THEN
                     1
                   ELSE
                     1
                   END) / (CASE in_fee_period
                           WHEN 'MONTHLY' THEN
                             30
                           WHEN 'WEEKLY' THEN
                             7
                           WHEN 'HOURLY' THEN
                             1
                           WHEN 'DAILY' THEN
                             1
                           ELSE
                             1
                           END)

  ;
END
$$;