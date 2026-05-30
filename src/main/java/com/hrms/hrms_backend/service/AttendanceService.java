package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.domain.AttendanceLog;
import com.hrms.hrms_backend.domain.Site;
import com.hrms.hrms_backend.domain.Worker;
import com.hrms.hrms_backend.repository.AttendanceLogRepository;
import com.hrms.hrms_backend.repository.SiteRepository;
import com.hrms.hrms_backend.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceLogRepository attendanceRepository;
    private final WorkerRepository workerRepository;
    private final SiteRepository siteRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "activeWorkers:";

    @Transactional
    public Map<String, Object> clockIn(Long workerId, Long siteId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found."));
        if (!worker.isActive()) {
            throw new IllegalStateException("Worker record is inactive.");
        }

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("Site not found."));
        if (!site.isActive()) {
            throw new IllegalStateException("Site location is inactive.");
        }

        attendanceRepository.findFirstByWorkerIdAndClockOutTimestampIsNullOrderByClockInTimestampDesc(workerId)
                .ifPresent(log -> {
                    throw new IllegalStateException("Worker is already clocked in at a site.");
                });

        AttendanceLog attendanceLog = AttendanceLog.builder()
                .worker(worker)
                .site(site)
                .clockInTimestamp(LocalDateTime.now())
                .build();

        attendanceRepository.save(attendanceLog);

        Map<String, Object> cacheData = Map.of(
                "workerId", worker.getId().toString(),
                "workerName", worker.getName(),
                "siteId", site.getId().toString(),
                "siteName", site.getSiteName(),
                "clockInTime", attendanceLog.getClockInTimestamp().toString()
        );

        redisTemplate.opsForHash().putAll(REDIS_KEY_PREFIX + workerId, cacheData);
        redisTemplate.expire(REDIS_KEY_PREFIX + workerId, 16, TimeUnit.HOURS);

        return cacheData;
    }

    @Transactional
    public AttendanceLog clockOut(Long workerId) {
        AttendanceLog attendanceLog = attendanceRepository.findFirstByWorkerIdAndClockOutTimestampIsNullOrderByClockInTimestampDesc(workerId)
                .orElseThrow(() -> new IllegalStateException("No open session tracking found for worker."));

        LocalDateTime outTime = LocalDateTime.now();
        attendanceLog.setClockOutTimestamp(outTime);

        long minutes = Duration.between(attendanceLog.getClockInTimestamp(), outTime).toMinutes();
        BigDecimal totalHours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        attendanceLog.setTotalHours(totalHours);

        BigDecimal overtime = BigDecimal.ZERO;
        if (totalHours.compareTo(BigDecimal.valueOf(8)) > 0) {
            overtime = totalHours.subtract(BigDecimal.valueOf(8));
        }
        attendanceLog.setOvertimeHours(overtime);

        if (totalHours.compareTo(BigDecimal.valueOf(16)) > 0) {
            attendanceLog.setFlagged(true);
        }

        attendanceRepository.save(attendanceLog);
        redisTemplate.delete(REDIS_KEY_PREFIX + workerId);

        return attendanceLog;
    }

    
    public Page<AttendanceLog> getAttendanceHistory(Long workerId, String fromDate, String toDate, int page, int size) {
        LocalDateTime start = LocalDate.parse(fromDate).atStartOfDay();
        LocalDateTime end = LocalDate.parse(toDate).atTime(23, 59, 59);
        Pageable pageable = PageRequest.of(page, size);
        
        return attendanceRepository.findByWorkerAndDateRange(workerId, start, end, pageable);
    }
}