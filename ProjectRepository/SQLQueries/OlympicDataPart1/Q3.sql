SELECT 
    country_code, gold_medal, silver_medal
FROM 
    tokyo_medals
WHERE 
    gold_medal > silver_medal
ORDER BY 
    gold_medal DESC, silver_medal DESC, country_code ASC;