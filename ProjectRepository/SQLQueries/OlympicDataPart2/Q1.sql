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
    r.participant_code ASC;
