package com.gsc.ninetosixapi.company_location.entity;

import com.gsc.ninetosixapi.company.entity.Company;
import com.gsc.ninetosixapi.location.entity.Location;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_COMPANY_LOCATION")
public class Company_Location {

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
