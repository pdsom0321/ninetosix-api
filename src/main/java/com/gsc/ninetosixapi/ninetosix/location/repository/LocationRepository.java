package com.gsc.ninetosixapi.ninetosix.location.repository;

import com.gsc.ninetosixapi.ninetosix.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, Long> {
}
