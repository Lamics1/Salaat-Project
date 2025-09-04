package com.finalproject.tuwaiqfinal.DTOin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDTO {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Account serial number cannot be empty")
    private String account_serial_num;
}
