package com.gsc.ninetosixapi.ninetosix.company.entity;

import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table
public class Company {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    private String code;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", fetch = FetchType.LAZY)
    private List<CompanyLocation> companyLocations = new ArrayList<>();
}
