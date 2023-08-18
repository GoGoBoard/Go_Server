package Go.board.dto;

import Go.board.entity.MemberEntity;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDTO {
    private int memberId;
    @NotNull
    private String loginId;
    @NotNull
    private String password;
    @NotNull
    private String nickname;

    public static MemberEntity toEntity(RegisterDTO registerDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setLoginId(registerDTO.getLoginId());
        memberEntity.setNickname(registerDTO.getNickname());
        memberEntity.setPassword(registerDTO.getPassword());
        return memberEntity;
    }
}
