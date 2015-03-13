ALTER TABLE cities
ADD COLUMN area BOX2D;

UPDATE cities
SET area = st_setsrid(
    st_makebox2d(
        ST_GeomFromText(
            concat('SRID=4326;POINT(',
                   37.319328799999994,
                   ' ',
                   55.48992699999999,
                   ')')
        ),
        ST_GeomFromText(
            concat('SRID=4326;POINT(',
                   37.94566110000005,
                   ' ',
                   56.009657,
                   ')'))
    ),
    4326)
WHERE name = 'Москва';