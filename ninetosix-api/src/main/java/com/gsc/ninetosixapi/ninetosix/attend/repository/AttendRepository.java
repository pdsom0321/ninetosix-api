package com.gsc.ninetosixapi.ninetosix.attend.repository;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AttendRepository extends JpaRepository<Attend, Long>, JpaSpecificationExecutor<Attend> {
    Optional<Attend> findByMemberAndAttendDate(Member member, String attendDate);
    List<Attend> findByMemberAndAttendDateContains(Member member, String attendDate);
    List<Attend> findByMemberAndAttendDateBetween(Member member, String startDate, String endDate);
}
