package com.gsc.ninetosixapi.ninetosix.board.service;

import com.gsc.ninetosixapi.ninetosix.board.dto.BoardResDTO;
import com.gsc.ninetosixapi.ninetosix.board.dto.BoardsResDTO;
import com.gsc.ninetosixapi.ninetosix.board.entity.Board;
import com.gsc.ninetosixapi.ninetosix.board.repository.BoardRepository;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardsResDTO> boards(String type) {
        LocalDateTime now = LocalDateTime.now();
        return boardRepository.findAllByTypeAndStartDateBeforeAndEndDateAfterAndUseYnAndDeleteYn(type, now, now, YNCode.Y, YNCode.N).stream()
                .map(BoardsResDTO::of)
                .collect(Collectors.toList());
    }

    public BoardResDTO board(String type, Long id) {
        Board board = boardRepository.findByIdAndType(id, type);
        return BoardResDTO.of(board);
    }
}
