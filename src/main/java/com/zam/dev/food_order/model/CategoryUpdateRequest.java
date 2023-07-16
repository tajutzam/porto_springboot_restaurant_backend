package com.zam.dev.food_order.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CategoryUpdateRequest {

    @NotBlank
    private String id;

    @NotBlank
    @Size(
            min = 4 , max = 100
    )
    private String name;
}
