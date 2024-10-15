package by.sorface.passport.web.records.responses;

import java.util.UUID;

public record UserRegisteredHash(UUID id, String email, String hash, String firstName, String lastName) {
}
