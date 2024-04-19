package project.notice.repository.noticeRepository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.notice.domain.notice.Notice;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom{

    @EntityGraph(attributePaths = {"member"})
    Optional<Notice> findById(Long noticeId);

   @EntityGraph(attributePaths = {"files"})
    Notice findWithFileById(Long noticeId);
}
