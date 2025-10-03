-- Insert or update shows for Inception in Calicut (theater_id=3) and Dark Night in Trivandrum (theater_id=2)

-- Inception (movie_id=2) in Calicut Mall Cinema (theater_id=3)
INSERT INTO shows (movie_id, theater_id, show_date, show_time, price_regular, price_premium, price_vip, is_active, created_at)
VALUES (2, 3, CURDATE(), '18:00:00', 200.00, 300.00, 400.00, TRUE, NOW())
ON DUPLICATE KEY UPDATE show_date=VALUES(show_date), show_time=VALUES(show_time), price_regular=VALUES(price_regular), price_premium=VALUES(price_premium), price_vip=VALUES(price_vip), is_active=VALUES(is_active), created_at=VALUES(created_at);

-- Dark Night (movie_id=1) in Trivandrum Grand (theater_id=2)
INSERT INTO shows (movie_id, theater_id, show_date, show_time, price_regular, price_premium, price_vip, is_active, created_at)
VALUES (1, 2, CURDATE(), '20:00:00', 250.00, 350.00, 450.00, TRUE, NOW())
ON DUPLICATE KEY UPDATE show_date=VALUES(show_date), show_time=VALUES(show_time), price_regular=VALUES(price_regular), price_premium=VALUES(price_premium), price_vip=VALUES(price_vip), is_active=VALUES(is_active), created_at=VALUES(created_at);

-- You can run this SQL script in your MySQL database to add or update the shows accordingly.
