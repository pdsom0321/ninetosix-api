package com.gsc.ninetosixapi.ninetosix.user.entity;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.user.dto.signupReqDTO;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

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

    /** TODO : 로그인 실패횟수 체크 안할거면 삭제 필요 */
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Attend> attends = new ArrayList<>();

    @Transient
    private static Integer INIT_LOGIN_FAIL_CNT = 0;

    public static User createUser(signupReqDTO signupReqDTO, Company company1, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(signupReqDTO.getEmail())
                .name(signupReqDTO.getName())
                .password(passwordEncoder.encode(signupReqDTO.getPassword()))
                .contact(signupReqDTO.getContact())
                .company(company1)
                .deleteYn(YNCode.N)
                .pushAgreeYn(YNCode.valueOf(signupReqDTO.getPushAgreeYn()))
                .loginFailCnt(INIT_LOGIN_FAIL_CNT)
                .insertDate(LocalDateTime.now())
                .build();
    }

    public void updatePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);
    }

}
