WITH athlete_medal_counts AS (
    SELECT 
        a.code AS athlete_code, a.name AS athlete_name, a.country_code, COUNT(m.medal_code) AS total_medals
    FROM 
        athletes a
    JOIN 
        medals m ON a.code = m.winner_code
    WHERE 
        m.discipline = 'Swimming'
    GROUP BY 
        a.code, a.name, a.country_code

    UNION ALL

    SELECT 
        a.code AS athlete_code, a.name AS athlete_name, a.country_code, COUNT(m.medal_code) AS total_medals
    FROM 
        athletes a
    JOIN 
        teams t ON a.code = t.athletes_code
    JOIN 
        medals m ON t.code = m.winner_code
    WHERE 
        m.discipline = 'Swimming'
    GROUP BY 
        a.code, a.name, a.country_code
)
SELECT 
    athlete_code, athlete_name, country_code, SUM(total_medals) AS medal_count
FROM 
    athlete_medal_counts
GROUP BY 
    athlete_code, athlete_name, country_code
HAVING 
    SUM(total_medals) >= 3
ORDER BY 
    medal_count DESC, athlete_name ASC;