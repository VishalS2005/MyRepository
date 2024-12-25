-- both athlete code and team code are necessary
-- gold medal, silver medal, and bronze medal counted as sum
-- ordered by gold, silver, and bronze
-- limit 10
SELECT 
    country_code,
    SUM(gold_medal) AS gold_medal,
    SUM(silver_medal) AS silver_medal,
    SUM(bronze_medal) AS bronze_medal
FROM (
    SELECT
        athletes.country_code,
        COUNT(CASE WHEN medals.medal_code = 1 THEN 1 ELSE NULL END) AS gold_medal,
        COUNT(CASE WHEN medals.medal_code = 2 THEN 1 ELSE NULL END) AS silver_medal,
        COUNT(CASE WHEN medals.medal_code = 3 THEN 1 ELSE NULL END) AS bronze_medal
    FROM
        athletes, medals
    WHERE 
        athletes.code = medals.winner_code
    GROUP BY
        athletes.country_code

    UNION ALL

    SELECT
        teams.country_code,
        COUNT(DISTINCT CASE WHEN medals.medal_code = 1 THEN medals.winner_code ELSE NULL END) AS gold_medal,
        COUNT(DISTINCT CASE WHEN medals.medal_code = 2 THEN medals.winner_code ELSE NULL END) AS silver_medal,
        COUNT(DISTINCT CASE WHEN medals.medal_code = 3 THEN medals.winner_code ELSE NULL END) AS bronze_medal
    FROM
        teams, medals
    WHERE
        teams.code = medals.winner_code
    GROUP BY
        teams.country_code
) AS combined
GROUP BY
    country_code
ORDER BY 
    SUM(gold_medal) DESC, SUM(silver_medal) DESC, SUM(bronze_medal) DESC
LIMIT 10;
