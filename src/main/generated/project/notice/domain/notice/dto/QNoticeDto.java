package project.notice.domain.notice.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * project.notice.domain.notice.dto.QNoticeDto is a Querydsl Projection type for NoticeDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QNoticeDto extends ConstructorExpression<NoticeDto> {

    private static final long serialVersionUID = -1871087189L;

    public QNoticeDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<java.time.LocalDateTime> startDateTime, com.querydsl.core.types.Expression<java.time.LocalDateTime> endDateTime, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdDateTime, com.querydsl.core.types.Expression<Integer> viewCount, com.querydsl.core.types.Expression<String> name) {
        super(NoticeDto.class, new Class<?>[]{long.class, String.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, int.class, String.class}, id, title, content, startDateTime, endDateTime, createdDateTime, viewCount, name);
    }

}

