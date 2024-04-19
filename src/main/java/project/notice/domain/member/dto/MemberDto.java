package project.notice.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.notice.domain.member.Role;

import java.io.Serializable;

/**
 * 세션에 저장 할 사용자 정보
 */
@Data
@AllArgsConstructor
public class MemberDto implements Serializable {

    private String name;
    private Role role;

}
