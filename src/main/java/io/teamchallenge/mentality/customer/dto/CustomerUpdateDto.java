package io.teamchallenge.mentality.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerUpdateDto(
    @Email String email,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String phone,
    @NotBlank String address,
    @NotBlank String profilePicture) {}
