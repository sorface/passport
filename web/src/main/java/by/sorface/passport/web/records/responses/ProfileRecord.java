package by.sorface.passport.web.records.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProfileRecord(UUID id,
                            String nickname,
                            String email,
                            String firstName,
                            String lastName,
                            String middleName,
                            String avatar,
                            List<String> roles) {
}
