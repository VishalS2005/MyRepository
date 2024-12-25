SELECT 
    code, name, country_code, birth_date
FROM 
    athletes
WHERE 
    country_code = 'CHN' AND disciplines LIKE '%Table Tennis%'
ORDER BY 
    name ASC;