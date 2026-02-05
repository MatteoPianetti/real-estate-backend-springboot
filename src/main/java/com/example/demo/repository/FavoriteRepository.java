package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.demo.domain.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>{
    List<Favorite> findAllByUserId(Long userId);
}
