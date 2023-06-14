package com.gsc.ninetosixapi.ninetosix.attend.repository;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public interface AttendRepository extends JpaRepository<Attend, Long>, JpaSpecificationExecutor<Attend> {
    Attend findByMemberAndAttendDate(Member member, String attendDate);
    List<Attend> findByMemberAndAttendDateContainsOrderByAttendDateAsc(Member member, String attendDate);
    ArrayList<Attend> findTop2ByMemberAndAttendDateBetweenOrderByAttendDateAsc(Member member, String startDate, String endDate);
    ArrayList<Attend> findTop2ByMemberIdAndAttendDateBetweenOrderByAttendDateAsc(Long memberId, String startDate, String endDate);
}
