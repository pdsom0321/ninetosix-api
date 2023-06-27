package com.gsc.ninetosixapi.ninetosix.code.entity;

import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long id;

    @Column(unique = true)
    private String code;

    private String name;

    private String codeGroup;

    private String key1;

    private String key2;

    @Column(unique = true)
    private Integer codeOrder;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNCode useYn;
}
