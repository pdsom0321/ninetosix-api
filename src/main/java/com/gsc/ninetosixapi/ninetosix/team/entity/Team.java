package com.gsc.ninetosixapi.ninetosix.team.entity;

import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team", fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();
}
