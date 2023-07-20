package com.zam.dev.food_order.model.midtrans;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MidtransPaymentRequest {

    @NotBlank
    @Pattern(regexp = "bank_transfer", message = "Only 'bank_transfer' value is allowed")
    private String payment_type;

    @NotNull
    private MidtransTransactionDetailsRequest transaction_details;
    @NotNull
    private MidtransCustomerDetailRequest customer_details;
    private List<MidtransItemDetailsRequest> item_details;
    @NotNull
    private MidtransBankTransferRequest bank_transfer;


    @Override
    public String toString() {
        return "MidtransPaymentRequest{" +
                "payment_type='" + payment_type + '\'' +
                ", transaction_details=" + transaction_details +
                ", customer_details=" + customer_details +
                ", item_details=" + item_details +
                ", bank_transfer=" + bank_transfer +
                '}';
    }
}
