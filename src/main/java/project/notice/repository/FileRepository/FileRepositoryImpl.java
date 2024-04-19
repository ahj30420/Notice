package project.notice.repository.FileRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import project.notice.domain.file.dto.UploadFile;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepositoryCustom{
    
    private final JdbcTemplate jdbcTemplate;

    /**
     * 첨부 파일 한번에 저장하기
     * @param noticeId: 공지사항 Id
     * @param uploadFiles: 첨부파일 이름 목록
     */
    @Override
    public void bulkSave(Long noticeId, List<UploadFile> uploadFiles) {

        jdbcTemplate.batchUpdate("insert into attachfile(notice_id, real_file_name, store_file_name) values(?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, noticeId);
                        ps.setString(2, uploadFiles.get(i).getRealFileName());
                        ps.setString(3, uploadFiles.get(i).getStoreFileName());
                    }

                    @Override
                    public int getBatchSize() {
                        return uploadFiles.size();
                    }
                }
        );
    }
    
}
