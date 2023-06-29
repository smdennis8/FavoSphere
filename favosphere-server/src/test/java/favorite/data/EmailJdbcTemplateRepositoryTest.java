package favorite.data;

import favorite.models.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EmailJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EmailJdbcTemplateRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindAll(){
        List<Email> actual = repository.findAll();
        assertTrue(actual.size() == 3);
        assertEquals(BigInteger.TWO, actual.get(1).getEmailId());
        assertEquals("https://www.youtube.com/watch?v=j7q8Zzw46oQ", actual.get(1).getUrl());
    }

    @Test
    void shouldFindById(){
        Email actual = repository.findById(BigInteger.TWO);
        assertEquals(BigInteger.TWO, actual.getEmailId());
        assertEquals("https://www.youtube.com/watch?v=j7q8Zzw46oQ", actual.getUrl());
    }

    @Test
    void shouldCreate(){
        Email email = new Email(BigInteger.ZERO, BigInteger.TWO, "http://www.sallysfavorite.com",
                 LocalDateTime.of(2022,02,22,22,22,22));
        Email actual = repository.create(email);
        assertEquals(BigInteger.valueOf(4), actual.getEmailId());

        Email foundById = repository.findById(BigInteger.valueOf(4));
        assertEquals("http://www.sallysfavorite.com", foundById.getUrl());
    }

    @Test
    void shouldDeleteById(){
        assertTrue(repository.deleteById(BigInteger.ONE));
        assertTrue(repository.findAll().size() == 2);
        assertNull(repository.findById(BigInteger.ONE));
    }

    @Test
    void shouldNotDeleteMissing(){
        assertFalse(repository.deleteById(BigInteger.valueOf(99)));
    }
}