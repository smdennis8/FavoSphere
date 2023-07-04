package favorite.data;

import favorite.data.mappers.EmailMapper;
import favorite.data.mappers.UserMapper;
import favorite.models.Email;
import favorite.models.Link;
import favorite.security.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;

@Repository
public class EmailJdbcTemplateRepository implements EmailRepository{

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Email> emailRowMapper = new EmailMapper();
    private final RowMapper<AppUser> userRowMapper = new UserMapper();

    public EmailJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Email> findAll() {
        String sql = """
                select *
                from email;
                """;
        return jdbcTemplate.query(sql, emailRowMapper);
    }

    @Override
    public Email findById(BigInteger emailId) {
        String sql = """
                select *
                from email
                where email_id = ?;
                """;
        return jdbcTemplate.query(sql, emailRowMapper, emailId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Email> findAllByUserId(BigInteger appUserId) {
        String sql = "select * from email where app_user_id = ?;";
        return  jdbcTemplate.query(sql, emailRowMapper, appUserId);
    }

    @Override
    public String findEmailByUserId(BigInteger appUserId) {
        String sql = "select * from app_user where app_user_id = ?;";
        AppUser user = jdbcTemplate.query(sql, userRowMapper, appUserId)
                .stream()
                .findFirst()
                .orElse(null);
        if(user != null){ return user.getEmail(); }
        else{ return null; }
    }

    @Override
    public Email createEmailFromLink(Link link){
        String sql = "select * from app_user where email = ?;";

        AppUser user = jdbcTemplate.query(sql, userRowMapper, link.getFrom())
                .stream()
                .findFirst()
                .orElse(null);

        if(user != null) {
            return create(new Email(BigInteger.ZERO, user.getAppUserId(), link.getUrl(), link.getSentOn()));
        }else{
            return null;
        }
    }

    @Override
    public Email create(Email email) {
        final String sql = "insert into email (app_user_id, url, sent_on) values (?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, (email.getUserId()).intValue());
            ps.setString(2, email.getUrl());
            ps.setTimestamp(3, Timestamp.valueOf(email.getTime()));
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
