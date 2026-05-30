-- ===================================================================
-- 1. CLEAN EXISTING DATA (Optional - Safely resets tables if needed)
-- ===================================================================
TRUNCATE TABLE attendance_logs, workers, sites RESTART IDENTITY CASCADE;

-- ===================================================================
-- 2. INSERT 10 RECORDS INTO 'SITES' TABLE
-- ===================================================================
INSERT INTO sites (site_name, location, is_active) VALUES
('Greenfield Phase 2', 'Noida Sector 62', true),
('Skyline Towers', 'Delhi Connaught Place', true),
('Metro Yard Project', 'Patna Danapur', true),
('Smart City Hub', 'Lucknow Hazratganj', true),
('Oceanic Enclave', 'Greater Noida Alpha 1', true),
('Riverfront Walkway', 'Patna Marine Drive', true),
('Highway Bypass Flyover', 'Varanasi Cantt', true),
('Tech Park Block C', 'Noida Sector 135', true),
('Industrial Warehouse', 'Kanpur Panki', false), -- Testing validation for inactive sites
('Premium Residency', 'Delhi Dwarka Sector 21', true);

-- ===================================================================
-- 3. INSERT 10 RECORDS INTO 'WORKERS' TABLE
-- ===================================================================
INSERT INTO workers (name, phone, designation, daily_wage_rate, is_active) VALUES
('Shibu Kumar', '9876543210', 'SUPERVISOR', 950.00, true),
('Amit Sharma', '9876543211', 'MASON', 600.00, true),
('Rahul Verma', '9876543212', 'ELECTRICIAN', 750.00, true),
('Vikram Singh', '9876543213', 'PLUMBER', 700.00, true),
('Rohan Yadav', '9876543214', 'HELPER', 450.00, true),
('Aman Gupta', '9876543215', 'MASON', 620.00, true),
('Deepak Raj', '9876543216', 'ELECTRICIAN', 720.00, true),
('Sanjay Misra', '9876543217', 'SUPERVISOR', 900.00, true),
('Manoj Tiwari', '9876543218', 'HELPER', 460.00, false), 
('Vijay Thapa', '9876543219', 'PLUMBER', 680.00, true);

-- ===================================================================
-- 4. INSERT 10 RECORDS INTO 'ATTENDANCE_LOGS' TABLE
--    (Includes varying shift hours, overtime calculations, and safety flags)
-- ===================================================================
INSERT INTO attendance_logs (worker_id, site_id, clock_in_timestamp, clock_out_timestamp, total_hours, overtime_hours, flagged) VALUES
(1, 1, '2026-05-25 08:00:00', '2026-05-25 16:00:00', 8.00, 0.00, false), 
(2, 1, '2026-05-25 08:00:00', '2026-05-25 18:30:00', 10.50, 2.50, false),
(3, 2, '2026-05-25 09:00:00', '2026-05-25 17:00:00', 8.00, 0.00, false),  
(4, 2, '2026-05-25 09:00:00', '2026-05-25 21:00:00', 12.00, 4.00, false), 
(5, 3, '2026-05-26 07:30:00', '2026-05-26 15:30:00', 8.00, 0.00, false),  
(6, 4, '2026-05-26 08:00:00', '2026-05-26 19:00:00', 11.00, 3.00, false), 
(7, 5, '2026-05-27 08:00:00', '2026-05-27 16:00:00', 8.00, 0.00, false),  
(8, 8, '2026-05-27 08:00:00', '2026-05-28 02:00:00', 18.00, 10.00, true),  
(1, 6, '2026-05-28 08:00:00', '2026-05-28 16:00:00', 8.00, 0.00, false),  
(3, 7, '2026-05-28 08:30:00', '2026-05-28 19:30:00', 11.00, 3.00, false); 
