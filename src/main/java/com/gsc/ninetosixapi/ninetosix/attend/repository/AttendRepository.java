package com.gsc.ninetosixapi.ninetosix.attend.repository;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AttendRepository extends JpaRepository<Attend, Long>, JpaSpecificationExecutor<Attend> {
    Optional<Attend> findByAttendDateAndMemberId(String attendDate, Long memberId);
    List<Attend> findByMemberIdAndAttendDateContainsOrderByAttendDateAsc(Long memberId, String attendDate);
    ArrayList<Attend> findTop2ByMemberIdAndAttendDateBetweenOrderByAttendDateAsc(Long memberId, String startDate, String endDate);
    List<Attend> findByMemberIdAndAttendDateStartsWith(Long memberId, String attendDate);
}
