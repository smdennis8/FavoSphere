package favorite.domain;

import favorite.data.EmailRepository;
import favorite.models.Email;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

@Service
public class EmailService {

    private final EmailRepository repository;

    public EmailService(EmailRepository repository) {
        this.repository = repository;
    }

    public List<Email> findAll() {
        return repository.findAll();
    }

    public Email findById(BigInteger emailId) {
        return repository.findById(emailId);
    }

    public List<Email> findByUserId(BigInteger appUserId) {
        return repository.findByUserId(appUserId);
    }

    public Result<Email> create(Email email) {
        Result<Email> result = validate(email);

        if (!result.isSuccess()) {
            return result;
        }

        if (email.getEmailId().compareTo(BigInteger.ZERO) > 0) {
            result.addMessage("Cannot create existing email");
            return result;
        }

        email = repository.create(email);
        result.setPayload(email);
        return result;
    }

    public Result<Email> deleteById(BigInteger emailId) {
        Result<Email> result = new Result<>();
        if (!repository.deleteById(emailId)) {
            result.addMessage(String.format("Email: %s doesn't exist", emailId), ResultType.NOT_FOUND);
        }
        return result;
    }

    private Result<Email> validate(Email email) {
        Result<Email> result = new Result<>();

        if (email == null) {
            result.addMessage("Email cannot be null");
            return result;
        }

        if (email.getUrl() == null || email.getUrl().isBlank()) {
            result.addMessage("Url is required");
        }

        if (!Validations.isValidUrl(email.getUrl())) {
            result.addMessage("Url must be a valid url");
        }

        if (email.getTime() == null) {
            result.addMessage("Email sent date is required");
        }

        if (email.getTime() != null) {
            if (email.getTime().isAfter(ChronoLocalDateTime.from(LocalDateTime.now()))) {
                result.addMessage("Email sent date cannot be in the future");
            }
        }

        return result;
    }
}