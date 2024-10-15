package by.sorface.passport.web.records.sessions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    private String id;

    private String device;

    private String deviceBrand;

    private String deviceType;

    private String browser;

    private Long createdAt;

    private boolean active;

}
