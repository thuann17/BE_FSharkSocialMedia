package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "IMAGES")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "IMAGE")
    private String image;

    @Column(name = "CREATEDATE")
    private Instant createdate;

    @Lob
    @Column(name = "AVATARRURL")
    private String avatarrurl;

    @Lob
    @Column(name = "COVERURL")
    private String coverurl;

    @Column(name = "STATUS")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME")
    private User username;

}