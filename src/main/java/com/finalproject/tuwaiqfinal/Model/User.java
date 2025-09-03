package com.finalproject.tuwaiqfinal.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Username cannot be empty")
    @Column(nullable = false)
    private String user_name;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^(CUSTOMER|OWNER|ADMIN)$", message = "Role must be CUSTOMER, OWNER, or ADMIN")
    @Column(nullable = false)
    private String role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Customer customer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Owner owner;
}
