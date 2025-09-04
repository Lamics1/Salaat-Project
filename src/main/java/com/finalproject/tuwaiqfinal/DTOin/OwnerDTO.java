package com.finalproject.tuwaiqfinal.DTOin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    @Size(min = 8, max = 128, message = "Password must be 8–128 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Account serial number cannot be empty")
    private String account_serial_num;
}
