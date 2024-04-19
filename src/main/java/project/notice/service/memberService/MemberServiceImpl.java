package project.notice.service.memberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.notice.domain.member.Member;
import project.notice.domain.member.Role;
import project.notice.domain.member.dto.JoinDto;
import project.notice.domain.member.dto.LoginDto;
import project.notice.domain.member.dto.MemberDto;
import project.notice.exception.DuplicatedNameException;
import project.notice.exception.LoginException;
import project.notice.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 이름으로 회원 정보 조회
     */
    public MemberDto findByName(String name){
        Member member = memberRepository.findByName(name);
        return new MemberDto(member.getName(), member.getRole());
    }

    /**
     * 회원가입
     * - 이름 중복 확인
     * - 비밀번호는 보안이 중요한 개인 정보이므로 데이터베이스에 저장하기 전에 암호화!!
     */
    @Override
    public void register(JoinDto joinDto) {

        ValidateDuplicateName(joinDto);

        String name = joinDto.getName();
        String pw = bCryptPasswordEncoder.encode(joinDto.getPw());
        Role role = joinDto.getRole();

        Member member = Member.createMember(name,pw,role);
        memberRepository.save(member);
    }

    /**
     * 로그인
     * - 이름과 패스워드 일치 여부 확인
     */
    @Override
    public MemberDto login(LoginDto loginDto) {

        Member member = memberRepository.findByName(loginDto.getName());
        passwordCheck(member, loginDto.getPw());

        String name = member.getName();
        Role role = member.getRole();

        MemberDto memberDto = new MemberDto(name, role);
        return memberDto;
    }

    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 회원가입 시 아이디 중복 체크 메서드
     */
    private void ValidateDuplicateName(JoinDto joinDto){
        Optional.ofNullable(memberRepository.findDuplicatedByName(joinDto.getName()))
                .ifPresent(member -> {
                    throw new DuplicatedNameException("이미 사용 중인 아이디 입니다.");
                });
    }

    /**
     * 비밀번호 일치 확인
     */
    private void passwordCheck(Member member, String pw){
        if(member == null || !bCryptPasswordEncoder.matches(pw, member.getPw())){
            throw new LoginException("아이디와 비밀번호를 확인해 주세요.");
        }
    }

}
