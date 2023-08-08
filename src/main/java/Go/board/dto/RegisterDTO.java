package Go.board.dto;

import Go.board.entity.MemberEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDTO {
    private int memberId;
    private String loginId;
    private String password;
    private String nickname;

    public static MemberEntity toEntity(RegisterDTO registerDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberId(registerDTO.getMemberId());
        memberEntity.setLoginId(registerDTO.getLoginId());
        memberEntity.setNickname(registerDTO.getNickname());
        memberEntity.setPassword(registerDTO.getPassword());
        return memberEntity;
    }
}
