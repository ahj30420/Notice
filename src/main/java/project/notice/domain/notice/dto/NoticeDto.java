package project.notice.domain.notice.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.notice.domain.member.Role;
import project.notice.domain.member.dto.MemberDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime createdDateTime;

    private int viewCount;

    private MemberDto memberDto;

    @QueryProjection
    public NoticeDto(Long id, String title, String content,
                     LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createdDateTime,
                      int viewCount, String name) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdDateTime = createdDateTime;
        this.viewCount = viewCount;
        this.memberDto = new MemberDto(name, Role.ADMIN);
    }

}
