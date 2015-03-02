ALTER TABLE apartments
ALTER COLUMN owner_id DROP NOT NULL;

ALTER TABLE apartments
ALTER COLUMN type_of_rent DROP NOT NULL;

ALTER TABLE apartments
ALTER COLUMN rental_fee DROP NOT NULL;

ALTER TABLE apartments
ALTER COLUMN fee_period DROP NOT NULL;

ALTER TABLE apartments
ADD COLUMN data_source VARCHAR(20);

UPDATE apartments
SET data_source = 'INTERNAL';

ALTER TABLE apartments
ALTER COLUMN data_source SET NOT NULL;