package com.example.demo.dto.favorite;

import java.time.LocalDateTime;

import com.example.demo.domain.Favorite;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FavoriteResponse {
    private Long id;
    private Long userId;
    private Long propertyId;
    private LocalDateTime createdAt;

    public static FavoriteResponse from(Favorite f) {
        FavoriteResponse res = new FavoriteResponse();

        res.setId(f.getId());
        res.setUserId(f.getUser().getId());
        res.setPropertyId(f.getProperty().getId());
        res.setCreatedAt(f.getCreatedAt());

        return res;
    }
}
