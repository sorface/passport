package by.sorface.passport.web.records.requests;

/**
 * application for user registration
 */
public record AccountSignup(
        /*
          user firstname
         */
        String firstName,

        /*
          user's lastname
         */
        String lastName,

        /*
          user's middleName
         */
        String middleName,

        /*
          user's email
         */
        String email,

        /*
          user's login in the system
         */
        String username,

        /*
          user's password in the system
         */
        String password) {
}
