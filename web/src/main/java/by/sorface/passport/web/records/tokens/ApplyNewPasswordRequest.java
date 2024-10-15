package by.sorface.passport.web.records.tokens;

public record ApplyNewPasswordRequest(
        String newPassword,
        String hashToken
) {
}
