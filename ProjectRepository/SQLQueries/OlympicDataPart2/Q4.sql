WITH table_tennis_final AS (

WITH table_tennis_total AS (

WITH table_tennis AS (
    SELECT 
    r.participant_code, 
    r.event_name, 
    r.result,
    CASE 
        WHEN r.event_name = 'Men''s Team' AND r.result = '3' THEN 1
        WHEN r.event_name = 'Women''s Team' AND r.result = '3' THEN 1
        WHEN r.event_name = 'Women''s Singles' AND r.result = '4' THEN 1
        WHEN r.event_name = 'Men''s Singles' AND r.result = '4' THEN 1
        WHEN r.event_name = 'Mixed Doubles' AND r.result = '4' THEN 1
        ELSE 0
    END AS win_count
FROM    
    results r
WHERE
    discipline_name = 'Table Tennis'
ORDER BY 
    r.participant_code ASC
)

SELECT  
    t.participant_code, 
    t.event_name, 
    COUNT(CASE WHEN t.win_count = 1 THEN 1 END) AS total_wins
FROM 
    table_tennis t  
GROUP BY 
    t.participant_code, t.event_name
ORDER BY 
    t.participant_code ASC

)

SELECT 
    x.participant_code, 
    x.event_name, 
    x.total_wins, 
    RANK() OVER (PARTITION BY x.event_name ORDER BY x.total_wins DESC) AS rank_position
FROM 
    table_tennis_total x
ORDER BY 
    rank_position ASC, x.participant_code ASC

)

SELECT 
    country_code, 
    COUNT(DISTINCT participant_code) AS top_four_count
FROM (
    SELECT  
        COALESCE(a.country_code, teams.country_code) AS country_code,
        f.participant_code
    FROM
        table_tennis_final f
    LEFT JOIN
        athletes a ON f.participant_code = a.code
    LEFT JOIN   
        teams ON f.participant_code = teams.code
    WHERE 
        f.rank_position <= 4
)   
GROUP BY 
    country_code  -- Group by country_code from the teams table
ORDER BY 
    top_four_count DESC, country_code ASC;  -- Sort by the count of top-four finishes and country_code







