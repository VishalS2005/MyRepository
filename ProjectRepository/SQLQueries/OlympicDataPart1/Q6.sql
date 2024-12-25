-- country code from athletes
-- medal count from medals
SELECT
    athletes.country_code,
    SUM(CASE WHEN medals.medal_code = 1 THEN 1 ELSE 0 END) AS gold_medal,
    SUM(CASE WHEN medals.medal_code = 2 THEN 1 ELSE 0 END) AS silver_medal,
    SUM(CASE WHEN medals.medal_code = 3 THEN 1 ELSE 0 END) AS bronze_medal
FROM
    athletes, medals
WHERE
    athletes.code = medals.winner_code 
    AND athletes.country_code = 'USA'
GROUP BY
    athletes.country_code;
