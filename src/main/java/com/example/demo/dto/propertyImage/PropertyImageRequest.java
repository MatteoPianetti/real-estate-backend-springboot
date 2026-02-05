package com.example.demo.dto.propertyImage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyImageRequest {
    private String url;
    private boolean cover;
}
