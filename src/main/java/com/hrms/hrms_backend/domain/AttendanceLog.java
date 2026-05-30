package com.hrms.hrms_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "attendance_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AttendanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @Column(name="clock_in_timestamp",nullable=false)
    private LocalDateTime clockInTimestamp;

    @Column(name="clock_out_timestamp")
    private  LocalDateTime clockOutTimestamp;

    @Column(name="total_hours",precision = 5, scale = 2)
    private BigDecimal totalHours;

    @Column(name = "overtime_hours", precision = 5 , scale = 2 )
    private BigDecimal overtimeHours;

    @Column(nullable = false)
    private boolean flagged = false;
}