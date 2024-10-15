package by.sorface.passport.web.records.responses;

public record OperationError(
        String message,
        String details,
        int code,
        String spanId,
        String traceId
) {
}
