package Go.board.controller;

import Go.board.dto.LoginDTO;
import Go.board.dto.MemberResponseDTO;
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
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;


    @PostMapping("/login")
    public ResponseEntity<MemberResponseDTO> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        MemberEntity loginMember = memberService.login(loginDTO);

        if (loginMember != null) {
            //로그인 성공, 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
            session.setAttribute("memberId", loginMember.getMemberId());

            session.setMaxInactiveInterval(1800);//30분 유지

            MemberResponseDTO dto = MemberResponseDTO.toMemberResponseDTO(loginMember);
            //닉네임 response
            return ResponseEntity.ok(dto);
        } else {
            //로그인 실패처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/join")
    public ResponseEntity<MemberResponseDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        MemberResponseDTO dto = memberService.registerMember(registerDTO);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
