package by.sorface.passport.web.converters;

import by.sorface.passport.web.dao.models.OAuth2Client;
import by.sorface.passport.web.records.responses.ApplicationClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class OAuth2ClientConverter {

    /**
     * The convert function takes an OAuth2Client object and a clientSecret string,
     * and returns an ApplicationClient object. The function first creates a new
     * ApplicationClient object using the source's id, name, description, websiteUrl
     * and logoUrl fields. It then sets the application client's redirectUris field to
     * be equal to the source's redirectUris field (which is of type Set&lt;String&gt;).
     *
     * @param source Get the clientsecret
     *               public applicationclient convert(final oauth2client source, final string clientsecret) {
     *               return new applicationclient() {{
     *               setid(source
     * @return An applicationclient
     */
    public ApplicationClient convert(final OAuth2Client source) {
        return convert(source, source.getClientSecret());
    }

    /**
     * The convertWithoutSecret function is used to convert an OAuth2Client object into an ApplicationClient object.
     * The function takes in a source OAuth2Client and a null secret, and returns the converted ApplicationClient.
     *
     * @param source Convert the oauth2client to an applicationclient
     * @return An applicationclient object
     */
    public ApplicationClient convertWithoutSecret(final OAuth2Client source) {
        return convert(source, null);
    }

    /**
     * The convert function is used to convert an OAuth2Client object into an ApplicationClient object.
     * The function takes in two parameters: the source and clientSecret.
     * The source parameter is the OAuth2Client that will be converted into an Application Client, while the clientSecret parameter is a string containing the secret of this application's client.
     * This function returns a new Application Client with all of its fields set to those of our original OAuth2Client,
     * except for its id field which has been converted from a UUID to a String and its redirectUrls field which has been split by semicolons (;) and
     *
     * @param source Get the clientid, clientname, and redirecturis
     * @param clientSecret Pass the client secret to the applicationclient object
     * @return A new applicationclient object
     */
    public ApplicationClient convert(final OAuth2Client source, final String clientSecret) {
        return ApplicationClient.builder()
                .id(source.getId().toString())
                .clientId(source.getClientId())
                .clientName(source.getClientName())
                .clientSecret(clientSecret)
                .redirectUrls(Arrays.stream(source.getRedirectUris().split(";")).collect(Collectors.toSet()))
                .expiresAt(source.getClientSecretExpiresAt())
                .issueTime(source.getClientIdIssueAt())
                .build();
    }

}
