package favorite.data;

import favorite.models.Favorite;
import favorite.security.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FavoriteJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    FavoriteJdbcTemplateRepository repository;

    static boolean hasRun = false;

    @BeforeEach
    void setup() {
        if (!hasRun) {
            jdbcTemplate.update("call set_known_good_state();");
            hasRun = true;
        }
    }

    @Test
    void shouldFindAll(){
//        (2, 'https://www.youtube.com/watch?v=GmneUncWZMg', 'Youtube', 'Sports Complex', 'Video', 'Most Unexpected Animal Interference Moments in Sports | Funny Invasions & Interruption',
//        '(All rights go to the original leagues and their broadcasters, no copyright infringement intended.  If I feature clips that you own and that you don\'t want me to feature, contact me via
//        my business email and I\'ll take it down as soon as I can) \"Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made for \"fair use\" for purposes such as criticism,
//        comment, news reporting, teaching, scholarship, and research. Fair use is a use permitted by copyright statute that might otherwise be infringing. Non-profit, educational or personal use tips
//        the balance in favor of fair use.\"', null, 'https://i.ytimg.com/vi/GmneUncWZMg/hq720.jpg?sqp=-oaymwEcCOgCEMoBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLAoIhY5ERmG_dnz42mHjc_uCxYqyg', '2023-10-10',
//        '2023-11-11', 1, 1, 1, 1);
        List<Favorite> actual = repository.findAll();
        assertFalse(actual.size() == 0);
        assertEquals(BigInteger.TWO, actual.get(1).getFavoriteId());
        assertEquals("https://www.youtube.com/watch?v=GmneUncWZMg", actual.get(1).getUrl());
    }
}