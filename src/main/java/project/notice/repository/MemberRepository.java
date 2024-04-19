package project.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.notice.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findDuplicatedByName(String name);

    Member findByName(String name);

}
