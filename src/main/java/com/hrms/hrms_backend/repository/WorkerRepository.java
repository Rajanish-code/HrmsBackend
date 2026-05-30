package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.domain.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {}