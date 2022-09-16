package com.gsc.ninetosixapi.ninetosix.attend.entity;

import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Column(nullable = false)
    private String attendDate;

    private String inTime;

    private String outTime;

    @Column(nullable = false)
    private String attendCode;

    private String locationCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime insertDate;

    private LocalDateTime updateDate;

    public static Attend createAttend(String attendDate, String inTime, String locationCode, User user, String code){
        return Attend
                .builder()
                .attendDate(attendDate)
                .inTime(inTime)
                .locationCode(locationCode)
                .user(user)
                .attendCode(code)
                .insertDate(LocalDateTime.now())
                .build();
    }

    public static Attend addCode(String date, User user, String code) {
        return Attend
                .builder()
                .attendDate(date)
                .user(user)
                .attendCode(code)
                .insertDate(LocalDateTime.now())
                .build();
    }
    public void editCode(String date, User user, String code) {
        this.attendDate = date;
        this.user = user;
        this.attendCode = code;
        this.updateDate = LocalDateTime.now();
    }

    public void changeOutTime(String time){
        this.outTime = time;
        this.updateDate = LocalDateTime.now();
    }
}
