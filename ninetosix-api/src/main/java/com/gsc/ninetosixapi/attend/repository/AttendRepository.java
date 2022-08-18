package com.gsc.ninetosixapi.attend.repository;

import com.gsc.ninetosixapi.attend.entity.Attend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendRepository extends JpaRepository<Attend, Long> {
    Attend findByCode(Long attendId);
}
