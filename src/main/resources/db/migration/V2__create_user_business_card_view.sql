CREATE VIEW user_business_cards AS
SELECT b.id AS id,
       b.user_id AS user_id,
       u.first_name AS first_name,
       u.last_name AS last_name,
       b.email AS email,
       b.phone AS phone,
       b.position AS position,
       b.company AS company,
       b.email_verified AS email_verified
FROM users AS u INNER JOIN (SELECT * FROM business_cards) AS b ON u.id = b.user_id;