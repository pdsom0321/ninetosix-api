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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    public ResponseEntity<BoardResDTO> board(@RequestBody BoardReqDTO reqDTO) {
        return ResponseEntity.ok(boardService.board(reqDTO));
    }

    @GetMapping("/boards")
    public ResponseEntity<List<BoardsResDTO>> boards(@RequestBody BoardsReqDTO reqDTO) {
        return ResponseEntity.ok(boardService.boards(reqDTO.type()));
    }
}
