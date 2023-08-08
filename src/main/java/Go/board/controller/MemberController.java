package Go.board.controller;

import Go.board.dto.LoginDTO;
import Go.board.dto.RegisterDTO;
import Go.board.entity.MemberEntity;
import Go.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;


    @PostMapping("/login")
    public ResponseEntity<String> login(LoginDTO loginDTO, HttpSession session) {
        MemberEntity loginMember = memberService.login(loginDTO);
        if (loginMember != null) {
            //로그인 성공, 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
            session.setAttribute("memberId", loginMember.getMemberId());
            session.setMaxInactiveInterval(1800);//30분 유지
            return ResponseEntity.ok("로그인 성공");
        } else {
            //로그인 실패처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 일치하지 않습니다");
        }
    }

    @PostMapping("/join")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        boolean registerOk = memberService.registerMember(registerDTO);
        if (registerOk) {
            return ResponseEntity.ok("회원가입에 성공하였습니다.");
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원가입에 실패하였습니다.(중복된 아이디 존재)");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃되었습니다.");
    }
}
