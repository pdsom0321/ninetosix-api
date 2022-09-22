package com.gsc.ninetosixapi.ninetosix.attend.repository;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AttendRepository extends JpaRepository<Attend, Long>, JpaSpecificationExecutor<Attend> {
    Attend findByUser(User user);
    Optional<Attend> findByUserAndAttendDate(User user, String attendDate);
}
