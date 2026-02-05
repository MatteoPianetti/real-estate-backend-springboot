package com.example.demo.dto.visit;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VisitRequest {
    private Long propertyId;
    private LocalDate date;
}
