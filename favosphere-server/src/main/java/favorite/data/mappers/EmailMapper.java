package favorite.data.mappers;

import favorite.models.Email;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailMapper implements RowMapper<Email> {

    @Override
    public Email mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Email(
                BigInteger.valueOf(rs.getInt("email_id")),
                BigInteger.valueOf(rs.getInt("app_user_id")),
                rs.getString("url"),
                LocalDateTime.parse(rs.getString("sent_on"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}