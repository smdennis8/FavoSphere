package favorite.controllers;

import favorite.domain.EmailService;
import favorite.domain.FavoriteService;
import favorite.domain.Result;
import favorite.models.Email;
import favorite.models.Favorite;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = {"http://localhost:3000"})
public class EmailController {

    private final EmailService emailService;
    private final FavoriteService favoriteService;

    public EmailController(EmailService emailService, FavoriteService favoriteService) {
        this.emailService = emailService;
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public List<Email> findAll() {
        return emailService.findAll();
    }

    @GetMapping("/refresh")
    public List<Email> refreshAndFindAll() throws IOException {
        emailService.createFromExternalAll();
        return emailService.findAll();
    }

    @GetMapping("/refresh/{appUserId}")
    public ResponseEntity<Object> findById(@PathVariable BigInteger appUserId) throws IOException {
        emailService.createFromExternalByUser(appUserId, true);
        List<Email> emails = emailService.findByUserId(appUserId);
        if (emails != null) {
            return new ResponseEntity<>(emails, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Favorite favorite) {
        Result<Favorite> result = favoriteService.create(favorite);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{emailId}")
    public ResponseEntity<Object> deleteById(@PathVariable BigInteger emailId) {
        Result<Email> result = emailService.deleteById(emailId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}