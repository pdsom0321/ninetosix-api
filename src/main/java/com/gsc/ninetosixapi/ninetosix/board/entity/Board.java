package com.gsc.ninetosixapi.ninetosix.board.entity;

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

    private String type;

    private String title;

    @Lob
    private String content;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNCode useYn;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNCode deleteYn;
}
