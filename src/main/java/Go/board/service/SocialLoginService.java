package Go.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class SocialLoginService {
    public void socialLogin(String code, String registrationId) {
        System.out.println("code = " + code);
        System.out.println("registrationId = " + registrationId);
    }
}
