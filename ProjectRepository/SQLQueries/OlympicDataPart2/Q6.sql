WITH apptop5 AS (

WITH countries_amount AS (
    SELECT
        r.date,
         --find the countries AND date AND appearances
        --check for null values
        --ensure proper join functions are used
        COALESCE(a.country_code, t.country_code) AS country_code,
        COUNT(*) AS appearances
    FROM
        results r
    LEFT JOIN 
        athletes a ON r.participant_code = a.code
        --select distinct code and country here very important
    LEFT JOIN 
        (SELECT DISTINCT code, country_code FROM teams) t ON r.participant_code = t.code
    WHERE
        r.rank <= 5 
        AND r.rank IS NOT NULL --first null check
        AND (
            (a.code IS NOT NULL AND r.participant_code = a.code) OR
            (t.code IS NOT NULL AND r.participant_code = t.code)
        )  --second null check
        AND r.discipline_name != 'Table Tennis'
    GROUP BY 
        r.date, COALESCE(a.country_code, t.country_code)  --doing this will ensure that we are getting country_code from right place
    HAVING 
        COUNT(*) > 0
)
SELECT
    c.date,
    c.country_code,
    c.appearances
FROM   
    countries_amount c
ORDER BY
    c.date ASC, c.country_code ASC
)


SELECT
    date, country_code, appearances,
    RANK() OVER (PARTITION BY date ORDER BY appearances DESC, country_code ASC) AS rank_position
FROM
    apptop5;