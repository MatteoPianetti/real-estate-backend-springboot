package com.example.demo.dto.property;

import java.math.BigDecimal;

import com.example.demo.domain.property.HouseStatus;
import com.example.demo.domain.property.Property;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PropertyResponse {
    private Long id;
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

    public static PropertyResponse from(Property p) {
        PropertyResponse res = new PropertyResponse();

        res.setId(p.getId());
        res.setTitle(p.getTitle());
        res.setDescription(p.getDescription());
        res.setPrice(p.getPrice());
        res.setAddress(p.getAddress());
        res.setCity(p.getCity());
        res.setSize(p.getSize());
        res.setRooms(p.getRooms());
        res.setBathrooms(p.getBathrooms());
        res.setStatus(p.getStatus());
        res.setManagerId(p.getManager().getId());

        return res;
    }
}
