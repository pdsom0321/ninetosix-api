package com.gsc.ninetosixapi.ninetosix.user.entity;

import com.gsc.ninetosixapi.ninetosix.user.vo.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_USER_ROLE")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long id;

    private String role;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public static UserRole createUserRole(User user) {
        return UserRole.builder()
                .role(Role.ROLE_ADMIN.name())
                .user(user)
                .build();
    }
}
