package com.example.subtrackerproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscription", indexes = {
    @Index(name = "idx_subscription_user", columnList = "app_user_id"),
    @Index(name = "idx_subscription_next_billing", columnList = "nextBillingDate"),
    @Index(name = "idx_subscription_active", columnList = "isActive")
})
@Getter
@Setter
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(scale = 2, precision = 10)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private BillingPeriod billingPeriod;

    private LocalDate nextBillingDate;

    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;
}

