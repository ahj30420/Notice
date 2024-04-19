package project.notice.domain.file;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttachFile is a Querydsl query type for AttachFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttachFile extends EntityPathBase<AttachFile> {

    private static final long serialVersionUID = 833062858L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttachFile attachFile = new QAttachFile("attachFile");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.notice.domain.notice.QNotice notice;

    public final StringPath realFileName = createString("realFileName");

    public final StringPath storeFileName = createString("storeFileName");

    public QAttachFile(String variable) {
        this(AttachFile.class, forVariable(variable), INITS);
    }

    public QAttachFile(Path<? extends AttachFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttachFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttachFile(PathMetadata metadata, PathInits inits) {
        this(AttachFile.class, metadata, inits);
    }

    public QAttachFile(Class<? extends AttachFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notice = inits.isInitialized("notice") ? new project.notice.domain.notice.QNotice(forProperty("notice"), inits.get("notice")) : null;
    }

}

