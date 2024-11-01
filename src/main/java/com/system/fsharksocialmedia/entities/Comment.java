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
@Table(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 500)
    @Nationalized
    @Column(name = "CONTENT", length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME")
    private User username;

    @Column(name = "CREATEDATE")
    private Instant createdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST")
    private Post post;

    @Lob
    @Column(name = "IMAGE")
    private String image;

    @OneToMany(mappedBy = "comment")
    private Set<Likecmt> likecmts = new LinkedHashSet<>();

}