package com.finalproject.tuwaiqfinal.DTOout;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSubHallDTO {

private Integer id;
    private String name;
    private Integer pricePerHour;
    private String description;
}
