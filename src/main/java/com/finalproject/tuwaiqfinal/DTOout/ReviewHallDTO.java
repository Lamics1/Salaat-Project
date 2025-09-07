package com.finalproject.tuwaiqfinal.DTOout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewHallDTO {

    private Integer id;
    private Double rating;
    private String comment;
}
