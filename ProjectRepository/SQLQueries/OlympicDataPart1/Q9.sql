WITH individual_shooting AS (
    SELECT 
        participant_code,
        COUNT(DISTINCT event_name) AS event_count
    FROM 
        results
    WHERE 
        participant_type = 'Person' AND discipline_name = 'Shooting'
    GROUP BY 
        participant_code
),
multiple_shooting AS (
    SELECT 
        participant_code
    FROM 
        individual_shooting
    WHERE 
        event_count > 1
)
SELECT 
    ROUND(
        (COUNT(DISTINCT multiple_shooting.participant_code) * 100.0) / COUNT(DISTINCT individual_shooting.participant_code), 2
    ) AS percentage
FROM 
    individual_shooting
LEFT JOIN 
    multiple_shooting ON individual_shooting.participant_code = multiple_shooting.participant_code;

    --ROUND (X, DECIMAL PLACES) AS percentage