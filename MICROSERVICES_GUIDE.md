# How Your Microservices Work

## 🏗️ Architecture Overview

You have **2 independent microservices** that can run separately:

```
┌─────────────────────────────────────────────────────────────┐
│                    Microservices Architecture                │
└─────────────────────────────────────────────────────────────┘

┌──────────────────────┐         ┌──────────────────────┐
│  Employee Service    │         │   Address Service    │
│   Port: 8088         │         │   Port: 8089         │
├──────────────────────┤         ├──────────────────────┤
│  - Employee Entity   │         │  - Address Entity    │
│  - Employee Repo     │         │  - Address Repo      │
│  - Employee Service  │         │  - Address Service   │
│  - Employee Controller│         │  - Address Controller │
│  - EmployeeResponse  │         │  - AddressResponse   │
└──────────────────────┘         └──────────────────────┘
         │                                  │
         │                                  │
         └─────────── MySQL DB ──────────────┘
              (Database: selenium)
```

---

## 📋 Microservice 1: Employee Service (Port 8088)

### **Components:**

1. **Employee Entity** (`Employee.java`)
   - Maps to database table `employee`
   - Fields: `id`, `name`, `email`, `bloodgroup`

2. **Employee Repository** (`EmployeeRepo.java`)
   - Extends `JpaRepository<Employee, Integer>`
   - Provides database operations (findById, findAll, etc.)

3. **Employee Service** (`EmployeeService.java`)
   - Business logic layer
   - Fetches employee from database
   - Converts Entity → Response DTO using ModelMapper

4. **Employee Controller** (`EmployeeController.java`)
   - REST API endpoint
   - Receives HTTP requests
   - Returns JSON responses

### **Request Flow:**

```
Client Request
    ↓
GET http://localhost:8088/employee/{id}
    ↓
EmployeeController.getEmployeeDetails(id)
    ↓
EmployeeService.getEmployeeById(id)
    ↓
EmployeeRepo.findById(id) → Database Query
    ↓
Employee Entity → ModelMapper → EmployeeResponse DTO
    ↓
JSON Response to Client
```

### **Example API Call:**

```bash
# Get employee with ID 1
GET http://localhost:8088/employee/1

# Response:
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "bloodgroup": "O+"
}
```

---

## 📋 Microservice 2: Address Service (Port 8089)

### **Components:**

1. **Address Entity** (`AddressEntity.java`)
   - Maps to database table `address`
   - Fields: `add_Id`, `lane_1`, `lane_2`, `state`, `zip`

2. **Address Repository** (`AddressRepo.java`)
   - Extends `JpaRepository<AddressEntity, Integer>`
   - Provides database operations

3. **Address Service** (`AddressService.java`)
   - Business logic layer
   - Fetches address from database
   - Converts Entity → Response DTO using ModelMapper

4. **Address Controller** (`AddressController.java`)
   - REST API endpoint
   - Receives HTTP requests
   - Returns JSON responses

### **Request Flow:**

```
Client Request
    ↓
GET http://localhost:8089/address/{empid}
    ↓
AddressController.getAddressById(empid)
    ↓
AddressService.getAddressById(empid)
    ↓
AddressRepo.findById(empid) → Database Query
    ↓
AddressEntity → ModelMapper → AddressResponse DTO
    ↓
JSON Response to Client
```

### **Example API Call:**

```bash
# Get address for employee ID 1
GET http://localhost:8089/address/1

# Response:
{
  "add_Id": 1,
  "lane_1": "123 Main St",
  "lane_2": "Apt 4B",
  "state": "California",
  "zip": 90210
}
```

---

## 🔄 How Microservices Communicate

### **Option 1: Direct HTTP Communication (REST)**

One microservice can call another using `RestTemplate` or `WebClient`:

**Example: Employee Service calling Address Service**

