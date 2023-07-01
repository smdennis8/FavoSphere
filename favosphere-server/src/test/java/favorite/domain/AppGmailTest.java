package favorite.domain;

import favorite.models.Link;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppGmailTest {

    @Autowired
    AppGmail appGmail;

    @Test
    void shouldFindAllInboxLinks() throws IOException {
        List<Link> allLinks = appGmail.getAllInboxLinks(false);
        assertEquals(allLinks.size(),3);
    }

    @Test
    void shouldFindUserInboxLinks() throws IOException {
        List<Link> userLinks = appGmail.getUserInboxLinks("favosphere.app.inbox@gmail.com",false);
        assertEquals(userLinks.size(),2);
    }

    @Test
    void shouldNotFindUnknownUserInboxLinks() throws IOException {
        List<Link> userLinks = appGmail.getUserInboxLinks("notARealUser@gmail.com",false);
        assertEquals(userLinks.size(),0);
    }
}