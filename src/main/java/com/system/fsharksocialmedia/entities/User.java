package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @Size(max = 200)
    @Column(name = "PASSWORD", length = 200)
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

    @OneToMany(mappedBy = "username")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<Groupmember> groupmembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<Image> images = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<Likecmt> likecmts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<Likepost> likeposts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usersrc")
    private Set<Message> messages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<Notification> notifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<Share> shares = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userid")
    private Set<Usertrip> usertrips = new LinkedHashSet<>();


}