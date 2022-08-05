package com.gsc.ninetosixapi.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;



@Entity
@Getter
@NoArgsConstructor
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

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 256)
    private String pwd;

    @Column(length = 50)
    private String contact;

    @Column(nullable = false, length = 10)
    private String empNo;

    @Column(length = 10)
    private String compCd;

    @Column(length = 10)
    private String deptCd;

    @Column(length = 1)
    private String delYn;

    @Column(length = 1)
    private String pushAgreeYn;

    @Column()
    private LocalDateTime pwdModDt;

    @Column(length = 10)
    private int loginFailCnt;

    @Column
    private LocalDateTime insDt;

    @Column(length = 20)
    private String insId;

    @Column
    private LocalDateTime updDt;

    @Column(length = 20)
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

    @PrePersist
    public void prePersist() {
        this.insDt = LocalDateTime.now();
        this.delYn = "N";
        this.loginFailCnt = 0;
    }

}
