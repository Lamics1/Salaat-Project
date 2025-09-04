package com.finalproject.tuwaiqfinal.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Owner {

    @Id
    private Integer id;

    @NotEmpty(message = "Account serial number cannot be empty")
    private String account_serial_num;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Hall> halls;
}
