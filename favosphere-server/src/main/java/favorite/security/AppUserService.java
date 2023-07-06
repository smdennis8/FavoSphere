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

    public List<AppUser> findAll() {
        return repository.findAll();
    }

    public AppUser findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Result<AppUser> create(AppUser appUser) {
        Result<AppUser> result = validate(appUser);
        if (!result.isSuccess()) {
            return result;
        }

        String hashedPassword = encoder.encode(appUser.getPassword());

        appUser = new AppUser(BigInteger.ZERO, (!appUser.getFirstName().isBlank() || !appUser.getFirstName().isEmpty()) ? appUser.getFirstName() : null,
                (!appUser.getMiddleName().isBlank() || !appUser.getMiddleName().isEmpty()) ? appUser.getMiddleName() : null,
                (!appUser.getLastName().isBlank() || !appUser.getLastName().isEmpty()) ? appUser.getLastName() : null,
                (!appUser.getPhone().isBlank() || !appUser.getPhone().isEmpty()) ? appUser.getPhone() : null,
                appUser.getEmail(), hashedPassword, appUser.getRegisteredOn(), appUser.getLastLogin(), true,
                List.of("USER"));

        try {
            appUser = repository.create(appUser);
            result.setPayload(appUser);
        } catch (DuplicateKeyException e) {
            result.addMessage("The provided email already exists", ResultType.INVALID);
        }

        return result;
    }

    private Result<AppUser> validate(AppUser appUser) {
        Result<AppUser> result = new Result<>();
        if (appUser.getEmail() == null || appUser.getEmail().isBlank()) {
            result.addMessage("email is required");
            return result;
        }

        if (appUser.getPassword() == null) {
            result.addMessage("password is required");
            return result;
        }

        if (appUser.getEmail().length() > 50) {
            result.addMessage("email must be less than 255 characters");
        }

        if (!isValidPassword(appUser.getPassword())) {
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