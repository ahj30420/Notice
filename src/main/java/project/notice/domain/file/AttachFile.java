package project.notice.domain.file;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.notice.domain.notice.Notice;

@Entity
@Getter
@Table(name = "attachfile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private String realFileName;

    private String storeFileName;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static AttachFile createFile(String realFileName, String storeFileName){
        AttachFile img = new AttachFile();
        img.realFileName = realFileName;
        img.storeFileName = storeFileName;

        return img;
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addNotice(Notice notice) {
        this.notice = notice;
        notice.getFiles().add(this);
    }

}
