package favorite.data;

import favorite.models.Email;
import favorite.models.Link;

import java.math.BigInteger;
import java.util.List;

public interface EmailRepository {

    List<Email> findAll();

    Email findById(BigInteger emailId);

    List<Email> findByUserId(BigInteger emailId);

    String findEmailByUserId(BigInteger appUserId);

    Email createEmailFromLink(Link link);

    Email create(Email email);


    boolean deleteById(BigInteger emailId);
}
