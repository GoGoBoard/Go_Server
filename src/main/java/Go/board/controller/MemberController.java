package Go.board.controller;

import Go.board.dto.JoinRequest;
import Go.board.dto.LoginRequest;
import Go.board.entity.Member;
import Go.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody JoinRequest joinRequest) {
        // Check if loginId and nickname are not duplicate
        if (memberService.checkLoginIdDuplicate(joinRequest.getLoginId())
                || memberService.checkNicknameDuplicate(joinRequest.getNickname())) {
            return ResponseEntity.badRequest().build();
        }

        memberService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Member> login(@RequestBody LoginRequest loginRequest,
                                        HttpServletRequest httpServletRequest) {
        Member member = memberService.login(loginRequest);

        if (member != null) {
            // 로그인 성공 => 세션 생성
            // 세션을 생성하기 전에 기존의 세션 파기
            httpServletRequest.getSession().invalidate();

            HttpSession session = httpServletRequest.getSession(true);  // Session이 없으면 생성
            // 세션에 userId를 넣어줌
            session.setAttribute("memberId", member.getMemberId());
            session.setMaxInactiveInterval(1800); // Session이 30분동안 유지
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);  // Session이 없으면 null return
        if(session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }
}
