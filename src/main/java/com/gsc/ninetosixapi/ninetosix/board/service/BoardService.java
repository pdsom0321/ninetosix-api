package com.gsc.ninetosixapi.ninetosix.board.service;

import com.gsc.ninetosixapi.ninetosix.board.dto.BoardResDTO;
import com.gsc.ninetosixapi.ninetosix.board.dto.BoardsResDTO;
import com.gsc.ninetosixapi.ninetosix.board.entity.Board;
import com.gsc.ninetosixapi.ninetosix.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResDTO board(String type, Long id) {
        Board board = boardRepository.findByIdAndType(id, type);
        return BoardResDTO.of(board);
    }

    public List<BoardsResDTO> boards(String type) {
        return boardRepository.findAllByType(type).stream()
                .map(BoardsResDTO::of)
                .collect(Collectors.toList());
    }
}
