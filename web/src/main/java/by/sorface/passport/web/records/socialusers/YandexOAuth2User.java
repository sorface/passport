package by.sorface.passport.web.records.socialusers;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class YandexOAuth2User implements SocialOAuth2User {

    private String id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String middleName;

    private String avatarUrl;

    private Map<String, Object> attributes;

}
