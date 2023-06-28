package favorite.data;

import favorite.security.AppUser;

public interface AppUserRepository {
    AppUser findByEmail(String email);
    AppUser create(AppUser user);
}