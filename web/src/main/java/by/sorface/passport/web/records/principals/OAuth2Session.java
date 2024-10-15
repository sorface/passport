package by.sorface.passport.web.records.principals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2Session implements Serializable {

    private UUID principleId;

    private String authorizationId;

    private String initiatorSystem;

}
