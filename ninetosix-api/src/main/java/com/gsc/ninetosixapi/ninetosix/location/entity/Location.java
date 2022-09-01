package com.gsc.ninetosixapi.ninetosix.location.entity;

import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private Float x;

    @Column(nullable = false)
    private Float y;

}
