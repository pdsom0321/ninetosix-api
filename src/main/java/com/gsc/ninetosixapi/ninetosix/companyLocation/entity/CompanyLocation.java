package com.gsc.ninetosixapi.ninetosix.companyLocation.entity;

import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.location.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class CompanyLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_location_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

}
