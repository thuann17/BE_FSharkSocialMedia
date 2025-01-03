package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "CONVERSATIONS")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 300)
    @Nationalized
    @Column(name = "NAME", length = 300)
    private String name;

    @Column(name = "CREATEDAT")
    private Instant createdat;

    @Lob
    @Column(name = "AVATAR")
    private String avatar;

    @OneToMany(mappedBy = "conversation")
    private Set<Groupmember> groupmembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages = new LinkedHashSet<>();

}