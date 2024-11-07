package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "IMAGES")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "IMAGE", columnDefinition = "TEXT")
    private String image;

    @Column(name = "CREATEDATE")
    private LocalDateTime createDate;

    @Column(name = "AVATARRURL", columnDefinition = "TEXT")
    private String avatarUrl;

    @Column(name = "COVERURL", columnDefinition = "TEXT")
    private String coverUrl;

    @Column(name = "STATUS")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME") // Liên kết với cột USERNAME trong bảng IMAGES
    private User user; // Liên kết với thực thể User
}
