SELECT 
    code AS country_code, 
    RANK() OVER (ORDER BY population DESC NULLS LAST) AS population_rank,
    RANK() OVER (ORDER BY "GDP ($ per capita)" DESC NULLS LAST) AS gdp_rank
FROM
    countries
GROUP BY
    code
ORDER BY
    code ASC;
--rank countries by population (largest first nulls last)
--rank countries by gdp per capita (nulls last)