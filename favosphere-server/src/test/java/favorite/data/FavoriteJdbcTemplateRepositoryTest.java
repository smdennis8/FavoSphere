package favorite.data;

import favorite.models.Favorite;
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
class FavoriteJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    FavoriteJdbcTemplateRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindAll(){
        List<Favorite> actual = repository.findAll();
        assertTrue(actual.size() == 2);
        assertEquals(BigInteger.TWO, actual.get(1).getFavoriteId());
        assertEquals("https://www.youtube.com/watch?v=GmneUncWZMg", actual.get(1).getUrl());
    }

    @Test
    void shouldFindById(){
        Favorite actual = repository.findById(BigInteger.TWO);
        assertEquals(BigInteger.TWO, actual.getFavoriteId());
        assertEquals("https://www.youtube.com/watch?v=GmneUncWZMg", actual.getUrl());
    }

    @Test
    void shouldCreate(){
        Favorite favorite = new Favorite(BigInteger.ZERO, BigInteger.TWO, "http://www.myfavorite.com", "Favorite", "Sports Complex", "Video", "Title",
                "Description", "http://www.myfavorite.com/gif", "http://www.myfavorite.com/image", LocalDate.of(2023,06,28),
                LocalDate.of(2023,06,28), true, true, true, true);
        Favorite actual = repository.create(favorite);
        assertEquals(BigInteger.valueOf(3), actual.getFavoriteId());

        Favorite foundById = repository.findById(BigInteger.valueOf(3));
        assertEquals("http://www.myfavorite.com", foundById.getUrl());
        assertEquals("Favorite", foundById.getSource());
    }

    @Test
    void shouldUpdate(){
        //update by first finding the favorite object in repo
        Favorite favorite = repository.findById(BigInteger.ONE);
        favorite.setSource("MyFavorite");
        assertTrue(repository.update(favorite));
        Favorite updated = repository.findById(BigInteger.ONE);
        assertEquals(BigInteger.ONE, updated.getFavoriteId());
        assertEquals("MyFavorite", updated.getSource());

        //update directly by accessing the favorite object by its id
        Favorite actual = new Favorite(BigInteger.valueOf(1), BigInteger.TWO, "http://www.myfavorite.com", "Favorite", "Sports Complex", "Video", "Title",
                "Description", "http://www.myfavorite.com/gif", "http://www.myfavorite.com/image", LocalDate.of(2023,06,28),
                LocalDate.of(2023,06,28), true, true, true, true);
        assertTrue(repository.update(actual));
        assertEquals("Title", actual.getTitle());
    }

    @Test
    void shouldNotUpdateMissing(){
        Favorite favorite = new Favorite(BigInteger.valueOf(99), BigInteger.TWO, "http://www.myfavorite.com", "Favorite", "Sports Complex", "Video", "Title",
                "Description", "http://www.myfavorite.com/gif", "http://www.myfavorite.com/image", LocalDate.of(2023,06,28),
                LocalDate.of(2023,06,28), true, true, true, true);

        assertFalse(repository.update(favorite));
    }

    @Test
    void shouldDeleteById(){
        assertTrue(repository.deleteById(BigInteger.ONE));
        assertTrue(repository.findAll().size() == 1);
        assertNull(repository.findById(BigInteger.ONE));
    }

    @Test
    void shouldNotDeleteMissing(){
        assertFalse(repository.deleteById(BigInteger.valueOf(99)));
    }
}