package favorite.models;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

public class Email {
    private BigInteger emailId;
    private BigInteger userId;
    private LocalDateTime time;
    private String url;

    public Email() {
    }

    public Email(BigInteger emailId, BigInteger userId, String url, LocalDateTime time) {
        this.emailId = emailId;
        this.userId = userId;
        this.url = url;
        this.time = time;
    }

    public BigInteger getEmailId() {
        return emailId;
    }

    public void setEmailId(BigInteger emailId) {
        this.emailId = emailId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return getEmailId().equals(email.getEmailId()) &&
                getUserId().equals(email.getUserId()) &&
                getUrl().equalsIgnoreCase(email.getUrl()) &&
                getTime().isEqual(email.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmailId(), getUserId(), getUrl(), getTime());
    }
}
