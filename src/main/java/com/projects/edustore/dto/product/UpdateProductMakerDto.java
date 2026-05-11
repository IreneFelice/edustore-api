package com.projects.edustore.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateProductMakerDto {
    @NotNull(message = "id of new maker is required")
    @Positive
    private Long newMakerId;

    public Long getNewMakerId() {
        return newMakerId;
    }

    public void setNewMakerId(Long newMakerId) {
        this.newMakerId = newMakerId;
    }
}
