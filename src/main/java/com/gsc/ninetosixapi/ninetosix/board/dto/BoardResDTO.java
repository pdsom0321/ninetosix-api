package com.gsc.ninetosixapi.ninetosix.board.dto;

import com.gsc.ninetosixapi.ninetosix.board.entity.Board;
import lombok.Builder;

@Builder
public record BoardResDTO(String title, String content, String startDate, String endDate) {
    public static BoardResDTO of(Board board) {
        return BoardResDTO.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .startDate(board.getStartDate())
                .endDate(board.getEndDate())
                .build();
    }
}
