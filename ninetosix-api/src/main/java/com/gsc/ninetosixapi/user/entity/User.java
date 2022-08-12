package com.gsc.ninetosixapi.user.entity;

import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.vo.Role;
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

//    @Column(nullable = false, length = 10)
//    private String employeeCode;

    @Column(length = 10)
    private String companyCode;

//    @Column(length = 10)
//    private String departmentCode;

    @Column(length = 1)
    private String deleteYn;

    @Column(length = 1)
    private String pushAgreeYn;

    @Column(length = 10)
    private int loginFailCnt;

    private LocalDateTime passwordModifyDate;

    private LocalDateTime insertDate;

    private LocalDateTime updateDate;

//    @Column(length = 20)
//    private String insId;

//    @Column(length = 20)
//    private String updId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> role = new HashSet<>();

    public void addRole(String role) {
        UserRole newRole = UserRole.builder()
                .role(role)
                .user(this)
                .build();

        this.getRole().add(newRole);
    }

    public static User createUser(UserInfoDTO userInfoDTO, PasswordEncoder passwordEncoder) {
        User newUser = new User();
        newUser.email = userInfoDTO.getEmail();
        newUser.name = userInfoDTO.getName();
        newUser.password = passwordEncoder.encode(userInfoDTO.getPassword());
        newUser.contact = userInfoDTO.getContact();
//        newUser.employeeCode = userInfoDTO.getEmployeeCode();
//        newUser.departmentCode = userInfoDTO.getDepartmentCode();
        newUser.companyCode = userInfoDTO.getCompanyCode();
        newUser.deleteYn = "N";
        newUser.pushAgreeYn = userInfoDTO.getPushAgreeYn();
        newUser.loginFailCnt = 0;
        newUser.insertDate = LocalDateTime.now();
        newUser.addRole(Role.ROLE_ADMIN.name());

        return newUser;
    }

}
