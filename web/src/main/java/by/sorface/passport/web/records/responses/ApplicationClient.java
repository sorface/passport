package by.sorface.passport.web.records.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationClient {

    private String id;

    private String clientId;

    private String clientSecret;

    private String clientName;

    private LocalDateTime issueTime;

    private LocalDateTime expiresAt;

    private Set<String> redirectUrls;

}
