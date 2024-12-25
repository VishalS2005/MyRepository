SELECT 
    code AS athlete_id, name AS athlete_name, country_code, birth_date
FROM 
    athletes
ORDER BY 
    birth_date DESC, athlete_id DESC
LIMIT 20;