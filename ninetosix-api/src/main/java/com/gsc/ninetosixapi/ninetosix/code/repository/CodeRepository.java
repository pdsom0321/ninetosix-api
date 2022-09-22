package com.gsc.ninetosixapi.ninetosix.code.repository;

import com.gsc.ninetosixapi.ninetosix.code.entity.Code;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CodeRepository extends JpaRepository<Code, Long> {
    List<Code> findAllByCodeGroupAndUseYn(String codeGroup, YNCode ynCode);
}
