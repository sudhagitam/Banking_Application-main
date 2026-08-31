select * from banking_app.bank_accounts;
update banking_app.bank_accounts set balance=3000 where account_number=1629177988
select * from banking_app.transaction;
select * from banking_app.user
$2a$10$jR.YLdHt4Xh5Z45iLlsRw..m0I6yI3eUpZdHfQHJLFRGiuAg2lXpu

SET SQL_SAFE_UPDATES = 0;
update banking_app.user 
set password='$2a$10$hwHldtv96UnSgOXM80w2U'
where password='$2a$10$hwHldtv96UnSgOXM80w2U..quCaFrFNjEd96YkevleSX1qc8/qPta'


SET SQL_SAFE_UPDATES = 0;

UPDATE banking_app.user 
SET password = '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe12.7.g/9d5d5N5d5N5d5N5d5N5d5N5d'
WHERE email = 'testuser_741301@example.com';

SET SQL_SAFE_UPDATES = 1;

SET SQL_SAFE_UPDATES = 0;
-- Hash below corresponds to plain-text password: password123
UPDATE banking_app.user 
SET password = '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe12.7.g/9d5d5N5d5N5d5N5d5N5d5N5d'
WHERE email = 'testuser_741301@example.com';

SET SQL_SAFE_UPDATES = 1;




SET SQL_SAFE_UPDATES = 0;

UPDATE banking_app.user 
SET password = '$2a$10$e0MYzXyjpJS7Pd0RVvHwHe12.7.g/9d5d5N5d5N5d5N5d5N5d5N5d'
WHERE email = 'phase1test@example.com';

SET SQL_SAFE_UPDATES = 1;


SET SQL_SAFE_UPDATES = 0;

UPDATE banking_app.user 
SET password = '$2a$10$8.UnVuG9HHgffUDAlk8qfOUVGkq8zgVymGe07xd00DMxs.AQubh4a' 
WHERE email = 'phase1test@example.com';

SET SQL_SAFE_UPDATES = 1;