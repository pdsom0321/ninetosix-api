package com.gsc.ninetosixapi.ninetosix.myPage.service;

import com.gsc.ninetosixapi.ninetosix.myPage.dto.MyPageDto;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;

    public MyPageDto getMemberInfo(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        return MyPageDto.from(member);
    }

    public void modify(MyPageDto myPageDto){
        long id = 0L;
        Optional<Member> memberInfo = memberRepository.findByEmail(myPageDto.getEmail());
        if(memberInfo != null){
            // TODO [MyPage] 수정 로직 개발 필요
            // userInfo.get().setName(myPageDto.getName());
        }
    }
}
