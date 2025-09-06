package com.finalproject.tuwaiqfinal.DTOin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameAvailabilityDTO {

    private Integer gameId;
    private String color;
    private Integer maxPlayers;
    private boolean available;

}
