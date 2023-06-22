package com.gsc.ninetosixapi.ninetosix.attend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "attendDate" }) })
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

    private String workTime;

    private LocalDateTime insertDate;

    private LocalDateTime updateDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Attend createAttend(String attendDate, String inTime, String attendCode, String locationCode, Member member){
        return Attend
                .builder()
                .attendDate(attendDate)
                .inTime(inTime)
                .attendCode(attendCode)
                .locationCode(locationCode)
                .member(member)
                .insertDate(LocalDateTime.now())
                .build();
    }

    public static Attend createAttendDayOff(String date, String code, Member member) {
        return Attend
                .builder()
                .attendDate(date)
                .attendCode(code)
                .member(member)
                .insertDate(LocalDateTime.now())
                .build();
    }
    public void updateCode(String code) {
        this.attendCode = code;
        this.updateDate = LocalDateTime.now();
    }

    public void updateInTimeAndLocationCode(String time, String locationCode){
        this.inTime = time;
        this.locationCode = locationCode;
        this.updateDate = LocalDateTime.now();
    }

    public void updateOutTimeAndWorkTime(String time){
        this.outTime = time;
        this.workTime = calculateWorkTime();
        this.updateDate = LocalDateTime.now();
    }

    private String calculateWorkTime() {
        LocalDateTime inDateTime = LocalDateTime.parse(this.getAttendDate() + this.getInTime(), DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        LocalDateTime outDateTime = LocalDateTime.parse(this.getAttendDate() + this.getOutTime(), DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        Duration duration = Duration.between(inDateTime, outDateTime);
        duration.minusMinutes(60);  // TODO: 특정 근무(ex.정상근무)에만 -60분(점심시간)으로 변경 필요
        return String.valueOf(duration.toHours()) + duration.toMinutesPart();
    }

}
