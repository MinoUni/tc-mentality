package io.teamchallenge.mentality.customer.dto;

public record CustomerPatchDto(
    String firstName,
    String lastName,
    String phone,
    String address,
    String profilePicture) {}
