package by.sorface.passport.web.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class NotFoundException extends GlobalSystemException {

    public NotFoundException(final String i18Code) {
        super(i18Code, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(final String i18Code, Map<String, String> args) {
        super(i18Code, args, HttpStatus.NOT_FOUND);
    }

}
