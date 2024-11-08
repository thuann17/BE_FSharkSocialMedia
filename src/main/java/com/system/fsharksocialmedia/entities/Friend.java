package com.system.fsharksocialmedia.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "FRIENDS")
public class Friend {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_TARGET")
    @JsonIgnore
    private User userTarget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SRC")
    @JsonIgnore
    private User userSrc;

    @Column(name = "CREATEDATE")
    private Instant createdate;

    @Column(name = "STATUS")
    private Boolean status;

}