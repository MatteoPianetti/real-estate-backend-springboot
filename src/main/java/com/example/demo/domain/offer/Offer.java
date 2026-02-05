package com.example.demo.domain.offer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.domain.property.Property;
import com.example.demo.domain.user.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
    name = "offerta",
    //non posso avere due righe con lo stesso user e la stessa proprietà
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "property_id", "amount"})
)

public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @NotNull
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private OfferStatus status;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    @PrePersist
    public void createdAt() {this.createdAt = LocalDateTime.now();}

    @PreUpdate
    public void updatedAt() {this.updatedAt = LocalDateTime.now();}
}
