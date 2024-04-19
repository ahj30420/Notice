package project.notice.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1368646885L;

    public static final QMember member = new QMember("member1");

    public final project.notice.domain.QBaseEntity _super = new project.notice.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDateTime = _super.lastModifiedDateTime;

    public final StringPath name = createString("name");

    public final ListPath<project.notice.domain.notice.Notice, project.notice.domain.notice.QNotice> notices = this.<project.notice.domain.notice.Notice, project.notice.domain.notice.QNotice>createList("notices", project.notice.domain.notice.Notice.class, project.notice.domain.notice.QNotice.class, PathInits.DIRECT2);

    public final StringPath pw = createString("pw");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

