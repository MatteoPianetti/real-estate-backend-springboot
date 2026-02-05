package com.example.demo.dto.visit;

import java.time.LocalDateTime;

import com.example.demo.domain.Visit.Visit;
import com.example.demo.domain.Visit.VisitStatus;

import java.time.LocalDate;
import lombok.*;

@Data
@NoArgsConstructor
public class VisitResponse {
    private Long id;
    private Long userId;
    private Long propertyId;
    private LocalDate visitDate;
    private VisitStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static VisitResponse from(Visit v) {
        VisitResponse res = new VisitResponse();

        res.setId(v.getId());
        res.setUserId(v.getUser().getId());
        res.setPropertyId(v.getProperty().getId());
        res.setVisitDate(v.getVisitDate());
        res.setStatus(v.getStatus());
        res.setCreatedAt(v.getCreatedAt());
        res.setUpdatedAt(v.getUpdatedAt());

        return res;
    }
}
