package com.example.demo.domain.property;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.domain.user.User;
import com.example.demo.domain.PropertyImage;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "proprietà")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private BigDecimal price;
    private String address;
    private String city;
    private BigDecimal size;
    private Integer rooms;
    private Integer bathrooms;

    @Enumerated(EnumType.STRING)
    private HouseStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyImage> images;

    @PrePersist
    public void createdAt() {this.createdAt = LocalDateTime.now();}

    @PreUpdate
    public void updateAt() {this.updatedAt = LocalDateTime.now();}
}
