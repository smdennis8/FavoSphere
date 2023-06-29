package favorite.security;

import favorite.data.AppUserRepository;
import favorite.domain.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserServiceTest {
    @MockBean
    AppUserRepository repository;

    @Autowired
    AppUserService service;

    @Test
    void shouldLoadUserByUsername(){
        AppUser expected = new AppUser(BigInteger.ONE, "John", "Jingle-Heimer", "Smith",
                "1-111-111-1111", "john@smith.com", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",
                LocalDate.of(2010,01,11), LocalDate.of(2023,06,26), true, List.of("ADMIN"));

        when(repository.findByEmail("john@smith.com")).thenReturn(expected);
        UserDetails actual = service.loadUserByUsername("john@smith.com");
        assertEquals("john@smith.com", actual.getUsername());
        assertEquals("$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", actual.getPassword());
        assertEquals(1, actual.getAuthorities().size());
        assertTrue(actual.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")));
        assertTrue(actual.isEnabled());
    }

    @Test
    void shouldCreate(){
        AppUser expected = new AppUser(BigInteger.ONE, "John", "Jingle-Heimer", "Smith",
                "1-111-111-1111", "john@smith.com", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",
                LocalDate.of(2010,01,11), LocalDate.of(2023,06,26), true, List.of("ADMIN"));

        Credentials credentials = new Credentials();
        credentials.setUsername("john@smith.com");
        credentials.setPassword("$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa");
        when(repository.create(any())).thenReturn(expected);
        Result<AppUser> result = service.create(credentials);

        assertTrue(result.isSuccess());
        assertEquals(BigInteger.ONE, result.getPayload().getAppUserId());
        assertEquals("john@smith.com", result.getPayload().getUsername());
        assertEquals("$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", result.getPayload().getPassword());
        assertEquals(1, result.getPayload().getAuthorities().size());
        assertTrue(expected.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")));
    }

    @Test
    void shouldNotCreateWithNullUsername(){
        Credentials credentials = new Credentials();
        credentials.setUsername(null);
        credentials.setPassword("$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa");
        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("email is required", result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithBlankUsername() {
        Credentials credentials = new Credentials();
        credentials.setUsername("");
        credentials.setPassword("$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa");
        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("email is required", result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithUsernameGreaterThan50Chars() {
        Credentials credentials = new Credentials();
        credentials.setUsername("a".repeat(51));
        credentials.setPassword("$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa");
        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("email must be less than 50 characters", result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateAppUserWithExistingUsername() {
        Credentials credentials = new Credentials();
        credentials.setUsername("john@smith.com");
        credentials.setPassword("Val1dP@ssw0rd!\"");

        when(repository.create(any())).thenThrow(DuplicateKeyException.class);

        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("The provided username already exists", result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithNullPassword() {
        Credentials credentials = new Credentials();
        credentials.setUsername("test@test.com");
        credentials.setPassword(null);

        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("password is required", result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithLessThan8CharsInPassword() {
        Credentials credentials = new Credentials();
        credentials.setUsername("test@test.com");
        credentials.setPassword("invalid");

        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("password must be at least 8 character and contain a digit, a letter, and a non-digit/non-letter",
                result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithoutNumberInPassword() {
        Credentials credentials = new Credentials();
        credentials.setUsername("test@test.com");
        credentials.setPassword("invalidp@ssword!");

        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("password must be at least 8 character and contain a digit, a letter, and a non-digit/non-letter",
                result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithoutSpecialCharInPassword() {
        Credentials credentials = new Credentials();
        credentials.setUsername("test@test.com");
        credentials.setPassword("invalidp4ssword");

        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("password must be at least 8 character and contain a digit, a letter, and a non-digit/non-letter",
                result.getMessages().get(0));
    }
}