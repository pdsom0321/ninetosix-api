package com.gsc.ninetosixapi.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "TB_USER")
public class User {

    public User(String email, String name, String pwd, String contact, String empNo, String deptCd, String compCd, String pushAgreeYn, String role) {
        this.email = email;
        this.name = name;
        this.pwd = pwd;
        this.contact = contact;
        this.empNo = empNo;
        this.deptCd = deptCd;
        this.compCd = compCd;
        this.pushAgreeYn = pushAgreeYn;
        this.addRole(role);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "PWD", nullable = false, length = 256)
    private String pwd;

    @Column(name = "CONTACT", length = 50)
    private String contact;

    @Column(name = "EMP_NO", nullable = false, length = 10)
    private String empNo;

    @Column(name = "COMP_CD", length = 10)
    private String compCd;

    @Column(name = "DEPT_CD", length = 10)
    private String deptCd;

    @Column(name = "DEL_YN", length = 1)
    private String delYn;

    @Column(name = "PUSH_AGREE_YN", length = 1)
    private String pushAgreeYn;

    @Column(name = "PWD_MOD_DT")
    private LocalDateTime pwdModDt;

    @Column(name = "LOGIN_FAIL_CNT", length = 10)
    private int loginFailCnt;

    @Column(name = "INS_DT")
    private LocalDateTime insDt;

    @Column(name = "INS_ID", length = 20)
    private String insId;

    @Column(name = "UPD_DT")
    private LocalDateTime updDt;

    @Column(name = "UPD_ID", length = 20)
    private String updId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> role = new HashSet<>();

    public void addRole(String role) {
        UserRole newRole = UserRole.builder()
                .role(role)
                .user(this)
                .build();

        this.getRole().add(newRole);
    }
}
