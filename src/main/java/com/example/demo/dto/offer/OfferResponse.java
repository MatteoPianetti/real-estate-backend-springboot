package com.example.demo.dto.offer;

import java.time.LocalDateTime;

import com.example.demo.domain.offer.Offer;
import com.example.demo.domain.offer.OfferStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OfferResponse {
    private Long id;
    private Long userId;
    private Long propertyId;
    private OfferStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OfferResponse from(Offer o) {
        OfferResponse res = new OfferResponse();
        
        res.setId(o.getId());
        res.setUserId(o.getUser().getId());
        res.setPropertyId(o.getProperty().getId());
        res.setStatus(o.getStatus());
        res.setCreatedAt(o.getCreatedAt());
        res.setUpdatedAt(o.getUpdatedAt());

        return res;
    }
}
