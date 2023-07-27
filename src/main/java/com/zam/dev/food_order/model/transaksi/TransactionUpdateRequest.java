package com.zam.dev.food_order.model.transaksi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUpdateRequest {

    @NotBlank
    @Pattern(regexp = "CANCELED|DELIVERED|DONE", message = "Invalid value. Allowed values are: CANCELED, DELIVERED , DONE")
    private String status_transaction;

}
