# Enterprise Banking Simulation System

An enterprise-grade, high-performance banking simulation system designed using a hybrid language architecture (Java Spring Boot + Python). The system features robust authentication, asynchronous task execution, thread pool monitoring, event-driven auditing, and centralized caching.

---

## 🚀 Key Features & Technologies

### 1. Security & Authentication (Spring Security + JWT)
- **Bearer Token Authentication**: Uses stateless JWT tokens generated on login.
- **Secure Password Hashing**: Hashing of user passwords with BCrypt.
- **Stateless Authorization**: All APIs under `/api/**` are protected by `ROLE_USER` authorization rules, managed via custom security filters.
- **H2-Console Support**: Securely enables the in-memory database GUI by configuring Frame Options exceptions.

### 2. High-Performance Asynchronous Queueing & Thread Pooling
- **Transactional Queue**: Transactions submitted to `/api/transfer` are queued onto an in-memory `TransferQueue` immediately (backed by a `BlockingQueue`) to guarantee high throughput and low HTTP response times.
- **Daemon Processing**: `TransferQueueWorker` continuously polls the queue in the background.
- **Thread Pool Monitoring**: Tasks are processed concurrently using a Spring-managed `ThreadPoolTaskExecutor` (named `taskExecutor`), logging thread pool status (active count, queue capacity) in real-time.

### 3. Event-Driven Auditing & Notifications
- **Decoupled Architecture**: Processes emit a `TransferCompletedEvent` upon successful execution.
- **Async Event Listener**: `TransferEventListener` intercepts the event in a background thread to:
  - Simulate real-time SMS alert logs.
  - Increment daily transaction volumes and count logs in the `Analytics` table.

### 4. Enterprise-Grade Caching (Spring Cache)
- **Centralized Caching**: Cached database reads on frequent operations:
  - `@Cacheable` on user accounts and loans listings.
  - `@CacheEvict` on transactions, user creations, or account additions to guarantee cache consistency.

### 5. Aspect-Oriented Programming (AOP Logging)
- **Audit Logging Aspect**: `LoggingAspect` intercepts execution of all methods inside classes annotated with `@Service`.
- **Auditing Details**: Automatically logs execution method parameters, exit results, execution duration in milliseconds, and unhandled exceptions.

### 6. Dynamic JPA Specifications & Projections
- **Performance Projections**: Custom database interfaces (`TransferProjection`) select only required columns, preventing fetching entire database records.
- **Dynamic Queries**: Supports filtering transfers by transaction date ranges and account list IDs.

---

## 🗄️ Database Schemas (Hibernate Mappings)

- **`User`**: Core user accounts containing element-collection mappings for account and loan identifiers.
- **`Account`**: Checking or savings profiles containing user ID, balances, routing numbers, and transaction lists.
- **`Loan`**: Interest rate, terms, principal balance, and repayment progress metadata.
- **`Transfer`**: Audit trail record tracking status (`QUEUED`, `SUCCESS`, `FAILED`), amounts, source/destination accounts, and error logs.
- **`Analytics`**: Running aggregate of transaction volumes and total counts grouped by date.

---

## 🛠️ Getting Started & Setup

### Prerequisites
- **Java**: JDK 17
- **Maven**: 3.8+
- **Python**: 3.7+ (for simulation)

### Project Directory Structure
```text
BankSimulation/
├── TransactionSim/        # Core Java Spring Boot backend
│   ├── src/               # Application source and tests
│   └── pom.xml            # Maven build descriptor
├── simulator.py           # Concurrent Python stress testing tool
└── README.md              # Documentation
```

### 1. Build and Run the Backend
Navigate to the `TransactionSim` directory, build the project, and start the server:
```powershell
cd TransactionSim
mvn clean package
mvn spring-boot:run
```
*Note: The server is configured to run on port `8085` by default to avoid port collisions with other local services (configured in `application.properties`).*

### 2. Run the Automated Tests
Run unit tests (service logic) and integration tests (REST endpoints + security verification):
```powershell
mvn test
```

---

## 🐍 High-Performance Python Simulator (`simulator.py`)

A high-performance transaction generator has been developed in Python to stress-test the backend concurrently. It uses only the **Python Standard Library** (`urllib.request` and `concurrent.futures`), requiring no third-party package installations.

### What the Simulator Does:
1. **Concurrently registers** 5 customers (`Alice`, `Bob`, `Charlie`, `David`, `Eve`).
2. **Authenticates** each user to fetch JWT Bearer tokens.
3. **Creates** both checking and savings accounts for each user.
4. **Stress-tests** the system by concurrently submitting **50 transfers** across thread pools.
5. **Applies for and approves** personal loans for each customer.

### How to Run the Simulator:
Ensure the Spring Boot backend is running, then execute the simulator from the workspace root:
```powershell
python simulator.py
```

### Expected Simulator Output:
```text
====================================================
      ENTERPRISE BANK SIMULATOR (PYTHON ENGINE)     
====================================================

[Step 1] Registering and authenticating clients concurrently...
  + Registered David Miller
  + Registered Charlie Brown
  + Registered Eve Wilson
  + Registered Alice Smith
  + Registered Bob Jones
  * Authenticated Bob (Token acquired)
  * Authenticated Charlie (Token acquired)
  * Authenticated Eve (Token acquired)
  * Authenticated Alice (Token acquired)
  * Authenticated David (Token acquired)

[Step 2] Opening accounts and depositing initial balances...
  + Created CHECKING account for Alice: ID=None, Balance=$1000.0
  + Created SAVINGS account for Alice: ID=None, Balance=$500.0
  ...

Collected 10 accounts across all clients.

[Step 3] Running concurrent transaction stress-test (50 transfers)...
  -> Transfer sample: $90.39 from Account ID 5 to 21
  -> Server Response: {'status': 'QUEUED', 'transactionId': 1, 'transferAmount': None, 'transferType': 'ACCOUNTtoACCOUNTTRANSFER', 'TransferMessage': 'Transfer was queued', 'timestamp': '2026-07-14T18:36:41.2870988'}

[Step 4] Applying for loans for clients...
  + Applied & Approved Personal Loan for Alice: ID=11, Amount=$10000.0
  ...

====================================================
                 SIMULATION REPORT                  
====================================================
Total Transactions Simulated:  50
Successfully Queued:          50
Failed (e.g. invalid bounds): 0
Concurrent Execution Time:     0.121 seconds
Throughput:                    412.9 req/sec
====================================================
```

---

## 🌐 API Reference

### Public Authentication Routes
- **`POST /auth/register`**: Creates a new user profile.
- **`POST /auth/login`**: Accepts user credentials and returns a Bearer JWT Token.

### Secured API Routes (Requires `Authorization: Bearer <token>` Header)
- **`GET /api/accounts`**: Fetches all accounts owned by the authenticated user.
- **`POST /api/accounts`**: Opens a checking or savings account.
- **`POST /api/transfer`**: Submits a transfer task to the asynchronous processing queue.
- **`GET /api/transactions`**: Dynamic search route filtering transactions by date range (`from` & `to` parameters).
- **`GET /api/loans`**: Fetches all loans owned by the user.
- **`POST /api/loans`**: Submits a personal loan application.
- **`POST /api/loans/payment`**: Submits a loan repayment request.