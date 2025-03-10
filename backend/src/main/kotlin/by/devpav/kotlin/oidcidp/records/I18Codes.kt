package by.devpav.kotlin.oidcidp.records

class I18Codes {
    object I18GlobalCodes {
        const val UNKNOWN_ERROR: String = "global.unknown_error"

        const val ACCESS_DENIED: String = "global.access_denied"

        const val VALIDATION_ERROR: String = "global.validation_error"

        const val OBJECT_IS_NOT_SUPPORTED: String = "global.object_is_not_supported"
    }

    object I18AuthenticationCodes {
        const val UNKNOWN_ERROR: String = "authentication.unknown"

        const val PASSWORD_OR_USERNAME_INVALID: String = "authentication.password_or_username_invalid"
        const val NOT_AUTHENTICATED: String = "authentication.not_authenticated"
    }

    object I18SessionCodes {
        const val DELETE_ACTIVE_SESSION_ERROR: String = "session.delete_active_session_error"
        const val UNKNOWN_SESSION_ID: String = "session.unknown_session"
    }

    object I18CsrfCodes {
        const val INVALID: String = "csrf.invalid"
    }

    object I18OtpCodes {
        const val INVALID_CODE: String = "otp.invalid_code"
        const val EXPIRED_CODE: String = "otp.expired_code"
    }

    object SELF {
        const val NOT_FOUND_KEY: String = "i18.not_found_template"
    }

    object I18UserCodes {
        const val ALREADY_EXISTS_WITH_THIS_EMAIL: String = "user.already_exists_with_this_email"

        const val ALREADY_EXISTS_WITH_THIS_LOGIN: String = "user.already_exists_with_this_login"

        const val NOT_FOUND_BY_EMAIL: String = "user.not_found_by_email"

        const val NOT_FOUND_BY_ID: String = "user.not_found_by_id"

        const val NOT_FOUND_BY_LOGIN: String = "user.not_found_by_login"

        const val ALREADY_AUTHENTICATED: String = "user.already_authenticated"

        const val EMAIL_NOT_VALID: String = "user.email.not_valid"
    }

    object I18AccountRegistryCodes {
        const val ACCOUNT_DATA_NOT_FOUND: String = "account.registry.data_not_found"
        const val ACCOUNT_DATA_NOT_FOUND_BY_ID: String = "account.registry.data_not_found_by_id"
    }

    object I18ClientCodes {
        const val NOT_FOUND: String = "client.not_found"

        const val REDIRECT_URL_NOT_VALID: String = "client.redirect_url_not_valid"

        const val NOT_FOUND_BY_ID: String = "client.not_found_by_id"

        const val ID_IS_INVALID: String = "client.id_is_invalid"

        const val ID_CANNOT_BE_EMPTY: String = "client.id_cannot_be_empty"

        const val ID_MUST_BE_SET: String = "client.id_must_be_set"

        const val NAME_MUST_BE_TRANSMITTED: String = "client.name_must_be_transmitted"
    }

    object I18TokenCodes {
        const val INVALID: String = "token.invalid"

        const val EXPIRED: String = "token.expired"

        const val NOT_FOUND: String = "token.not_found"

        const val INVALID_OPERATION_TYPE: String = "token.invalid_operation_type"
    }

    object I18EmailCodes {
        const val TEMPLATE: String = "email.template"

        const val CONFIRMATION_REGISTRATION: String = "email.confirmation_registration"

        const val HTML_TEMPLATE_OTP: String = "email.template.otp"

        const val HTML_TEMPLATE_SUCCESS_REGISTRATION: String = "email.template.success_registration"

        const val SUBJECT_TITLE_RESET_PASSWORD: String = "email.title_reset_password"
        const val SUBJECT_SUCCESS_REGISTRATION: String = "email.subject_success_registration"

        const val TEMPLATE_RENEW_PASSWORD: String = "email.template_renew_password"
    }
}
