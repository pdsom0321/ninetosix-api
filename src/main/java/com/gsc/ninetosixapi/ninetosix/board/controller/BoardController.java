package com.gsc.ninetosixapi.ninetosix.board.controller;

import com.gsc.ninetosixapi.ninetosix.board.dto.BoardReqDTO;
import com.gsc.ninetosixapi.ninetosix.board.dto.BoardResDTO;
import com.gsc.ninetosixapi.ninetosix.board.dto.BoardsReqDTO;
import com.gsc.ninetosixapi.ninetosix.board.dto.BoardsResDTO;
import com.gsc.ninetosixapi.ninetosix.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<List<BoardsResDTO>> getBoards(BoardsReqDTO reqDTO) {
        return ResponseEntity.ok(boardService.getBoards(reqDTO.getType()));
    }

    @GetMapping("/board")
    public ResponseEntity<BoardResDTO> getBoard(BoardReqDTO reqDTO) {
        return ResponseEntity.ok(boardService.getBoard(reqDTO));
    }
}
