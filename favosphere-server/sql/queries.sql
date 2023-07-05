use favosphere;
select ar.title
from app_user_role ur
inner join app_role ar on ur.app_role_id = ar.app_role_id
inner join app_user au on ur.app_user_id = au.app_user_id
where au.email = 'john@smith.com';

select app_user_id, email, password_hash, user_enabled
from app_user
where email = 'john@smith.com';

select * from app_user;

select *
from favorite;