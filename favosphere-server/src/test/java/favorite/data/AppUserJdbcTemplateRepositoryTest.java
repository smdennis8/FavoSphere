package favorite.data;

import favorite.security.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AppUserJdbcTemplateRepository repository;



    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindByUsername(){
        AppUser actual = repository.findByEmail("john@smith.com");
        assertEquals("John", actual.getFirstName());
        assertEquals("Jingle-Heimer", actual.getMiddleName());
        assertEquals("Smith", actual.getLastName());
        assertTrue(actual.isEnabled());
        assertEquals(1, actual.getAuthorities().size());
        assertTrue(actual.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")));
    }

    @Test
    void shouldFindAll() {
        List<AppUser> all = repository.findAll();
        assertEquals(3, all.size());
        assertEquals("john@smith.com", all.get(0).getEmail());
        assertEquals("sally@jones.com", all.get(1).getEmail());
    }

    @Test
    void shouldNotFindMissingUsername(){
        AppUser actual = repository.findByEmail("test@test.com");
        assertNull(actual);
    }

    @Test
    void shouldCreate(){
        AppUser user = new AppUser(BigInteger.valueOf(0),"Jane", "John", "Jackson",
                "800-800-8000", "jjjackson@jmail.jom", "pAsSwOrD", LocalDate.of(2023,06,28),
                LocalDate.of(2023,06,28), true, List.of("USER"));
        AppUser actual = repository.create(user);
        AppUser jane = repository.findByEmail("jjjackson@jmail.jom");

        assertEquals(BigInteger.valueOf(4), actual.getAppUserId());
        assertNotNull(actual);
        assertEquals("Jane", jane.getFirstName());
        assertEquals("John", jane.getMiddleName());
        assertEquals("Jackson", jane.getLastName());
        assertTrue(jane.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER")));
        assertTrue(actual.isEnabled());
    }

    @Test
    void shouldCreateWithCredentialsOnly(){
        AppUser user = new AppUser(BigInteger.valueOf(0),null, null, null,
                null, "jjjackson@jmail.jom", "pAsSwOrD", LocalDate.of(2023,06,28),
                LocalDate.of(2023,06,28), true, List.of("USER"));
        AppUser actual = repository.create(user);
        AppUser jane = repository.findByEmail("jjjackson@jmail.jom");
    }

    @Test
    void shouldUpdate(){
        AppUser john = repository.findByEmail("john@smith.com");

        john.setEnabled(false);
        assertTrue(repository.update(john));

        AppUser updatedJohn = repository.findByEmail("john@smith.com");
        assertFalse(updatedJohn.isEnabled());
    }
}