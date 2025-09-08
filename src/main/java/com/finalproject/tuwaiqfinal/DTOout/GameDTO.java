package com.finalproject.tuwaiqfinal.DTOout;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    private Integer id;
    private String color;
    private Boolean isAvailable;
    private Integer numberOfPlayer;
}
