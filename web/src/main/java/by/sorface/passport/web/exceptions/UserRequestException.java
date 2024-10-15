package by.sorface.passport.web.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class UserRequestException extends GlobalSystemException {

    public UserRequestException(final String i18Code) {
        super(i18Code, HttpStatus.BAD_REQUEST);
    }

    public UserRequestException(final String i18Code, final Map<String, String> args) {
        super(i18Code, args, HttpStatus.BAD_REQUEST);
    }

}
