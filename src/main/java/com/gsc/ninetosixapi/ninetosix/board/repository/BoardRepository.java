package com.gsc.ninetosixapi.ninetosix.board.repository;

import com.gsc.ninetosixapi.ninetosix.board.entity.Board;
import com.gsc.ninetosixapi.ninetosix.vo.BoardType;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByTypeAndStartDateBeforeAndEndDateAfterAndUseYnAndDeleteYn(BoardType type, String startDate, String endDate, YNCode useYn, YNCode deleteYn, Pageable pageable);

    long countAllByType(BoardType type);

    Board findByIdAndType(long id, BoardType type);
}
