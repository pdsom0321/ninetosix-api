package com.gsc.ninetosixapi.companyLocation.entity;

import com.gsc.ninetosixapi.company.entity.Company;
import com.gsc.ninetosixapi.location.entity.Location;
import com.gsc.ninetosixapi.attend.entity.Attend;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_COMPANY_LOCATION")
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyLocation", fetch = FetchType.EAGER)
    private List<Attend> attend = new ArrayList<>();

}
