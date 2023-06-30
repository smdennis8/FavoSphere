package favorite.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Link {

    private String from;
    private String url;
    private LocalDateTime sentOn;

    public Link(String from, String url, LocalDateTime sentOn) {
        this.from = from;
        this.url = url;
        this.sentOn = sentOn;
    }

    public Link() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getSentOn() {
        return sentOn;
    }

    public void setSentOn(LocalDateTime sentOn) {
        this.sentOn = sentOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return getFrom().equalsIgnoreCase(link.getFrom()) &&
                getUrl().equalsIgnoreCase(link.getUrl()) &&
                getSentOn().isEqual(link.getSentOn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getUrl(), getSentOn());
    }
}
