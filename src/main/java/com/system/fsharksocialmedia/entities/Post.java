package com.system.fsharksocialmedia.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "POSTS")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "USERNAME")
    private User username;

    @Size(max = 200)
    @Nationalized
    @Column(name = "CONTENT", length = 200)
    private String content;

    @Column(name = "CREATEDATE")
    private Instant createdate;

    @Column(name = "STATUS")
    private Boolean status;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<Likepost> likeposts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<Notification> notifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "postid")
    private List<Postimage> postimages;

    @OneToMany(mappedBy = "post")
    private Set<Share> shares = new LinkedHashSet<>();

}