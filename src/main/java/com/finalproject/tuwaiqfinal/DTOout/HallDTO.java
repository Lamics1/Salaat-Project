package com.finalproject.tuwaiqfinal.DTOout;

import com.finalproject.tuwaiqfinal.Model.ReviewHall;
import com.finalproject.tuwaiqfinal.Model.ReviewSubHall;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HallDTO {

    private Integer id;
    private String name;
    private String description;
    private Boolean isAvailable;
    private String location;

    private Set<SubHallDTO> subHalls;
    private Set<ReviewHall> reviewHalls;

}
