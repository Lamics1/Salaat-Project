package com.finalproject.tuwaiqfinal.DTOin;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CustomerDTO {


    private Integer user_id;

    @NotEmpty(message = "username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "password length should be at least 8")
    private String password;

    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+9665\\d{8}$", message = "Phone number must be a valid Saudi mobile in the format +9665XXXXXXXX")
    private String phoneNumber;

    @NotEmpty(message = "Location cannot be empty")
    private String location;

    @NotNull(message = "Age cannot be null")
    @Min(value = 12, message = "you must be above 11 years old")
    private Integer age;


}
