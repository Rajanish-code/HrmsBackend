# Workforce Attendance Management Backend (HRMS)
This is a high-performance, enterprise-grade Spring Boot backend service designed to manage workforce 
clock-in/out workflows, track shift hours, automatically calculate real-time overtime, and prevent concurrent 
shift fraud.

The architecture is explicitly tuned to handle remote database connection pooling efficiently and leverages distributed caching for real-time tracking.

---
#### Draw the Backend
here is link to draw https://app.diagrams.net/

<img width="762" height="491" alt="Untitled Diagram" src="https://github.com/user-attachments/assets/7804a2f8-6d69-4439-a5ce-9d946a1beccb" />

## System Architecture Diagram
Below is the complete architectural layout demonstrating how data flows securely between the client requests, application logic, Redis caching infrastructure, and the relational database engine:

---
## Step-by-Step Setup & Installation

### 1. Prerequisites Your System Meets
* **Java:** Version 17 or higher (Successfully tested on OpenJDK 25.0.2)
* **Build Tool:** Maven 3.x
* **Database:** PostgreSQL 16+ (Local or Cloud-hosted via Supabase)
* **In-Memory Store:** Redis Server (Active local instance listening on port 6379)

  ---

### 2. Database Connection Setup ('application.properties')
  Navigate to your 'src/main/resources/application.properties' file and update your database and memory engine bindings:
 
<img width="1920" height="1080" alt="Screenshot from 2026-05-30 12-10-57" src="https://github.com/user-attachments/assets/af2ff47b-db11-4f8a-973c-d4c2238291a0" />

### 3. Initialize Seed Table Structure and Records (SQL Script)
Before triggering state transitions, your relational tables need foundational master profiles. Open your database terminal/SQL console and run the following bootstrap data:

[Uploading table structure.s-- 1. Create Sites Table
CREATE TABLE sites (
    id BIGSERIAL PRIMARY KEY,
    site_name VARCHAR(150) NOT NULL,
    location VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true
);

-- 2. Create Workers Table
CREATE TABLE workers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    designation VARCHAR(20) NOT NULL,
    daily_wage_rate NUMERIC(10, 2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true
);

-- 3. Create Attendance Logs Table
CREATE TABLE attendance_logs (
    id BIGSERIAL PRIMARY KEY,
    worker_id BIGINT NOT NULL REFERENCES workers(id),
    site_id BIGINT NOT NULL REFERENCES sites(id),
    clock_in_timestamp TIMESTAMP NOT NULL,
    clock_out_timestamp TIMESTAMP,
    total_hours NUMERIC(5, 2),
    overtime_hours NUMERIC(5, 2),
    flagged BOOLEAN NOT NULL DEFAULT false
);ql…]()

And given sql Records 

[sql query records.sql](https://github.com/user-attachments/files/28417501/sql.query.records.sql)
-- Insert operational construction sites
INSERT INTO sites (site_name, location, is_active) VALUES
('Greenfield Phase 2', 'Noida Sector 62', true),
('Skyline Towers', 'Delhi Connaught Place', true);

-- Insert contract workers
INSERT INTO workers (name, phone, designation, daily_wage_rate, is_active) VALUES
('Shibu Kumar', '9876543210', 'SUPERVISOR', 950.00, true),
('Amit Sharma', '9876543211', 'MASON', 600.00, true);


### 4. Compiling & Launching the Server
Execute these instructions sequentially in your Linux bash terminal to verify annotation processing configurations and start the application context cleanly:

#### Step A: Flush stale build caches
mvn clean

#### Step B: Ensure local Redis instance is active
sudo systemctl start redis-server

#### Step C: Boot the application profile
mvn spring-boot:run


# Core API Specification (Request & Response Payloads)
### 1. Clock-in Workflow (Persists to DB & Enrolls in Redis)
Registers a worker's shift activation. Blocks instantly if the user tries to register simultaneously across multiple active locations.
#### HTTP Method: POST
#### Endpoint: http://localhost:8080/api/attendance/clock-in
#### Headers: Content-Type: application/json

<img width="1920" height="1080" alt="Screenshot from 2026-05-30 12-33-26" src="https://github.com/user-attachments/assets/0faece4f-6eb6-4c7a-91ab-c6989a110516" />

### 2. Clock-Out Workflow (Updates DB, Evicts Redis, Processes Rules)
Gracefully closes a shift session, computes exact hour allocations, calculates precise overtime thresholds, and issues a flag alert if standard safety compliance limits are broken.
#### HTTP Method: POST
#### Endpoint: http://localhost:8080/api/attendance/clock-out
#### Headers: Content-Type: application/json

<img width="1920" height="1080" alt="Screenshot from 2026-05-30 12-36-47" src="https://github.com/user-attachments/assets/d62d3065-4094-4bb1-a5f4-8a3e6349e873" />

### 3. Fetch Historical Logs (Strict Range & N+1 Performance Proof)
Fetches fully hydrated range logs with clean, bulletproof execution utilizing pagination.
#### HTTP Method: GET
#### Endpoint: http://localhost:8080/api/attendance/log?workerId=1&from=2026-05-01&to=2026-05-31&page=0&size=10

<img width="1920" height="1080" alt="Screenshot from 2026-05-30 12-39-53" src="https://github.com/user-attachments/assets/420db24e-fda0-433d-ac0c-960910679611" />
