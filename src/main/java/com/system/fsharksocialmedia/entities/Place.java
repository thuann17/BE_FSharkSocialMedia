package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "PLACES")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 500)
    @Nationalized
    @Column(name = "NAMEPLACE", length = 500)
    private String nameplace;

    @Size(max = 1000)
    @Column(name = "URLMAP", length = 1000)
    private String urlmap;

    @Size(max = 1000)
    @Nationalized
    @Column(name = "ADDRESS", length = 1000)
    private String address;

    @Size(max = 1000)
    @Nationalized
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @OneToMany(mappedBy = "placeid")
    private Set<Placeimage> placeimages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "placeid")
    private Set<Placetrip> placetrips = new LinkedHashSet<>();

}