package favorite.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import favorite.domain.Result;
import favorite.domain.ResultType;
import favorite.domain.FavoriteService;
import favorite.models.Favorite;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService service;

    public FavoriteController(FavoriteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Favorite> findAll() {
        return service.findAll();
    }

    @GetMapping("/{favoriteId}")
    public ResponseEntity<Object> findById(@PathVariable BigInteger favoriteId) {
        Favorite favorite = service.findById(favoriteId);
        if (favorite != null) {
            return new ResponseEntity<>(favorite, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Favorite favorite) {
        Result<Favorite> result = service.create(favorite);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{favoriteId}")
    public ResponseEntity<Object> update(@PathVariable BigInteger favoriteId, @RequestBody Favorite favorite) {
        if(!Objects.equals(favoriteId, favorite.getFavoriteId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<Favorite> result = service.update(favorite);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Object> deleteById(@PathVariable BigInteger favoriteId) {
        Result<Favorite> result = service.deleteById(favoriteId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}