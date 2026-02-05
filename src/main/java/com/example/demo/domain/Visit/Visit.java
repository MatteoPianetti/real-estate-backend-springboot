package com.example.demo.domain.Visit;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.domain.property.Property;
import com.example.demo.domain.user.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
    name = "visite",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "property_id", "visit_date"})
)

public class Visit {
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
    private LocalDate visitDate;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void createdAt() {this.createdAt = LocalDateTime.now();}

    @PreUpdate
    public void updateAt() {this.updatedAt = LocalDateTime.now();}
}
