package project.notice.domain.notice;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotice is a Querydsl query type for Notice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotice extends EntityPathBase<Notice> {

    private static final long serialVersionUID = -526957787L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotice notice = new QNotice("notice");

    public final project.notice.domain.QBaseEntity _super = new project.notice.domain.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final DateTimePath<java.time.LocalDateTime> endDateTime = createDateTime("endDateTime", java.time.LocalDateTime.class);

    public final ListPath<project.notice.domain.file.AttachFile, project.notice.domain.file.QAttachFile> files = this.<project.notice.domain.file.AttachFile, project.notice.domain.file.QAttachFile>createList("files", project.notice.domain.file.AttachFile.class, project.notice.domain.file.QAttachFile.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDateTime = _super.lastModifiedDateTime;

    public final project.notice.domain.member.QMember member;

    public final DateTimePath<java.time.LocalDateTime> startDateTime = createDateTime("startDateTime", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QNotice(String variable) {
        this(Notice.class, forVariable(variable), INITS);
    }

    public QNotice(Path<? extends Notice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotice(PathMetadata metadata, PathInits inits) {
        this(Notice.class, metadata, inits);
    }

    public QNotice(Class<? extends Notice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new project.notice.domain.member.QMember(forProperty("member")) : null;
    }

}

