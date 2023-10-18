package com.gsc.ninetosixapi.ninetosix.team.repository;

import com.gsc.ninetosixapi.ninetosix.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByCompanyId(long companyId);
}
