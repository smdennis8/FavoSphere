package favorite.data;

import favorite.models.Favorite;

import java.math.BigInteger;
import java.util.List;

public interface FavoriteRepository {

    List<Favorite> findAll();

    List<Favorite> findAllByUserId(BigInteger appUserId);

    Favorite findById(BigInteger favoriteId);

    Favorite create(Favorite favorite);

    boolean update(Favorite favorite);

    boolean deleteById(BigInteger favoriteId);
}
