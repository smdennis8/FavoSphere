package favorite.data;

import favorite.data.mappers.FavoriteMapper;
import favorite.models.Favorite;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class FavoriteJdbcTemplateRepository implements FavoriteRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Favorite> rowMapper = new FavoriteMapper();

    public FavoriteJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Favorite> findAll() {
        String sql = """
                select *
                from favorite;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Favorite findById(BigInteger favoriteId) {
        String sql = """
                select *
                from favorite
                where favorite_id = ?;
                """;
        return jdbcTemplate.query(sql, rowMapper, favoriteId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Favorite create(Favorite favorite) {
        final String sql = "insert into favorite (app_user_id, url, `source`, creator, `type`, title, `description`, " +
                "gif_url, image_url, created_on, updated_on, is_custom_title, is_custom_description, is_custom_image, is_custom_gif) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, (favorite.getUserId()).intValue());
            ps.setString(2, favorite.getUrl());
            ps.setString(3, favorite.getSource());
            ps.setString(4, favorite.getCreator());
            ps.setString(5, favorite.getType());
            ps.setString(6, favorite.getTitle());
            ps.setString(7, favorite.getDescription());
            ps.setString(8, favorite.getGifUrl());
            ps.setString(9, favorite.getImageUrl());
            ps.setDate(10, Date.valueOf(favorite.getCreatedOn()));
            ps.setDate(11, Date.valueOf(favorite.getUpdatedOn()));
            ps.setBoolean(12, favorite.getCustomTitle());
            ps.setBoolean(13, favorite.getCustomDescription());
            ps.setBoolean(14, favorite.getCustomImage());
            ps.setBoolean(15, favorite.getCustomGif());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        favorite.setFavoriteId(BigInteger.valueOf(keyHolder.getKey().intValue()));
        return favorite;
    }

    @Override
    public boolean update(Favorite favorite) {
        final String sql = """
                update favorite set
                    app_user_id = ?,
                    url = ?,
                    source = ?,
                    creator = ?,
                    type = ?,
                    title = ?,
                    description = ?,
                    gif_url = ?,
                    image_url = ?,
                    created_on = ?,
                    updated_on = ?,
                    is_custom_title = ?,
                    is_custom_description = ?,
                    is_custom_image = ?,
                    is_custom_gif = ?
                where favorite_id = ?;
                """;

        int rowsUpdated = jdbcTemplate.update(sql,
                favorite.getUserId(),
                favorite.getUrl(),
                favorite.getSource(),
                favorite.getCreator(),
                favorite.getType(),
                favorite.getTitle(),
                favorite.getDescription(),
                favorite.getGifUrl(),
                favorite.getImageUrl(),
                favorite.getCreatedOn(),
                favorite.getUpdatedOn(),
                favorite.getCustomTitle(),
                favorite.getCustomDescription(),
                favorite.getCustomImage(),
                favorite.getCustomGif(),
                favorite.getFavoriteId());

        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteById(BigInteger favoriteId) {
        return jdbcTemplate.update("delete from favorite where favorite_id = ?;", favoriteId) > 0;
    }
}