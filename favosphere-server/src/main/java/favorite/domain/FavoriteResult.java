package favorite.domain;

import favorite.models.Favorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteResult {

    //Fields
    private final List<String> messages = new ArrayList<>();
    private Favorite favorite;
    private ResultType resultType = ResultType.SUCCESS;

    //Getters and Setters
    public List<String> getMessages(){
        return messages;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public ResultType getResultType() {
        return resultType;
    }

    //Methods
    public void addMessage(String message) {
        addMessage(message, ResultType.INVALID);
    }

    public void addMessage(String message, ResultType resultType) {
        this.resultType = resultType;
        messages.add(message);
    }

    public boolean isSuccess() {
        return resultType == ResultType.SUCCESS;
    }
}