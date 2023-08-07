package Go.board.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberId;
    private String loginId;
    private String password;
    private String nickname;

}
