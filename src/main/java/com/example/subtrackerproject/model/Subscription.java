package com.example.subtrackerproject.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private BillingPeriod billingPeriod;

    private LocalDate nextBillingDate;

    private String category;
    private boolean isActive = true;
}

