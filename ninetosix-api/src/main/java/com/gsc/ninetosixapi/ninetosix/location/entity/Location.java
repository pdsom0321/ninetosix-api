package com.gsc.ninetosixapi.ninetosix.location.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_LOCATION")
public class Location {

    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private Float x;

    @Column(nullable = false)
    private Float y;

}
