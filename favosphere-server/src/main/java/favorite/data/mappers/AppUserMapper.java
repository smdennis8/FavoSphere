package favorite.data.mappers;

import favorite.security.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AppUserMapper implements RowMapper<AppUser> {
    private final List<String> roles;

    public AppUserMapper(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public AppUser mapRow(ResultSet rs, int i) throws SQLException {
        return new AppUser(
                BigInteger.valueOf(rs.getInt("app_user_id")),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("password_hash"),
                LocalDate.parse(rs.getString("registered_on")),
                LocalDate.parse(rs.getString("last_login")),
                rs.getBoolean("user_enabled"),
                roles);
    }
}
