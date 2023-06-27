package favorite.data;

import favorite.data.mappers.FavoriteMapper;
import favorite.models.Favorite;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

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
    public Favorite findById(int favoriteId) {
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
        
    }

    @Override
    public boolean update(Favorite favorite) {
        return false;
    }

    @Override
    public boolean delete(int favoriteId) {
        return false;
    }
}