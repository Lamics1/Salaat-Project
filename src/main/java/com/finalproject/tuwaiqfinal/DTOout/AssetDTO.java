package com.finalproject.tuwaiqfinal.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssetDTO {
    private byte[] data;
    private String contentType;
}
