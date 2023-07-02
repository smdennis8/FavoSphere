package favorite.security;

import favorite.data.AppUserRepository;
import favorite.domain.Result;
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
    void shouldFindAll() {
        AppUser user1 = new AppUser(BigInteger.valueOf(1),"Jane", "John", "Jackson",
                "800-800-8000", "jjjackson@jmail.jom", "pAsSwOrD", LocalDate.of(2023,06,28),
                LocalDate.of(2023,06,28), true, List.of("USER"));
        AppUser user2 = new AppUser(BigInteger.TWO, "John", "Jingle-Heimer", "Smith",
                "1-111-111-1111", "john@smith.com", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",
                LocalDate.of(2010,01,11), LocalDate.of(2023,06,26), true, List.of("ADMIN"));
        when(repository.findAll()).thenReturn(List.of(user1, user2));
        List<AppUser> actual = service.findAll();
        assertTrue(actual.size() == 2);
        assertEquals(BigInteger.ONE, actual.get(0).getAppUserId());
        assertEquals("Jane", actual.get(0).getFirstName());
        assertEquals(BigInteger.TWO, actual.get(1).getAppUserId());
        assertEquals("John", actual.get(1).getFirstName());
    }

    @Test
    void shouldCreate(){
        AppUser expected = new AppUser(BigInteger.ONE, "John", "Jingle-Heimer", "Smith",
                "1-111-111-1111", "john@smith.com", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa",
                LocalDate.of(2010,01,11), LocalDate.of(2023,06,26), true, List.of("ADMIN"));

        Credentials credentials = new Credentials();
        credentials.setEmail("john@smith.com");
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
    void shouldNotCreateWithNullEmail(){
        Credentials credentials = new Credentials();
        credentials.setEmail(null);
        credentials.setPassword("$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa");
        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("email is required", result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithBlankEmail() {
        Credentials credentials = new Credentials();
        credentials.setEmail("");
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
        credentials.setEmail("a".repeat(256));
        credentials.setPassword("$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa");
        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("email must be less than 255 characters", result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateAppUserWithExistingEmail() {
        Credentials credentials = new Credentials();
        credentials.setEmail("john@smith.com");
        credentials.setPassword("Val1dP@ssw0rd!\"");

        when(repository.create(any())).thenThrow(DuplicateKeyException.class);

        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("The provided email already exists", result.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithNullPassword() {
        Credentials credentials = new Credentials();
        credentials.setEmail("test@test.com");
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
        credentials.setEmail("test@test.com");
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
        credentials.setEmail("test@test.com");
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
        credentials.setEmail("test@test.com");
        credentials.setPassword("invalidp4ssword");

        Result<AppUser> result = service.create(credentials);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("password must be at least 8 character and contain a digit, a letter, and a non-digit/non-letter",
                result.getMessages().get(0));
    }
}