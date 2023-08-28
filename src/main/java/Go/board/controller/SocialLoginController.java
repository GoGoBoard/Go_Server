package Go.board.controller;

import Go.board.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/login/oauth2", produces = "application/json")
@RequiredArgsConstructor
public class SocialLoginController {

    SocialLoginService socialLoginService;

    @GetMapping("/code/{registrationId}")
    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        socialLoginService.socialLogin(code, registrationId);
    }
}
