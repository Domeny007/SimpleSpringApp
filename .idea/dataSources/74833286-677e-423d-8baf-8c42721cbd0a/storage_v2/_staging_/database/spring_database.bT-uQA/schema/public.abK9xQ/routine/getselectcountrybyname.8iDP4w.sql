create function getselectcountrybyname(text) returns SETOF countries
LANGUAGE SQL
AS $$
SELECT *
FROM countries
WHERE LOWER(name) SIMILAR TO (LOWER($1) || '%')
ORDER BY name DESC LIMIT 10 ;
$$;

SELECT * FROM getselectcountrybyname('РОССИЯ');

