package com.hrms.hrms_backend.domain;

import com.hrms.hrms_backend.domain.enums.Designation;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "workers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 15, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Designation designation;

    @Column(name = "daily_wage_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyWageRate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}