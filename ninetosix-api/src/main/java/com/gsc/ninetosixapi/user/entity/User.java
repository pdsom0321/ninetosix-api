package com.gsc.ninetosixapi.user.entity;

import com.gsc.ninetosixapi.attend.entity.Attend;
import com.gsc.ninetosixapi.company.entity.Company;
import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.vo.YNCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 256)
    private String password;

    @Column(length = 50)
    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNCode deleteYn;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNCode pushAgreeYn;

    @Column(length = 1)
    private Integer loginFailCnt;

    private LocalDateTime passwordModifiedDate;

    private LocalDateTime insertDate;

    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> role = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Attend> attend = new HashSet<>();

    @Transient
    private static Integer INIT_LOGIN_FAIL_CNT = 0;

    public static User createUser(UserInfoDTO userInfoDTO, PasswordEncoder passwordEncoder, Company company1) {
        return User.builder()
                .email(userInfoDTO.getEmail())
                .name(userInfoDTO.getName())
                .password(passwordEncoder.encode(userInfoDTO.getPassword()))
                .contact(userInfoDTO.getContact())
                .company(company1)
                .deleteYn(YNCode.N)
                .pushAgreeYn(YNCode.valueOf(userInfoDTO.getPushAgreeYn()))
                .loginFailCnt(INIT_LOGIN_FAIL_CNT)
                .insertDate(LocalDateTime.now())
                .build();
    }

}
