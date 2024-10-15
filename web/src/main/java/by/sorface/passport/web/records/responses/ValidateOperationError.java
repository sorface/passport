package by.sorface.passport.web.records.responses;

import lombok.Data;

import java.util.List;

public record ValidateOperationError(String message, List<ValidateError> errors, int code, String spanId, String traceId) {

    @Data
    public static class ValidateError {

        private String field;

        private String message;

    }

}
