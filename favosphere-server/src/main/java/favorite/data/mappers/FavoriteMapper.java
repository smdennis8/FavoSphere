package favorite.data.mappers;

import favorite.models.Favorite;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FavoriteMapper implements RowMapper<Favorite> {

    @Override
    public Favorite mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Favorite(
                rs.getInt("favorite_id"),
                rs.getInt("app_user_id"),
                rs.getString("url"),
                rs.getString("source"),
                rs.getString("creator"),
                rs.getString("type"),
                rs.getString("title"),
                rs.getString("description")
                rs.getString("gif_url")
                rs.getString("image_url"),
                rs.getDate("created_on"),
                rs.getDate("updated_on"),
                rs.getBoolean("is_custom_title"),
                rs.getBoolean("is_custom_description"),
                rs.getBoolean("is_custom_image"),
                rs.getBoolean("is_custom_gif")
        );
    }
}
