package com.gsc.ninetosixapi.ninetosix.board.service;

import com.gsc.ninetosixapi.ninetosix.board.dto.BoardResDTO;
import com.gsc.ninetosixapi.ninetosix.board.dto.BoardSDTO;
import com.gsc.ninetosixapi.ninetosix.board.dto.BoardsResDTO;
import com.gsc.ninetosixapi.ninetosix.board.entity.Board;
import com.gsc.ninetosixapi.ninetosix.board.repository.BoardRepository;
import com.gsc.ninetosixapi.ninetosix.vo.BoardType;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardsResDTO boards(String type, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<BoardSDTO> boards = boardRepository.findAllByTypeAndStartDateBeforeAndEndDateAfterAndUseYnAndDeleteYn(BoardType.valueOf(type), nowDate, nowDate, YNCode.Y, YNCode.N, pageable).stream()
                .map(BoardSDTO::of)
                .toList();

        long count = boardRepository.countAllByType(BoardType.valueOf(type));
        int pageSize = pageable.getPageSize();
        int totalPage = (int) Math.ceil( (double) count / pageSize);

        return new BoardsResDTO(pageable.getPageNumber(), totalPage, boards);
    }

    public BoardResDTO board(String type, long id) {
        Board board = boardRepository.findByIdAndType(id, BoardType.valueOf(type));
        return BoardResDTO.of(board);
    }
}
