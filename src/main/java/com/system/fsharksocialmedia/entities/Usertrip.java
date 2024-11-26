package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USERTRIPS")
public class Usertrip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRIPID")
    private Trip tripid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERID")
    private User userid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE")
    private Triprole role;

    @Size(max = 50)
    @Column(name = "STATUS", length = 50)
    private String status;

}