package favorite.data;

import favorite.models.Favorite;

import java.util.List;

public interface FavoriteRepository {

    List<Favorite> findAll();

    Favorite findById(int favoriteId);

    Favorite create(Favorite favorite);

    boolean update(Favorite favorite);

    boolean delete(int favoriteId);
}