```java
// In EmployeeService.java
@Autowired
private RestTemplate restTemplate;

public EmployeeWithAddressResponse getEmployeeWithAddress(int id) {
    // 1. Get employee from own database
    Employee employee = employeeRepo.findById(id).orElse(null);
    
    // 2. Call Address microservice
    String addressUrl = "http://localhost:8089/address/" + id;
    AddressResponse address = restTemplate.getForObject(
        addressUrl, 
        AddressResponse.class
    );
    
    // 3. Combine data
    EmployeeWithAddressResponse response = new EmployeeWithAddressResponse();
    response.setEmployee(employee);
    response.setAddress(address);
    
    return response;
}
```

### **Option 2: API Gateway Pattern**

Use an API Gateway (like Spring Cloud Gateway) to route requests:

```
Client → API Gateway → Employee Service
                      → Address Service
```

---

## 🚀 How to Run Your Microservices

### **Step 1: Start MySQL Database**
```bash
# Make sure MySQL is running on localhost:3306
# Database name: selenium
# Username: root
# Password: root
```

### **Step 2: Start Employee Service**
```bash
cd Employee
mvn spring-boot:run
# Service starts on http://localhost:8088
```

### **Step 3: Start Address Service** (in a new terminal)
```bash
cd Address
mvn spring-boot:run
# Service starts on http://localhost:8089
```

### **Step 4: Test the Services**

**Test Employee Service:**
```bash
curl http://localhost:8088/employee/1
```

**Test Address Service:**
```bash
curl http://localhost:8089/address/1
```

---

## 🎯 Key Microservices Principles in Your Project

### ✅ **1. Independent Deployment**
- Each service has its own `pom.xml`
- Each service runs on different port (8088 vs 8089)
- Can be deployed separately

### ✅ **2. Single Responsibility**
- **Employee Service**: Manages employee data
- **Address Service**: Manages address data

### ✅ **3. Database per Service** (Shared DB in your case)
- Both services use same database (`selenium`)
- But they access different tables (`employee` vs `address`)
- In production, each service should have its own database

### ✅ **4. API Communication**
- Services expose REST APIs
- Can communicate via HTTP calls
- Return JSON responses

### ✅ **5. Service Independence**
- If Address service is down, Employee service still works
- Services don't directly depend on each other's code

---

## 📊 Complete Request Flow Example

### **Scenario: Get Employee with Address**

```
┌─────────┐
│ Client  │
└────┬────┘
     │
     │ 1. GET /employee/1
     ↓
┌─────────────────────┐
│ Employee Service    │
│ (Port 8088)         │
├─────────────────────┤
│ Controller          │
│   ↓                 │
│ Service             │
│   ↓                 │
│ Repository          │
│   ↓                 │
│ Database            │
│ (employee table)    │
└────┬────────────────┘
     │
     │ 2. Employee Data Retrieved
     │
     │ 3. HTTP Call to Address Service
     ↓
┌─────────────────────┐
│ Address Service     │
│ (Port 8089)         │
├─────────────────────┤
│ Controller          │
│   ↓                 │
│ Service             │
│   ↓                 │
│ Repository          │
│   ↓                 │
│ Database            │
│ (address table)     │
└────┬────────────────┘
     │
     │ 4. Address Data Retrieved
     ↓
┌─────────────────────┐
│ Employee Service    │
│ Combines Data       │
└────┬────────────────┘
     │
     │ 5. Combined Response
     ↓
┌─────────┐
│ Client  │
└─────────┘
```

---

## 🔧 Next Steps to Enhance Your Microservices

1. **Add Service Discovery** (Eureka/Consul)
   - Services can find each other dynamically

2. **Add API Gateway** (Spring Cloud Gateway)
   - Single entry point for all services

3. **Add Circuit Breaker** (Resilience4j)
   - Handle failures gracefully

4. **Add Distributed Tracing** (Zipkin/Jaeger)
   - Track requests across services

5. **Separate Databases**
   - Each service should have its own database

6. **Add Configuration Server** (Spring Cloud Config)
   - Centralized configuration management

---

## 📝 Summary

Your microservices architecture:
- ✅ **2 independent services** running on different ports
- ✅ **RESTful APIs** for communication
- ✅ **Separate business logic** (Employee vs Address)
- ✅ **Can scale independently**
- ✅ **Fault isolation** (one service failure doesn't crash the other)

This is a solid foundation for a microservices architecture!

