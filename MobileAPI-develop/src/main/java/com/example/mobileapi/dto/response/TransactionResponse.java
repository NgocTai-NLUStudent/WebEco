package com.example.mobileapi.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponse {
    UUID id;
    OrderResponseDTO orders;
    String responseCode;
    String transactionDate;
    String createDate;
    String amount;
}
