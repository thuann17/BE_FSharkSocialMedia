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
@Table(name = "USERROLES")
public class Userrole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ROLE", length = 100)
    private String role;

    @OneToMany(mappedBy = "roles")
    private Set<User> users = new LinkedHashSet<>();

}