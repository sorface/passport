package by.sorface.passport.web.records.requests;

import lombok.Data;

import java.util.Set;

@Data
public class ApplicationClientPatchRequest {

    /**
     * Application client name
     */
    private String name;

    /**
     * Application client redirect urls
     */
    private Set<String> redirectionUrls;

}
