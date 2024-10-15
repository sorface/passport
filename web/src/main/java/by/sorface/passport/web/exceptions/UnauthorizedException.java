package by.sorface.passport.web.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class UnauthorizedException extends GlobalSystemException {

    public UnauthorizedException(final String i18Code, final Map<String, String> args) {
        super(i18Code, args, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(final String i18Code) {
        super(i18Code, HttpStatus.UNAUTHORIZED);
    }

}
