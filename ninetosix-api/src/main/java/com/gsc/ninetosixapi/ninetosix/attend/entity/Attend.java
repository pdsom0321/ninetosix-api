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
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private String hms = currentDateTime.format(DateTimeFormatter.ofPattern("HHmmss"));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attend_id")
    private Long id;

    @Column(nullable = false)
    private String attendDate;

    @Column(name = "inTime")
    private String goToWorkTime;

    @Column(name = "outTime")
    private String leaveWorkTime;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_location_id")
    private CompanyLocation companyLocation;

    public void setGoToWorkTime(){
        this.goToWorkTime = hms;
    }

    public void setLeaveWorkTime(){
        this.leaveWorkTime = hms;
    }

    public static Attend createAttend(String attendDate, String inTime, CompanyLocation companyLocation, User user, String status){
        return Attend
                .builder()
                .attendDate(attendDate)
                .goToWorkTime(inTime)
                .companyLocation(companyLocation)
                .user(user)
                .status(status)
                .build();
    }
}
