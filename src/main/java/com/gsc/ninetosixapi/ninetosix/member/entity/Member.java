package com.gsc.ninetosixapi.ninetosix.member.entity;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.member.dto.SignupReqDTO;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
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
    /**
     * TODO : 로그인 실패횟수 체크 안할거면 삭제 필요
     */
    @Column(length = 1)
    private Integer loginFailCnt;
    private LocalDateTime passwordModifiedDate;
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member", fetch = FetchType.EAGER)
    private Set<MemberRole> role = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member", fetch = FetchType.LAZY)
    private List<Attend> attends = new ArrayList<>();
    @Transient
    private static Integer INIT_LOGIN_FAIL_CNT = 0;

    public static Member create(SignupReqDTO signupReqDTO, String password, Company company1) {
        return Member.builder()
                .email(signupReqDTO.email())
                .name(signupReqDTO.name())
                .password(password)
                .contact(signupReqDTO.contact())
                .company(company1)
                .deleteYn(YNCode.N)
                .pushAgreeYn(YNCode.valueOf(signupReqDTO.pushAgreeYn()))
                .loginFailCnt(INIT_LOGIN_FAIL_CNT)
                .insertDate(LocalDateTime.now())
                .build();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.passwordModifiedDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

}
