package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PLACEIMAGES")
public class Placeimage {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACEID")
    private Place placeid;

    @Lob
    @Column(name = "IMAGE")
    private String image;

}