package com.system.fsharksocialmedia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "TRIPS")
public class Trip {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 500)
    @Nationalized
    @Column(name = "TRIPNAME", length = 500)
    private String tripname;

    @Column(name = "STARTDATE")
    private LocalDate startdate;

    @Column(name = "ENDDATE")
    private LocalDate enddate;

    @Column(name = "CREATEDATE")
    private Instant createdate;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

}