package favorite.data;

import favorite.models.Email;
import favorite.models.Favorite;

import java.math.BigInteger;
import java.util.List;

public interface EmailRepository {

    List<Email> findAll();

    Email findById(BigInteger emailId);

    List<Email> findByUserId(BigInteger emailId);

    Email create(Email email);

    boolean deleteById(BigInteger emailId);
}
