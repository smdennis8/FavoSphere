package favorite.data;

import favorite.data.mappers.EmailMapper;
import favorite.models.Email;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;

@Repository
public class EmailJdbcTemplateRepository implements EmailRepository{

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Email> rowMapper = new EmailMapper();

    public EmailJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Email> findAll() {
        String sql = """
                select *
                from email;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Email findById(BigInteger emailId) {
        String sql = """
                select *
                from email
                where email_id = ?;
                """;
        return jdbcTemplate.query(sql, rowMapper, emailId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Email> findByUserId(BigInteger emailId) {
        return null;
    }

    @Override
    public Email create(Email email) {
        final String sql = "insert into email (app_user_id, url, sent_on) values (?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, (email.getUserId()).intValue());
            ps.setString(2, email.getUrl());
            java.util.Date date = java.util.Date
                    .from(email.getTime().atZone(ZoneId.systemDefault())
                            .toInstant());
            ps.setDate(3, new java.sql.Date(date.getTime()));
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        email.setEmailId(BigInteger.valueOf(keyHolder.getKey().intValue()));
        return email;
    }

    @Override
    public boolean deleteById(BigInteger emailId) {
        return jdbcTemplate.update("delete from email where email_id = ?;", emailId) > 0;
    }
}
