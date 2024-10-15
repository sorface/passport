package by.sorface.passport.web.controllers;

import by.sorface.passport.web.facade.accounts.RenewPasswordFacade;
import by.sorface.passport.web.records.accounts.AccountRenewPasswordRequest;
import by.sorface.passport.web.records.tokens.ApplyNewPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts/passwords")
public class RenewPasswordController {

    private final RenewPasswordFacade renewPasswordFacade;

    @PostMapping("/forget")
    public ResponseEntity<?> forgetPassword(@RequestBody @Valid AccountRenewPasswordRequest request) {
        renewPasswordFacade.forgetPassword(request.email());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/new/apply")
    public ResponseEntity<?> applyNewPassword(@RequestBody @Valid ApplyNewPasswordRequest request) {
        renewPasswordFacade.applyNewPassword(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/hash/introspect")
    public ResponseEntity<?> checkRenewPasswordToken(@RequestParam("hash") String hash) {
        renewPasswordFacade.checkRenewPasswordToken(hash);

        return ResponseEntity.ok().build();
    }

}
