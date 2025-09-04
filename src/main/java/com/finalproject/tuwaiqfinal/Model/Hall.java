package com.finalproject.tuwaiqfinal.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private Owner owner;

    @NotEmpty(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "Description cannot be empty")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Status cannot be null")
    @Column(nullable = false)
    private Boolean status;

    @NotEmpty(message = "Location cannot be empty")
    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
    private Set<SubHall> subHalls;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
    private Set<ReviewHall> reviewHalls;
}