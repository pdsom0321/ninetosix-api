package com.gsc.ninetosixapi.ninetosix.board.entity;

import com.gsc.ninetosixapi.ninetosix.vo.BoardType;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardType type;

    private String title;

    @Lob
    private String content;

    private String startDate;

    private String endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNCode useYn;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNCode deleteYn;

    private String insertId;

    private LocalDateTime insertDate;

    private String updateId;

    private LocalDateTime updateDate;
}
