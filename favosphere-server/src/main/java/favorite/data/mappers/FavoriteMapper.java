package favorite.data.mappers;

import favorite.models.Favorite;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FavoriteMapper implements RowMapper<Favorite> {

    @Override
    public Favorite mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Favorite(
                BigInteger.valueOf(rs.getInt("favorite_id")),
                BigInteger.valueOf(rs.getInt("app_user_id")),
                rs.getString("url"),
                rs.getString("source"),
                rs.getString("creator"),
                rs.getString("type"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("gif_url"),
                rs.getString("image_url"),
                LocalDate.parse(rs.getString("created_on")),
                LocalDate.parse(rs.getString("updated_on")),
                rs.getBoolean("is_custom_title"),
                rs.getBoolean("is_custom_description"),
                rs.getBoolean("is_custom_image"),
                rs.getBoolean("is_custom_gif")
        );
    }
}
