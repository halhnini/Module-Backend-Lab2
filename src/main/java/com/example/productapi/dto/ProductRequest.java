package com.example.productapi.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String name,

        @NotNull(message = "Le prix est obligatoire")
        @Positive(message = "Le prix doit être positif")
        BigDecimal price,

        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 0, message = "La quantité ne peut pas être négative")
        Integer quantity,

        String description
) {
}
