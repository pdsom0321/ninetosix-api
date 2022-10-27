package com.gsc.ninetosixapi.ninetosix.board.dto;

import com.gsc.ninetosixapi.ninetosix.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter @Builder
public class BoardResDTO {
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static BoardResDTO of(Board board) {
        return BoardResDTO.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .startDate(board.getStartDate())
                .endDate(board.getEndDate())
                .build();
    }
}
