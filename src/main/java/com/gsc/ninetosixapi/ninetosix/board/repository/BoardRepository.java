package com.gsc.ninetosixapi.ninetosix.board.repository;

import com.gsc.ninetosixapi.ninetosix.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByType(String type);
    Board findByIdAndType(Long id, String type);
}
