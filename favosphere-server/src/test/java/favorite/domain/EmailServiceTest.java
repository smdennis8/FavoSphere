package favorite.domain;

import favorite.data.EmailJdbcTemplateRepository;
import favorite.data.EmailRepository;
import favorite.data.FavoriteRepository;
import favorite.models.Email;
import favorite.models.Favorite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EmailServiceTest {

    @MockBean
    EmailRepository repository;

    @Autowired
    EmailService service;

    @Test
    void shouldFindAll() {
        List<Email> emails = List.of(
                new Email(
                        BigInteger.valueOf(1), BigInteger.valueOf(1), "http://url.example01@test.com",
                        LocalDateTime.of(2001, 01, 01,01,01,01)),
                new Email(
                        BigInteger.valueOf(2), BigInteger.valueOf(1), "http://url.example02@test.com",
                        LocalDateTime.of(2002, 02, 02,02,02,02)),
                new Email(
                        BigInteger.valueOf(3), BigInteger.valueOf(2), "http://url.example03@test.com",
                        LocalDateTime.of(2003, 03, 03,03,03,03))
        );

        when(repository.findAll()).thenReturn(emails);

        List<Email> actual = service.findAll();
        assertEquals(3, actual.size());

        Email email = actual.get(0);

        assertEquals(BigInteger.valueOf(1), email.getEmailId());
        assertEquals(BigInteger.valueOf(1), email.getUserId());
        assertEquals("http://url.example01@test.com", email.getUrl());
        assertEquals(LocalDateTime.of(2001, 01, 01, 01, 01, 01), email.getTime());
    }

    @Test
    void shouldFindById() {

        Email email = new Email(
                BigInteger.valueOf(1), BigInteger.valueOf(1), "http://url.example01@test.com",
                LocalDateTime.of(2001, 01, 01,01,01,01));

        when(repository.findById(BigInteger.valueOf(1))).thenReturn(email);
        Email actual = service.findById(BigInteger.valueOf(1));
        assertEquals(email, actual);
    }

    @Test
    void shouldNotFindMissingId() {
        Email email = new Email(
                BigInteger.valueOf(1), BigInteger.valueOf(1), "http://url.example01@test.com",
                LocalDateTime.of(2001, 01, 01,01,01,01));

        when(repository.findById(BigInteger.valueOf(99))).thenReturn(email);
        Email actual = service.findById(BigInteger.valueOf(1));
        assertNotEquals(email, actual);
    }

    @Test
    void shouldCreateEmail() {
        Email email = new Email(
                BigInteger.valueOf(0), BigInteger.valueOf(1), "http://url.example01@test.com",
                LocalDateTime.of(2001, 01, 01,01,01,01));

        Result<Email> actual = service.create(email);
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotCreateExistingEmail() {
        Email email = new Email(
                BigInteger.valueOf(1), BigInteger.valueOf(1), "http://url.example01@test.com",
                LocalDateTime.of(2001, 01, 01,01,01,01));
        Result<Email> actual = service.create(email);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().get(0).contains("existing"));
    }

    @Test
    void shouldNotCreateNullEmail() {
        Result<Email> actual = service.create(null);
        assertFalse(actual.isSuccess());
        assertEquals("Email cannot be null", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithNullUrl() {
        Email email = new Email(
                BigInteger.valueOf(0), BigInteger.valueOf(1), null,
                LocalDateTime.of(2001, 01, 01,01,01,01));

        Result<Email> actual = service.create(email);
        assertFalse(actual.isSuccess());
        assertEquals("Url is required", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithBlankUrl() {
        Email email = new Email(
                BigInteger.valueOf(0), BigInteger.valueOf(1), " ",
                LocalDateTime.of(2001, 01, 01,01,01,01));

        Result<Email> actual = service.create(email);
        assertFalse(actual.isSuccess());
        assertEquals("Url is required", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithInvalidUrl() {
        Email email = new Email(
                BigInteger.valueOf(0), BigInteger.valueOf(1), "FAKE",
                LocalDateTime.of(2001, 01, 01,01,01,01));

        Result<Email> actual = service.create(email);
        assertFalse(actual.isSuccess());
        assertEquals("Url must be a valid url", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithoutCreatedOnDate() {
        Email email = new Email(
                BigInteger.valueOf(0), BigInteger.valueOf(1), "http://url.example01@test.com", null);
        Result<Email> actual = service.create(email);
        assertFalse(actual.isSuccess());
        assertEquals("Email sent date is required", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateIfSentOnDateIsInFuture() {
        Email email = new Email(
                BigInteger.valueOf(0), BigInteger.valueOf(1), "http://url.example01@test.com",
                LocalDateTime.of(2050, 01, 01,01,01,01));
        Result<Email> actual = service.create(email);
        assertFalse(actual.isSuccess());
        assertEquals("Email sent date cannot be in the future", actual.getMessages().get(0));
    }

    @Test
    void shouldDeleteExistingEmail() {
        Email toDelete = new Email(
                BigInteger.valueOf(0), BigInteger.valueOf(1), "http://url.example01@test.com",
                LocalDateTime.of(2001, 01, 01,01,01,01));
        when(repository.deleteById(toDelete.getEmailId())).thenReturn(true);

        Result<Email> actual = service.deleteById(toDelete.getEmailId());
        assertTrue(actual.isSuccess());
        assertEquals(0, actual.getMessages().size());
    }

    @Test
    void shouldNotDeleteNullFavorite() {
        Email blank = new Email();

        Result<Email> actual = service.deleteById(blank.getEmailId());
        assertFalse(actual.isSuccess());
        assertEquals("Email: null doesn't exist", actual.getMessages().get(0));
    }

    @Test
    void shouldNotDeleteNonExistingFavorite() {
        Email dummy = new Email(
                BigInteger.valueOf(0), BigInteger.valueOf(1), "http://url.example01@test.com",
                LocalDateTime.of(2001, 01, 01,01,01,01));

        when(repository.deleteById(dummy.getEmailId())).thenReturn(true);
        Result<Email> actual = service.deleteById(BigInteger.TEN);
        assertFalse(actual.isSuccess());
        assertEquals("Email: 10 doesn't exist", actual.getMessages().get(0));
    }
}