package Go.board.dto;

import Go.board.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberResponseDTO {
    private String nickname;

    public static MemberResponseDTO toMemberResponseDTO(MemberEntity entity) {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.nickname = entity.getNickname();
        return dto;
    }
}
