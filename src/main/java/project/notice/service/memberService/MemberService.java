package project.notice.service.memberService;

import project.notice.domain.member.dto.JoinDto;
import project.notice.domain.member.dto.LoginDto;
import project.notice.domain.member.dto.MemberDto;

public interface MemberService {

    MemberDto findByName(String name);

    void register(JoinDto joinDto);

    MemberDto login(LoginDto loginDto);
}
