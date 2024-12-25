SELECT DISTINCT 
    code, country_code, medal_code, event
FROM 
    teams, medals
WHERE 
    teams.code = medals.winner_code AND teams.country_code = 'USA' AND medals.medal_code = '1'
ORDER BY 
    teams.code ASC; 
