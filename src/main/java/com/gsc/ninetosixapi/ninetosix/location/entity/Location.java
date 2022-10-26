package com.gsc.ninetosixapi.ninetosix.location.entity;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Location {

    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Float latitude;

    @Column(nullable = false)
    private Float longitude;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", fetch = FetchType.LAZY)
    private List<CompanyLocation> companyLocations = new ArrayList<>();
}
