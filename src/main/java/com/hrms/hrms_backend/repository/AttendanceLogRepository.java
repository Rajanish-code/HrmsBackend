package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.domain.AttendanceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {

    @EntityGraph(attributePaths = {"worker", "site"})
    @Query("SELECT a FROM AttendanceLog a WHERE a.worker.id = :workerId AND a.clockInTimestamp BETWEEN :start AND :end")
    Page<AttendanceLog> findByWorkerAndDateRange(
            @Param("workerId") Long workerId, 
            @Param("start") LocalDateTime start, 
            @Param("end") LocalDateTime end, 
            Pageable pageable);

    Optional<AttendanceLog> findFirstByWorkerIdAndClockOutTimestampIsNullOrderByClockInTimestampDesc(Long workerId);
}