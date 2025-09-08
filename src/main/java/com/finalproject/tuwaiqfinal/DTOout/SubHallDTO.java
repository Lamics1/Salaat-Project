package com.finalproject.tuwaiqfinal.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
    public class SubHallDTO {

        private Integer id;
        private String name;
        private Integer pricePerHour;
        private String description;

        private Set<GameDTO> games;
        private List<ReviewSubHallDTOout> reviewSubHalls;
    }
