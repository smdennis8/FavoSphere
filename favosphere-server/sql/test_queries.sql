use favosphere_test;
set sql_safe_updates = 0;
call set_known_good_state();
-- select *
-- from app_user_role ur
-- inner join app_role ar on ur.app_role_id = ar.app_role_id
-- inner join app_user au on ur.app_user_id = au.app_user_id
-- where au.email = 'john@smith.com';

select *
from app_user;
-- -- where email = "jjjackson@jmail.jom";
-- set sql_safe_updates = 1;

select *
from favorite;

-- select au.email, au.app_user_id, ar.title
-- from app_user_role ur
-- inner join app_role ar on ur.app_role_id = ar.app_role_id
-- inner join app_user au on ur.app_user_id = au.app_user_id;
-- -- inner join app_user au on ur.app_user_id = au.app_user_id
-- -- where au.email = "john@smith.com";