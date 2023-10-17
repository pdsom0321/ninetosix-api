package com.gsc.ninetosixapi.ninetosix.board.dto;

import com.gsc.ninetosixapi.ninetosix.board.entity.Board;

public record BoardSDTO(long id, String title, String startDate, String endDate) {
    public static BoardSDTO of(Board board) {
        return new BoardSDTO(board.getId(), board.getTitle(), board.getStartDate(), board.getEndDate());
    }
}

