package favorite.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import favorite.data.FavoriteRepository;
import favorite.models.Favorite;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FavoriteServiceTest {

    @MockBean
    FavoriteRepository repository;

    @Autowired
    FavoriteService service;

    @Test
    void shouldFindAll() {
        List<Favorite> favorites = List.of(
                new Favorite(
                        BigInteger.valueOf(1), BigInteger.valueOf(11), "url.example01@test.com",
                        "sourceExample01", "creatorExample01", "typeExample01",
                        "titleExample01","descriptionExample01", "gifUrlExample01",
                        "imageUrlExample01", LocalDate.of(2001, 1, 1),
                        LocalDate.of(2011, 11, 11), true,
                        false, true, true),
                new Favorite(
                        BigInteger.valueOf(2), BigInteger.valueOf(22), "url.example02@test.com",
                        "sourceExample02", "creatorExample02", "typeExample02",
                        "titleExample02","descriptionExample02", "gifUrlExample02",
                        "imageUrlExample02", LocalDate.of(2002, 2, 2),
                        LocalDate.of(2022, 12, 22), true,
                        true, false, true),
                new Favorite(
                        BigInteger.valueOf(3), BigInteger.valueOf(33), "url.example03@test.com",
                        "sourceExample03", "creatorExample03", "typeExample03",
                        "titleExample03","descriptionExample03", "gifUrlExample03",
                        "imageUrlExample03", LocalDate.of(2003, 3, 3),
                        LocalDate.of(2013, 3, 13), false,
                        true, true, false)        );

        when(repository.findAll()).thenReturn(favorites);

        List<Favorite> actual = service.findAll();
        assertEquals(3, actual.size());

        Favorite favorite = actual.get(0);

        assertEquals(BigInteger.valueOf(1), favorite.getFavoriteId());
        assertEquals(BigInteger.valueOf(11), favorite.getUserId());
        assertEquals("url.example01@test.com", favorite.getUrl());
        assertEquals("sourceExample01", favorite.getSource());
        assertEquals("creatorExample01", favorite.getCreator());
        assertEquals("typeExample01", favorite.getType());
        assertEquals("titleExample01", favorite.getTitle());
        assertEquals("descriptionExample01", favorite.getDescription());
        assertEquals("gifUrlExample01", favorite.getGifUrl());
        assertEquals("imageUrlExample01", favorite.getImageUrl());
        assertEquals(LocalDate.of(2001, 1, 1), favorite.getCreatedOn());
        assertEquals(LocalDate.of(2011, 11, 11), favorite.getUpdatedOn());
        assertTrue(favorite.getCustomTitle());
        assertFalse(favorite.getCustomDescription());
        assertTrue(favorite.getCustomImage());
        assertTrue(favorite.getCustomGif());
    }

    @Test
    void shouldFindFavoriteById() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(1), BigInteger.valueOf(11), "url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "gifUrlExample01",
                "imageUrlExample01", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        when(repository.findById(BigInteger.valueOf(1))).thenReturn(favorite);

        Favorite actual = service.findById(BigInteger.valueOf(1));

        assertEquals(favorite, actual);
    }

    @Test
    void shouldNotFindMissingId() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(1), BigInteger.valueOf(11), "url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "gifUrlExample01",
                "imageUrlExample01", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        when(repository.findById(BigInteger.valueOf(99))).thenReturn(favorite);

        Favorite actual = service.findById(BigInteger.valueOf(1));

        assertNotEquals(favorite, actual);

    }

    @Test
    void shouldCreateFavorite() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        Result<Favorite> actual = service.create(favorite);
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotCreateExistingFavorite() {
        Result<Favorite> actual = service.create(new Favorite(
                BigInteger.valueOf(10), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().get(0).contains("existing"));
    }

    @Test
    void shouldNotCreateNullFavorite() {
        Result<Favorite> actual = service.create(null);
        assertFalse(actual.isSuccess());
        assertEquals("Favorite cannot be null", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithNullUrl() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), null,
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        Result<Favorite> actual = service.create(favorite);
        assertFalse(actual.isSuccess());
        assertEquals("Url is required", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithBlankUrl() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), " ",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        Result<Favorite> actual = service.create(favorite);
        assertFalse(actual.isSuccess());
        assertEquals("Url is required", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithInvalidUrl() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "FAKE",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        Result<Favorite> actual = service.create(favorite);
        assertFalse(actual.isSuccess());
        assertEquals("Url must be a valid url", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithInvalidGifUrl() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "FAKE",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);
        Result<Favorite> actual = service.create(favorite);
        assertFalse(actual.isSuccess());
        assertEquals("Gif url must be a valid url", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithInvalidImageUrl() {
        Result<Favorite> actual = service.create(new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "imageUrlExample01", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true));
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("Image url must be a valid url", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithoutCreatedOnDate() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", null,
                LocalDate.of(2011, 11, 11), true,
                false, true, true);
        Result<Favorite> actual = service.create(favorite);
        assertFalse(actual.isSuccess());
        assertEquals("Created on date is required", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateIfCreatedOnDateIsInFuture() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(3000, 01, 01),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);
        Result<Favorite> actual = service.create(favorite);
        assertFalse(actual.isSuccess());
        assertEquals("Created on date cannot be in the future", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateIfUpdatedOnDateIsInFuture() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2000, 01, 01),
                LocalDate.of(3000, 11, 11), true,
                false, true, true);
        Result<Favorite> actual = service.create(favorite);
        assertFalse(actual.isSuccess());
        assertEquals("Updated on date cannot be in the future", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateWithoutUpdatedOnDate() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2000, 01, 01),
                null, true,
                false, true, true);
        Result<Favorite> actual = service.create(favorite);
        assertFalse(actual.isSuccess());
        assertEquals("Updated on date is required", actual.getMessages().get(0));
    }

    @Test
    void shouldNotCreateIfUpdatedOnIsBeforeCreatedOn() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2000, 01, 01),
                LocalDate.of(1900, 12, 31), true,
                false, true, true);
        Result<Favorite> actual = service.create(favorite);
        assertFalse(actual.isSuccess());
        assertEquals("Updated on date cannot be before created on date", actual.getMessages().get(0));
    }

    @Test
    void shouldUpdateExistingFavorite() {
        Favorite toUpdate = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        toUpdate.setCustomTitle(false);

        when(repository.update(toUpdate)).thenReturn(true);

        Result<Favorite> actual = service.update(toUpdate);
        assertTrue(actual.isSuccess());
        assertEquals(0, actual.getMessages().size());
    }

    @Test
    void shouldNotUpdateNullFavorite() {
        Result<Favorite> actual = service.update(null);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals("Favorite cannot be null", actual.getMessages().get(0));
    }

    @Test
    void shouldNotUpdateNonExistingFavorite() {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(999), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        Result<Favorite> actual = service.update(favorite);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertEquals(ResultType.NOT_FOUND, actual.getResultType());
        assertEquals("Favorite doesn't exist", actual.getMessages().get(0));
    }

    @Test
    void shouldDeleteExistingFavorite() {
        Favorite toDelete = new Favorite(
                BigInteger.valueOf(0), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);
        when(repository.deleteById(toDelete.getFavoriteId())).thenReturn(true);

        Result<Favorite> actual = service.deleteById(toDelete.getFavoriteId());
        assertTrue(actual.isSuccess());
        assertEquals(0, actual.getMessages().size());
    }

    @Test
    void shouldNotDeleteNullFavorite() {
        Favorite blank = new Favorite();

        Result<Favorite> actual = service.deleteById(blank.getFavoriteId());
        assertFalse(actual.isSuccess());
        assertEquals("Favorite: null doesn't exist", actual.getMessages().get(0));
    }

    @Test
    void shouldNotDeleteNonExistingFavorite() {

    }
}