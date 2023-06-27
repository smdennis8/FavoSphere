package favorite.models;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

public class Favorite {

    private BigInteger favoriteId;
    private BigInteger userId;
    private String url;
    private String source;
    private String creator;
    private String type;
    private String title;
    private String description;
    private String gifUrl;
    private String imageUrl;
    private LocalDate createdOn;
    private LocalDate updatedOn;

    private Boolean isCustomTitle;
    private Boolean isCustomDescription;
    private Boolean isCustomImage;
    private Boolean isCustomGif;

<<<<<<< Updated upstream
    public Favorite(BigInteger favoriteId, BigInteger userId, String url, String source, String creator,
                    String type, String title, String description, String gifUrl, String imageUrl, LocalDate createdOn,
=======
    public Favorite() {
    }

    public Favorite(BigInteger favoriteId, BigInteger userId, String url, String source, String creator, String type,
                    String title, String description, String gifUrl, String imageUrl, LocalDate createdOn,
>>>>>>> Stashed changes
                    LocalDate updatedOn, Boolean isCustomTitle, Boolean isCustomDescription, Boolean isCustomImage,
                    Boolean isCustomGif) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.url = url;
        this.source = source;
        this.creator = creator;
        this.type = type;
        this.title = title;
        this.description = description;
        this.gifUrl = gifUrl;
        this.imageUrl = imageUrl;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.isCustomTitle = isCustomTitle;
        this.isCustomDescription = isCustomDescription;
        this.isCustomImage = isCustomImage;
        this.isCustomGif = isCustomGif;
    }

    public BigInteger getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(BigInteger favoriteId) {
        this.favoriteId = favoriteId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Boolean getCustomTitle() {
        return isCustomTitle;
    }

    public void setCustomTitle(Boolean customTitle) {
        isCustomTitle = customTitle;
    }

    public Boolean getCustomDescription() {
        return isCustomDescription;
    }

    public void setCustomDescription(Boolean customDescription) {
        isCustomDescription = customDescription;
    }

    public Boolean getCustomImage() {
        return isCustomImage;
    }

    public void setCustomImage(Boolean customImage) {
        isCustomImage = customImage;
    }

    public Boolean getCustomGif() {
        return isCustomGif;
    }

    public void setCustomGif(Boolean customGif) {
        isCustomGif = customGif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return getFavoriteId().equals(favorite.getFavoriteId()) &&
                getUserId().equals(favorite.getUserId()) &&
                getUrl().equalsIgnoreCase(favorite.getUrl()) &&
                getSource().equalsIgnoreCase(favorite.getSource()) &&
                getCreator().equalsIgnoreCase(favorite.getCreator()) &&
                getTitle().equalsIgnoreCase(favorite.getTitle()) &&
                getDescription().equalsIgnoreCase(favorite.getDescription()) &&
                getGifUrl().equalsIgnoreCase(favorite.getGifUrl()) &&
                getImageUrl().equalsIgnoreCase(favorite.getImageUrl()) &&
                getCreatedOn().equals(favorite.getCreatedOn()) &&
                getUpdatedOn().equals(favorite.getUpdatedOn()) &&
                isCustomTitle.equals(favorite.isCustomTitle) &&
                isCustomDescription.equals(favorite.isCustomDescription) &&
                isCustomImage.equals(favorite.isCustomImage) &&
                isCustomGif.equals(favorite.isCustomGif);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFavoriteId(), getUserId(), getUrl(), getSource(), getCreator(), getTitle(),
                getDescription(), getGifUrl(), getImageUrl(), getCreatedOn(), getUpdatedOn(), isCustomTitle,
                isCustomDescription, isCustomImage, isCustomGif);
    }
}
