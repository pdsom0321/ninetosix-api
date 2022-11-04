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

    @Transient
    private String workTime;

    private LocalDateTime insertDate;

    private LocalDateTime updateDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Attend createAttend(String attendDate, String locationCode, Member member, String code){
        return Attend
                .builder()
                .attendDate(attendDate)
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

    public void updateInTime(String time){
        this.inTime = time;
        this.updateDate = LocalDateTime.now();
    }

    public void updateOutTime(String time){
        this.outTime = time;
        this.updateDate = LocalDateTime.now();
    }

    public void updateWorkTime(long hour, long min){
        this.workTime = ((hour < 10) ? "0" : "") + hour + "시간 ";
        this.workTime += ((min < 10) ? "0" : "") +  min + "분";
    }
}
