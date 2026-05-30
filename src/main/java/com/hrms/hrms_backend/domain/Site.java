package com.hrms.hrms_backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Site{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="site_name",nullable=false,length=150)
    private String siteName;

    @Column(nullable=false,length=255)
    private String location;

    @Column(name="is_active",nullable=false)
    private boolean isActive=true;
}