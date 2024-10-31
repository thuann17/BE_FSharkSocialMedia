package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @Size(max = 200)
    @Column(name = "USERNAME", nullable = false, length = 200)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLES")
    private Userrole roles;

    @Size(max = 30)
    @Column(name = "PASSWORD", length = 30)
    private String password;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Size(max = 200)
    @Column(name = "EMAIL", length = 200)
    private String email;

    @Column(name = "GENDER")
    private Boolean gender;

    @Size(max = 100)
    @Nationalized
    @Column(name = "LASTNAME", length = 100)
    private String lastname;

    @Size(max = 100)
    @Nationalized
    @Column(name = "FIRSTNAME", length = 100)
    private String firstname;

    @Column(name = "BIRTHDAY")
    private LocalDate birthday;

    @Size(max = 500)
    @Nationalized
    @Column(name = "BIO", length = 500)
    private String bio;

    @Size(max = 200)
    @Nationalized
    @Column(name = "HOMETOWN", length = 200)
    private String hometown;

    @Size(max = 100)
    @Nationalized
    @Column(name = "CURRENCY", length = 100)
    private String currency;

}