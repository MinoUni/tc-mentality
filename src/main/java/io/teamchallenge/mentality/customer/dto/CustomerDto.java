package io.teamchallenge.mentality.customer.dto;

import java.time.LocalDateTime;

public record CustomerDto(
    Integer id,
    String email,
    String firstName,
    String lastName,
    String phone,
    String address,
    String profilePicture,
    LocalDateTime createdAt) {}
