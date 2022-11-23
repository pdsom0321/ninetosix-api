package com.gsc.ninetosixapi.ninetosix.attend.repository;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AttendRepository extends JpaRepository<Attend, Long>, JpaSpecificationExecutor<Attend> {
    Attend findByMember(Member member);
    Optional<Attend> findByMemberAndAttendDate(Member member, String attendDate);
    List<Attend> findByMemberAndAttendDateContainsOOrderByAttendDateAsc(Member member, String attendDate);
    ArrayList<Attend> findTop2ByMemberAndAttendDateBetweenOrderByAttendDateAsc(Member member, String startDate, String endDate);
}
