package by.sorface.passport.web.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GlobalSystemException extends RuntimeException implements SystemException {

    private final Map<String, String> args = new HashMap<>();

    private final String i18Code;

    private final HttpStatus httpStatus;

    public GlobalSystemException(final String i18Code, final HttpStatus httpStatus) {
        this.i18Code = i18Code;
        this.httpStatus = httpStatus;
    }

    public GlobalSystemException(final String i18Code, final Map<String, String> args, HttpStatus httpStatus) {
        this(i18Code, httpStatus);
        this.args.putAll(args);
    }
}
