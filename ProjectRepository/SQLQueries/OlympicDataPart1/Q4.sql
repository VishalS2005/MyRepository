SELECT 
    name, country_code, medal_code, event
FROM 
    athletes, medals
WHERE 
    athletes.code = medals.winner_code AND athletes.country_code = 'USA' AND medals.medal_code = '1'
ORDER BY 
    name ASC; 