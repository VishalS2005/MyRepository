SELECT 
    code,
    "GDP ($ per capita)" * population AS total_gdp,
    ROW_NUMBER() OVER (ORDER BY "GDP ($ per capita)" * population DESC NULLS LAST) AS gdp_rank
FROM 
    countries
ORDER BY
    total_gdp DESC NULLS LAST
LIMIT 10;