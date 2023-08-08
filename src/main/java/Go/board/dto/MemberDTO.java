package Go.board.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드를 매개변수로 하는 생성자
public class MemberDTO {
    private int memberId;
    private String loginId;
    private String password;
    private String nickname;


}
