package com.servicehub.crm.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public record CategorySaveRequest(
        Long id,
        @NotBlank String name,
        @NotBlank String slug
) { }
