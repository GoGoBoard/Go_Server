package Go.board.dto;

import Go.board.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;

    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickname;

    // 비밀번호 암호화 X
    public Member toEntity() {
        return Member.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }

    // 비밀번호 암호화
    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .nickname(this.nickname)
                .build();
    }
}