package project.notice.repository.FileRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import project.notice.domain.file.AttachFile;
import project.notice.domain.notice.Notice;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<AttachFile, Long>, FileRepositoryCustom{

    @Query("select f from AttachFile f where f.notice.id = :noticeId")
    List<AttachFile> findByNoticeId(Long noticeId);

    @Modifying
    @Query("delete from AttachFile f where f.notice.id = :noticeId")
    void deleteNotice(Long noticeId);

}
