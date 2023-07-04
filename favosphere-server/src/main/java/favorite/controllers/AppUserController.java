package favorite.controllers;

import favorite.models.Favorite;
import favorite.security.AppUser;
import favorite.security.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/appUser")
@CrossOrigin(origins = {"http://localhost:3000"})
public class AppUserController {
    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppUser> findAll() {
        return service.findAll();
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> findByEmail(@PathVariable String email) {
        AppUser appUser = service.findByEmail(email);
        if (appUser != null) {
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
