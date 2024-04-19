package project.notice.domain.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.notice.domain.BaseEntity;
import project.notice.domain.file.AttachFile;
import project.notice.domain.member.Member;
import project.notice.domain.notice.dto.EditNotice;
import project.notice.exception.DateSettingException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "notice")
    private List<AttachFile> files = new ArrayList<>();

    private String title;

    private String content;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int viewCount;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Notice createNotice(Member member, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Notice notice = new Notice();
        notice.addMember(member);
        notice.title = title;
        notice.content = content;
        notice.startDateTime = startDateTime;
        notice.endDateTime = endDateTime;

        if(startDateTime.isAfter(endDateTime)){
            throw new DateSettingException("공지 시작일과 마감일을 다시 설정해주세요");
        }
        
        return notice;
    }

    //----------------------------------------수정 메서드-------------------------------------------------------
    public void increaseViewCount(){
        this.viewCount++;
    }

    public void edit(EditNotice editNotice) {
        if(editNotice.getTitle() != null){
            this.title = editNotice.getTitle();
        }
        if(editNotice.getContent() != null){
            this.content = editNotice.getContent();
        }
        if(editNotice.getStartDateTime() != null){
            this.startDateTime = editNotice.getStartDateTime();
        }
        if(editNotice.getEndDateTime() != null){
            this.endDateTime = editNotice.getEndDateTime();
        }
        if(startDateTime.isAfter(endDateTime)){
            throw new DateSettingException("공지 시작일과 마감일을 다시 설정해주세요");
        }
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addMember(Member member) {
        this.member = member;
        member.getNotices().add(this);
    }

}
