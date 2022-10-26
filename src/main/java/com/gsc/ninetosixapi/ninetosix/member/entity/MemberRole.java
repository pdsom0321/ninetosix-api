package com.gsc.ninetosixapi.ninetosix.member.entity;

import com.gsc.ninetosixapi.ninetosix.member.vo.Role;
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
@Table
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_role_id")
    private Long id;

    private String role;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    public static MemberRole createUserRole(Member member) {
        return MemberRole.builder()
                .role(Role.ROLE_MEMBER.name())
                .member(member)
                .build();
    }
}
