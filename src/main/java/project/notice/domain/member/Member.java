package project.notice.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.notice.domain.BaseEntity;
import project.notice.domain.notice.Notice;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Notice> notices = new ArrayList<>();

    @Column(unique = true)
    private String name;

    private String pw;

    @Enumerated(EnumType.STRING)
    private Role role;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Member createMember(String name, String pw, Role role){
        Member member = new Member();
        member.name = name;
        member.pw = pw;
        member.role = role;

        return member;
    }

    //----------------------------------------수정 메서드-------------------------------------------------------
    public void changeRole(){
        this.role = Role.ADMIN;
    }

}
