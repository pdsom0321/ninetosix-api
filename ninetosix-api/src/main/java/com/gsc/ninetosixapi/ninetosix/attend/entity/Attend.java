package com.gsc.ninetosixapi.ninetosix.attend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Attend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attend_id")
    private Long id;

    private String attendDate;

    private String inTime;

    private String outTime;

    private String attendCode;

    private String locationCode;

    private LocalDateTime insertDate;

    private LocalDateTime updateDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Attend createAttend(String attendDate, String inTime, String locationCode, Member member, String code){
        return Attend
                .builder()
                .attendDate(attendDate)
                .inTime(inTime)
                .locationCode(locationCode)
                .member(member)
                .attendCode(code)
                .insertDate(LocalDateTime.now())
                .build();
    }

    public static Attend createAttendByCode(String date, Member member, String code) {
        return Attend
                .builder()
                .attendDate(date)
                .member(member)
                .attendCode(code)
                .insertDate(LocalDateTime.now())
                .build();
    }
    public void updateCode(Member member, String code) {
        this.member = member;
        this.attendCode = code;
        this.updateDate = LocalDateTime.now();
    }

    public void updateOutTime(String time){
        this.outTime = time;
        this.updateDate = LocalDateTime.now();
    }
}
