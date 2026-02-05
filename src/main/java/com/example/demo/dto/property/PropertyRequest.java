package com.example.demo.dto.property;

import java.math.BigDecimal;

import com.example.demo.domain.property.HouseStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private String address;
    private String city;
    private BigDecimal size;
    private Integer rooms;
    private Integer bathrooms;
    private HouseStatus status;
    private Long managerId;
}   
