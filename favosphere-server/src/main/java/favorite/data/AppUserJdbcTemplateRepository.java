package favorite.data;

import favorite.data.mappers.AppUserMapper;
import favorite.data.mappers.UserMapper;
import favorite.security.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public class AppUserJdbcTemplateRepository implements AppUserRepository{
    private final JdbcTemplate jdbcTemplate;

    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AppUser findByEmail(String email) {
        List<String> roles = getRolesByEmail(email);
        final String sql = """
                select *
                from app_user
                where email = ?;
                """;
        return jdbcTemplate.query(sql, new AppUserMapper(roles), email)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public List<AppUser> findAll() {
        final String sql = """
                select *
                from app_user;
                """;
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    @Transactional
    public AppUser create(AppUser user) {
        final String sql = "insert into `app_user` (first_name, middle_name, last_name, phone, email, password_hash, registered_on, last_login, user_enabled) values (?,?,?,?,?,?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getMiddleName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPassword());
            ps.setDate(7, Date.valueOf(LocalDate.now()));
            ps.setDate(8, Date.valueOf(user.getLastLogin()));
            ps.setBoolean(9, user.isEnabled());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setAppUserId(BigInteger.valueOf(keyHolder.getKey().intValue()));

        updateRoles(user);

        return user;
    }

    private List<String> getRolesByEmail(String email) {
        final String sql = """
                select ar.title
                from app_user_role ur
                inner join app_role ar on ur.app_role_id = ar.app_role_id
                inner join app_user au on ur.app_user_id = au.app_user_id
                where au.email = ?;
                """;
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("title"), email);
    }

    @Transactional
    public boolean update(AppUser user) {
        final String sql = """
                update app_user set
                first_name = ?,
                middle_name = ?,
                last_name = ?,
                phone = ?,
                email = ?,
                password_hash = ?,
                registered_on = ?,
                last_login = ?,
                user_enabled = ?
                where app_user_id = ?;
                """;

        int rowsReturned = jdbcTemplate.update(sql,
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getPhone(),
                user.getEmail(),
                user.getPassword(),
                user.getRegisteredOn(),
                user.getLastLogin(),
                user.isEnabled(),
                user.getAppUserId());

        if (rowsReturned > 0) {
            updateRoles(user);
            return true;
        }

        return false;
    }

    private void updateRoles(AppUser user) {
        // delete all roles, then re-add
        jdbcTemplate.update("delete from app_user_role where app_user_id = ?;", user.getAppUserId());

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (GrantedAuthority role : authorities) {
            String sql = """
                    insert into app_user_role (app_user_id, app_role_id)
                        select ?, app_role_id from app_role where `title` = ?;
                    """;
            jdbcTemplate.update(sql, user.getAppUserId(), role.getAuthority());
        }
    }
}