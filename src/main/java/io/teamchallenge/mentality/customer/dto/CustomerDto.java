package io.teamchallenge.mentality.customer.dto;

import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;

public record CustomerDto(
    Integer id,
    @Email String email,
    String firstName,
    String lastName,
    String phone,
    String address,
    String profilePicture,
    LocalDateTime createdAt) {}
