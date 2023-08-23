package Go.board.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "file")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post postId; // 첨부된 게시글

    private String url;

    @Column(name = "file_name")
    private String fileName;
}
