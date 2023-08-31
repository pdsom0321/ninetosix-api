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

    private Long locationId;

    private Long workTime;

    private LocalDateTime insertDate;

    private LocalDateTime updateDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Attend createAttend(String attendDate, String inTime, String attendCode, Long locationId, Member member){
        return Attend
                .builder()
                .attendDate(attendDate)
                .inTime(inTime)
                .attendCode(attendCode)
                .locationId(locationId)
                .member(member)
                .insertDate(LocalDateTime.now())
                .build();
    }

    public static Attend createAttendDayOff(String date, Member member) {
        return Attend
                .builder()
                .attendDate(date)
                .member(member)
                .insertDate(LocalDateTime.now())
                .build();
    }
    public void updateCode(String code) {
        this.attendCode = code;
        this.updateDate = LocalDateTime.now();
    }

    public void updateInTimeAndLocationId(String time, Long locationId){
        this.inTime = time;
        this.locationId = locationId;
        this.updateDate = LocalDateTime.now();
    }

    public void updateOutTimeAndWorkTime(String time){
        this.outTime = time;
        this.workTime = calculateWorkTime();
        this.updateDate = LocalDateTime.now();
    }

    private long calculateWorkTime() {
        LocalDateTime inDateTime = LocalDateTime.parse(this.getAttendDate() + this.getInTime(), DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        LocalDateTime outDateTime = LocalDateTime.parse(this.getAttendDate() + this.getOutTime(), DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        Duration duration = Duration.between(inDateTime, outDateTime);
        Duration minusLunchTime = duration;
        if(Integer.parseInt(this.getInTime()) <= 1130 && Integer.parseInt(this.getOutTime()) >= 1230)
            minusLunchTime = duration.minusMinutes(60);
        return minusLunchTime.toMinutes();
    }
}
