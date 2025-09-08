package com.finalproject.tuwaiqfinal.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSubHallDTOout {

    private Integer id;
    private Double rating;
    private String comment;
    private LocalDateTime createdAt;

}
