package com.gsc.ninetosixapi.ninetosix.board.dto;

import java.util.List;

public record BoardsResDTO(int page, int totalPage, List<BoardSDTO> list) {
}
