package com.gsc.ninetosixapi.ninetosix.attend.entity;

import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Column(nullable = false)
    private String attendDate;
    private String inTime;
    private String outTime;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_location_id")
    private CompanyLocation companyLocation;

    public static Attend createAttend(String attendDate, String inTime, String outTime, User user, CompanyLocation companyLocation, String status){
        return Attend
                .builder()
                    .attendDate(attendDate)
                    .inTime(inTime)
                    .outTime(outTime)
                    .user(user)
                    .companyLocation(companyLocation)
                    .status(status)
                .build();
    }
}
