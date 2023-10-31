package com.gsc.ninetosixapi.ninetosix.signature.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "signature_id")
    private Long id;

    @Lob
    private String signature;

    private Long memberId;

    private LocalDateTime insertDate;

    public static Signature create(String signature, Long memberId) {
        return Signature.builder()
                .signature(signature)
                .memberId(memberId)
                .build();
    }
}
