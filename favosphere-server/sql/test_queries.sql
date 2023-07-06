use favosphere_test;
set sql_safe_updates = 0;
call set_known_good_state();

select *
from app_user;