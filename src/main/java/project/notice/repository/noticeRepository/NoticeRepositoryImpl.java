package project.notice.repository.noticeRepository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.notice.domain.notice.dto.NoticeDto;
import project.notice.domain.notice.dto.QNoticeDto;

import java.time.LocalDateTime;

import static project.notice.domain.notice.QNotice.notice;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    /**
     * 공지 사항 목록 조회 (페이징 처리)
     * 1. 공지 사항 시작 일시와 마감 일시에 부합하는 공지 사항만 조회
     * 2. orderBy: 가장 최근에 수정 및 등록 된 공지 사항을 기준으로 정렬
     */
    @Override
    public Page<NoticeDto> getNoticeList(Pageable pageable) {

        QueryResults<NoticeDto> results = queryFactory
                .select(new QNoticeDto(
                        notice.id, notice.title, notice.content,
                        notice.startDateTime, notice.endDateTime, notice.createDateTime,
                        notice.viewCount, notice.member.name))
                .from(notice)
                .leftJoin(notice.member)
                .where(notice.startDateTime.before(LocalDateTime.now()), notice.endDateTime.after(LocalDateTime.now()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.lastModifiedDateTime.desc())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
