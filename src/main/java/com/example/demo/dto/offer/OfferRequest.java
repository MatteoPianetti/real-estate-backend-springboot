package com.example.demo.dto.offer;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfferRequest {
    private Long propertyId;
    private BigDecimal amount;
}
