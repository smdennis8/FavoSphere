package favorite.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import favorite.data.FavoriteRepository;
import favorite.models.Favorite;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    FavoriteRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    private Favorite makeFavorite(BigInteger favoriteId) {
        return new Favorite(favoriteId, BigInteger.valueOf(69), "http://www.favorite-url@website.com",
                "source", "creator", "type", "title", "description",
                "http://www.favorite-gifUrl@website.com", "http://www.favorite-imageUrl@website.com",
                LocalDate.of(2000,1,1), LocalDate.of(2020,12,31),
                true, true, true, true);
    }

    @Test
    void shouldFindAllAndReturnHttpOK() throws Exception {
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
                    true, true, false)
        );

        when(repository.findAll())
                .thenReturn(favorites);

        String expectedJson = objectMapper.writeValueAsString(favorites);

        mvc.perform(get("/favorite"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldFindByIdAndReturnHttpOK() throws Exception {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(3), BigInteger.valueOf(33), "url.example03@test.com",
                "sourceExample03", "creatorExample03", "typeExample03",
                "titleExample03","descriptionExample03", "gifUrlExample03",
                "imageUrlExample03", LocalDate.of(2003, 3, 3),
                LocalDate.of(2013, 3, 13), false,
                true, true, false);

        when(repository.findById(BigInteger.valueOf(3))).thenReturn(favorite);

        String expectedJson = objectMapper.writeValueAsString(favorite);

        mvc.perform(get("/favorite/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldNotFindMissingFavoriteByIdAndReturnHttpNotFound() throws Exception {

        mvc.perform(get("/favorite/99"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "john@smith.com", password = "P@ssw0rd!", authorities = "ADMIN")
    void shouldAddValidFavoriteAndReturn201() throws Exception {
        Favorite favorite = makeFavorite(BigInteger.ZERO);
        Favorite expected = makeFavorite(BigInteger.valueOf(7));

        when(repository.create(any())).thenReturn(expected);

        String jsonIn = TestHelpers.serializeObjectToJson(favorite);
        String expectedJson = TestHelpers.serializeObjectToJson(expected);

        var request = post("/favorite")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(username = "john@smith.com", password = "P@ssw0rd!", authorities = "ADMIN")
    void shouldNotAddInvalidFavoriteAndReturn400() throws Exception {
        Favorite favorite = makeFavorite(BigInteger.ZERO);
        favorite.setGifUrl("INVALID GIF URL");

        String jsonIn = TestHelpers.serializeObjectToJson(favorite);

        var request = post("/favorite")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "john@smith.com", password = "P@ssw0rd!", authorities = "ADMIN")
    void shouldNotAddDuplicateFavoriteAndReturnX() throws Exception {
        Favorite favorite = makeFavorite(BigInteger.TEN);
        Favorite favoriteDuplicate = makeFavorite(BigInteger.TEN);

        when(repository.create(any())).thenReturn(favorite);
        when(repository.create(any())).thenReturn(favoriteDuplicate);

        String jsonIn = TestHelpers.serializeObjectToJson(favorite);
        String expectedJson = TestHelpers.serializeObjectToJson(favorite);

        String jsonInDuplicate = TestHelpers.serializeObjectToJson(favoriteDuplicate);
        String expectedJsonDuplicate = TestHelpers.serializeObjectToJson(favoriteDuplicate);

        var request = post("/favorite")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        var requestDuplicate = post("/favorite")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInDuplicate);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

        mvc.perform(requestDuplicate)
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "john@smith.com", password = "P@ssw0rd!", authorities = "ADMIN")
    void shouldUpdateFavoriteAndReturnHttpNoContent() throws Exception {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(1), BigInteger.valueOf(11), "http://url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        String favoriteJson = objectMapper.writeValueAsString(favorite);

        when(repository.update(favorite)).thenReturn(true);

        var request = put("/favorite/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(favoriteJson);

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "john@smith.com", password = "P@ssw0rd!", authorities = "ADMIN")
    void shouldNotUpdateFavoriteIfIdsMismatchedAndReturnHttpConflict() throws Exception {

        Favorite favorite = new Favorite(
                BigInteger.valueOf(1), BigInteger.valueOf(11), "url.example01@test.com",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "gifUrlExample01",
                "imageUrlExample01", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        String favoriteJson = objectMapper.writeValueAsString(favorite);

        var request = put("/favorite/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(favoriteJson);

        mvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "john@smith.com", password = "P@ssw0rd!", authorities = "ADMIN")
    void shouldNotUpdateMissingFavoriteAndReturnHttpNotFound() throws Exception {
        Favorite favorite = new Favorite(
                BigInteger.valueOf(99), BigInteger.valueOf(999), "http://url.exampleX@test.com",
                "sourceExampleX", "creatorExampleX", "typeExampleX",
                "titleExampleX","descriptionExampleX", "http://gifUrl.ExampleX@test.com",
                "http://imageUrl.ExampleX@test.com", LocalDate.of(1901, 1, 1),
                LocalDate.of(1902, 2, 2), true,
                false, true, true);

        String favoriteJson = objectMapper.writeValueAsString(favorite);

        var request = put("/favorite/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(favoriteJson);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john@smith.com", password = "P@ssw0rd!", authorities = "ADMIN")
    void shouldNotUpdateInvalidFavoriteAndReturnHttpBadRequest() throws Exception {

        Favorite favorite = new Favorite(
                BigInteger.valueOf(1), BigInteger.valueOf(11), "null",
                "sourceExample01", "creatorExample01", "typeExample01",
                "titleExample01","descriptionExample01", "http://gifUrl.Example01@test.com",
                "http://imageUrl.Example01@test.com", LocalDate.of(2001, 1, 1),
                LocalDate.of(2011, 11, 11), true,
                false, true, true);

        List<String> errorMessages = List.of("Url must be a valid url");
        String expectedJson = objectMapper.writeValueAsString(errorMessages);

        String favoriteJson = objectMapper.writeValueAsString(favorite);

        var request = put("/favorite/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(favoriteJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(username = "john@smith.com", password = "P@ssw0rd!", authorities = "ADMIN")
    void shouldDeleteFavoriteAndReturnHttpNoContent() throws Exception {

        when(repository.deleteById(BigInteger.valueOf(3))).thenReturn(true);

        var request = delete("/favorite/3");

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "john@smith.com", password = "P@ssw0rd!", authorities = "ADMIN")
    void shouldNotDeleteMissingAndReturnHttpNotFound() throws Exception {

        var request = delete("/favorite/99");

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
}