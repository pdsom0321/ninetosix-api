package com.gsc.ninetosixapi.ninetosix.attend.repository;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AttendRepository extends JpaRepository<Attend, Long>, JpaSpecificationExecutor<Attend> {
    Attend findByMemberAndAttendDate(Member member, String attendDate);
    Optional<Attend> findByMemberIdAndAttendDate(Long memberId, String attendDate);
    List<Attend> findByMemberIdAndAttendDateContainsOrderByAttendDateAsc(Long memberId, String attendDate);
    // ArrayList<Attend> findTop2ByMemberAndAttendDateBetweenOrderByAttendDateAsc(Member member, String startDate, String endDate);
    ArrayList<Attend> findTop2ByMemberIdAndAttendDateBetweenOrderByAttendDateAsc(Long memberId, String startDate, String endDate);
}
