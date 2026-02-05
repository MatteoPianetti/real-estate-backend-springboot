package com.example.demo.dto.favorite;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteRequest {
    private Long propertyId;
}
