package com.gsc.ninetosixapi.attend.entity;

import com.gsc.ninetosixapi.company_location.entity.Company_Location;
import com.gsc.ninetosixapi.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ATTEND")
public class Attend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attend_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime attendDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime checkDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalDateTime inTime;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalDateTime outTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_location_id")
    private Company_Location companyLocation;
}
