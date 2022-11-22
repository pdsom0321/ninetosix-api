package com.gsc.ninetosixapi.ninetosix.board.dto;

import com.gsc.ninetosixapi.ninetosix.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter @Builder
public class BoardsResDTO {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static BoardsResDTO getBoards(Board board) {
        return BoardsResDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .startDate(board.getStartDate())
                .endDate(board.getEndDate())
                .build();
    }
}
