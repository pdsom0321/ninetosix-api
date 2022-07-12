package com.gsc.ninetosixapi.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INS_DT")
    private Date insDt;

    @Column(name = "INS_ID", length = 20)
    private String insId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPD_DT")
    private Date updDt;

    @Column(name = "UPD_ID", length = 20)
    private String updId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PWD_MOD_DT")
    private Date pwdModDt;

    @Column(name = "LOGIN_FAIL_CNT", length = 10)
    private int loginFailCnt;

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
