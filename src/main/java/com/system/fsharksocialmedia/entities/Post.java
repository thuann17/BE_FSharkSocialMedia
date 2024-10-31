package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "POSTS")
public class Post {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
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

}