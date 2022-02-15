package tk.finedesk.finedesk.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for password validation.
 *
 * Password must contain at least one digit [0-9].
 * Password must contain at least one lowercase Latin character [a-z].
 * Password must contain at least one uppercase Latin character [A-Z].
 * Password must contain at least one special character like ! @ # & ( ).
 * Password must contain a length of at least 8 characters and a maximum of 20 characters.
 */

@Component
public class PasswordValidator {

    private final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public boolean isPasswordValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


}
