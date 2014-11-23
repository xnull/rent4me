ALTER TABLE apartments
ADD COLUMN description TEXT;

ALTER TABLE apartments
DROP COLUMN short_desc;

ALTER TABLE apartments
DROP COLUMN extended_desc;