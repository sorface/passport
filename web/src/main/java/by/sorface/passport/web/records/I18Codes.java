package by.sorface.passport.web.records;

public class I18Codes {

    public static class I18GlobalCodes {

        public static final String UNKNOWN_ERROR = "global.unknown_error";

        public static final String ACCESS_DENIED = "global.access_denied";

        public static final String VALIDATION_ERROR = "global.validation_error";

        public static final String OBJECT_IS_NOT_SUPPORTED = "global.object_is_not_supported";

    }

    public static class I18UserCodes {

        public static final String ALREADY_EXISTS_WITH_THIS_EMAIL = "user.already_exists_with_this_email";

        public static final String ALREADY_EXISTS_WITH_THIS_LOGIN = "user.already_exists_with_this_login";

        public static final String NOT_FOUND_BY_EMAIL = "user.not_found_by_email";

        public static final String NOT_FOUND_BY_ID = "user.not_found_by_id";

        public static final String NOT_FOUND_BY_LOGIN = "user.not_found_by_login";

        public static final String ALREADY_AUTHENTICATED = "user.already_authenticated";

        public static final String EMAIL_NOT_VALID = "user.email.not_valid";

    }

    public static class I18ClientCodes {

        public static final String NOT_FOUND = "client.not_found";

        public static final String REDIRECT_URL_NOT_VALID = "client.redirect_url_not_valid";

        public static final String NOT_FOUND_BY_ID = "client.not_found_by_id";

        public static final String ID_IS_INVALID = "client.id_is_invalid";

        public static final String ID_CANNOT_BE_EMPTY = "client.id_cannot_be_empty";

        public static final String ID_MUST_BE_SET = "client.id_must_be_set";

        public static final String NAME_MUST_BE_TRANSMITTED = "client.name_must_be_transmitted";

    }

    public static class I18TokenCodes {

        public static final String INVALID = "token.invalid";

        public static final String EXPIRED = "token.expired";

        public static final String NOT_FOUND = "token.not_found";

        public static final String INVALID_OPERATION_TYPE = "token.invalid_operation_type";

    }

    public static class I18EmailCodes {

        public static final String TEMPLATE = "email.template";

        public static final String CONFIRMATION_REGISTRATION = "email.confirmation_registration";

        public static final String SUBJECT_TITLE_RESET_PASSWORD = "email.title_reset_password";

        public static final String TEMPLATE_RENEW_PASSWORD = "email.template_renew_password";

    }

}
