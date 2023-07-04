use favosphere;
select ar.title
from app_user_role ur
inner join app_role ar on ur.app_role_id = ar.app_role_id
inner join app_user au on ur.app_user_id = au.app_user_id
where au.email = 'john@smith.com';

select app_user_id, email, password_hash, user_enabled
from app_user
where email = 'john@smith.com';


-- insert into `app_user` (first_name, middle_name, last_name, phone, email, password_hash, registered_on, last_login, user_enabled)
--     values
--     ('John', 'First', 'Last', '1-111-111-1111', 'first@last.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', '2010-01-11', '2023-06-26', 1);
    
-- delete from app_user_role where app_user_id = 1;
select * from app_user;


-- insert into app_user_role (app_user_id, app_role_id)
-- select 1,
-- app_role_id from app_role where `title` = 'ADMIN';

-- select *
-- from favorite;

-- select * from email;

select *
from favorite
where app_user_id = 1;