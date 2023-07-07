package favorite.domain;
import org.apache.commons.validator.routines.UrlValidator;
import java.util.regex.Pattern;

public class Validations {

    final static String pattern =
            "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()!@:%_\\+.~#?&\\/\\/=]*)";

    public static boolean isValidUrl(String input) {
        UrlValidator validator = new UrlValidator();
        return validator.isValid(input);
    }
}