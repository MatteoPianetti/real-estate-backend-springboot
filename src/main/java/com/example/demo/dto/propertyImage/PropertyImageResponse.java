package com.example.demo.dto.propertyImage;

import com.example.demo.domain.PropertyImage;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PropertyImageResponse {
    private Long id;
    private String url;
    private boolean cover;

    public static PropertyImageResponse from(PropertyImage p) {
        PropertyImageResponse res = new PropertyImageResponse();

        res.setId(p.getId());
        res.setUrl(p.getUrl());
        res.setCover(p.getCover());

        return res;
    }
}
