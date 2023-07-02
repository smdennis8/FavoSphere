package favorite.data;

import favorite.security.AppUser;

import java.util.List;

public interface AppUserRepository {
    AppUser findByEmail(String email);
    List<AppUser> findAll();
    AppUser create(AppUser user);
}