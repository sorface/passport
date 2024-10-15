package by.sorface.passport.web.records.responses;

import java.util.UUID;

public record UserConfirm(UUID id, String email, boolean confirm) {
}
