package favorite.security;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import favorite.data.AppUserRepository;
import favorite.domain.Result;
import favorite.domain.ResultType;

import java.math.BigInteger;
import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByEmail(username);

        if (appUser == null) {
            throw new UsernameNotFoundException(username + " not found.");
        }

        return appUser;
    }

    public Result<AppUser> create(Credentials credentials) {
        Result<AppUser> result = validate(credentials);
        if (!result.isSuccess()) {
            return result;
        }

        String hashedPassword = encoder.encode(credentials.getPassword());

        AppUser appUser = new AppUser(BigInteger.ZERO, null, null, null, null,
                credentials.getEmail(), hashedPassword, null, null, true,
                List.of("USER"));

        try {
            appUser = repository.create(appUser);
            result.setPayload(appUser);
        } catch (DuplicateKeyException e) {
            result.addMessage("The provided email already exists", ResultType.INVALID);
        }

        return result;
    }

    private Result<AppUser> validate(Credentials credentials) {
        Result<AppUser> result = new Result<>();
        if (credentials.getEmail() == null || credentials.getEmail().isBlank()) {
            result.addMessage("email is required");
            return result;
        }

        if (credentials.getPassword() == null) {
            result.addMessage("password is required");
            return result;
        }

        if (credentials.getEmail().length() > 50) {
            result.addMessage("email must be less than 255 characters");
        }

        if (!isValidPassword(credentials.getPassword())) {
            result.addMessage(
                    "password must be at least 8 character and contain a digit," +
                            " a letter, and a non-digit/non-letter");
        }

        return result;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        return digits > 0 && letters > 0 && others > 0;
    }
}