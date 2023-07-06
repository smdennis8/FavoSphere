package favorite.domain;

import org.springframework.stereotype.Service;
import favorite.data.FavoriteRepository;
import favorite.models.Favorite;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class FavoriteService {

    private final FavoriteRepository repository;

    public FavoriteService(FavoriteRepository repository) {
        this.repository = repository;
    }

    public List<Favorite> findAll() {
        return repository.findAll();
    }

    public List<Favorite> findAllByUserId(BigInteger appUserId) {
        return repository.findAllByUserId(appUserId);
    }

    public Favorite findById(BigInteger favoriteId) {
        return repository.findById(favoriteId);
    }

    public Result<Favorite> create(Favorite favorite) {
        Result<Favorite> result = validate(favorite);


        if (!result.isSuccess()) {
            return result;
        }

        if (favorite.getFavoriteId() != null && favorite.getFavoriteId().compareTo(BigInteger.ZERO) > 0) {
            result.addMessage("Cannot create existing favorite");
            return result;
        }

        Favorite newFavorite = repository.create(favorite);
        result.setPayload(newFavorite);
        return result;
    }

    public Result<Favorite> update(Favorite favorite) {
        Result<Favorite> result = validate(favorite);

        if (!result.isSuccess()) {
            return result;
        }

        boolean updated = repository.update(favorite);
        if (!updated) {
            result.addMessage("Favorite doesn't exist", ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<Favorite> deleteById(BigInteger favoriteId) {
        Result<Favorite> result = new Result<>();
        if (!repository.deleteById(favoriteId)) {
            result.addMessage(String.format("Favorite: %s doesn't exist", favoriteId), ResultType.NOT_FOUND);
        }
        return result;
    }

    private Result<Favorite> validate(Favorite favorite) {
        Result<Favorite> result = new Result<>();

        if (favorite == null) {
            result.addMessage("Favorite cannot be null");
            return result;
        }

        if(favorite.getUserId() == null){
            result.addMessage("User ID is required");
        }

        if (favorite.getUrl() == null || favorite.getUrl().isBlank()) {
            result.addMessage("Url is required");
        }

        if (!Validations.isValidUrl(favorite.getUrl())) {
            result.addMessage("Url must be a valid url");
        }

        if ((favorite.getGifUrl() != null && !(favorite.getGifUrl().isBlank()) && (favorite.getGifUrl().equals(""))) && (favorite.getGifUrl().isEmpty())
                && !Validations.isValidUrl(favorite.getGifUrl())) {
            result.addMessage("Gif url must be a valid url");
        }

        if ((favorite.getImageUrl() != null && !(favorite.getImageUrl().isBlank()) && ((favorite.getImageUrl().equals(""))) && (favorite.getImageUrl().isEmpty()))
                && !Validations.isValidUrl(favorite.getImageUrl())) {
            result.addMessage("Image url must be a valid url");
        }

        if (favorite.getCreatedOn() != null) {
            if (favorite.getCreatedOn().isAfter(LocalDate.now())) {
                result.addMessage("Created on date cannot be in the future");
            }
        }

        if (favorite.getUpdatedOn() != null) {
            if (favorite.getUpdatedOn().isAfter(LocalDate.now())) {
                result.addMessage("Updated on date cannot be in the future");
            }
        }

        if (favorite.getCreatedOn() != null && favorite.getUpdatedOn() != null) {
            if (favorite.getUpdatedOn().isBefore(favorite.getCreatedOn())) {
                result.addMessage("Updated on date cannot be before created on date");
            }
        }
        if (isDuplicate(favorite)) {
            result.addMessage("Favorite cannot be a duplicate");
        }

        return result;
    }

    private boolean isDuplicate(Favorite favorite) {
        if(Objects.equals(favorite.getFavoriteId(), BigInteger.ZERO)) {
            List<Favorite> all = repository.findAll();
            for(Favorite f : all){
                if(favorite.equals(f)){
                    return true;
                }
            }
        }
        else {
            BigInteger favoriteId = favorite.getFavoriteId();
            List<Favorite> listWithoutCurrentObject = repository.findAll().stream().filter(f -> !f.getFavoriteId().equals(favoriteId)).toList();
            for(Favorite f : listWithoutCurrentObject){
                if(favorite.equals(f)){
                    return true;
                }
            }
        }
        return false;
    }
}