package by.sorface.passport.web.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ObjectInvalidException extends GlobalSystemException {

    public ObjectInvalidException(final String i18Code) {
        super(i18Code, HttpStatus.BAD_REQUEST);
    }

    public ObjectInvalidException(final String i18Code, final Map<String, String> args) {
        super(i18Code, args, HttpStatus.BAD_REQUEST);
    }

}
