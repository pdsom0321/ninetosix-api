package com.gsc.ninetosixapi.ninetosix.board.repository;

import com.gsc.ninetosixapi.ninetosix.board.entity.Board;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByTypeAndStartDateBeforeAndEndDateAfterAndUseYnAndDeleteYn(String type, LocalDateTime startDate, LocalDateTime endDate, YNCode useYn, YNCode deleteYn);

    Board findByIdAndType(Long id, String type);
}
