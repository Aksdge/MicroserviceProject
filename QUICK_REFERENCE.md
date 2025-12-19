# Quick Reference: Inter-Service Communication

## ✅ What Was Implemented

### Files Created:
1. ✅ `RestTemplateConfig.java` - Configures RestTemplate for HTTP calls
2. ✅ `AddressResponse.java` - DTO to receive Address service response  
3. ✅ `EmployeeWithAddressResponse.java` - Combined response DTO

### Files Updated:
1. ✅ `EmployeeService.java` - Added `getEmployeeWithAddress()` method
2. ✅ `EmployeeController.java` - Added `/employee/{id}/with-address` endpoint

---

## 🏗️ How to Build

### Build Employee Service:
```bash
cd Employee
.\mvnw.cmd clean install
```

### Build Address Service:
```bash
cd Address
.\mvnw.cmd clean install
```

### Build Both (from root):
```bash
cd Employee && .\mvnw.cmd clean install && cd ..
cd Address && .\mvnw.cmd clean install && cd ..
```

**Note:** Use `.\mvnw.cmd` instead of `mvn` if Maven is not installed!

**After build, JAR files will be in:**
- `Employee/target/demo-0.0.1-SNAPSHOT.jar`
- `Address/target/demo-0.0.1-SNAPSHOT.jar`

---

## 🚀 How to Run

### Option 1: Using Maven Wrapper (Development)
```bash
# Terminal 1 - Employee Service
cd Employee
.\mvnw.cmd spring-boot:run

# Terminal 2 - Address Service
cd Address
.\mvnw.cmd spring-boot:run
```

### Option 2: Using JAR (Production)
```bash
# Terminal 1 - Employee Service
cd Employee
java -jar target/demo-0.0.1-SNAPSHOT.jar

# Terminal 2 - Address Service
cd Address
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## 🧪 How to Test

### Step 1: Start Both Services

**Terminal 1 - Employee Service:**
```bash
cd Employee
mvn spring-boot:run
```
✅ Wait for: `Started DemoApplication`

**Terminal 2 - Address Service:**
```bash
cd Address
mvn spring-boot:run
```
✅ Wait for: `Started AddressApplication`

### Step 2: Test the New Endpoint

**In Browser or Postman:**
```
GET http://localhost:8088/employee/1/with-address
```

**Or using curl:**
```bash
curl http://localhost:8088/employee/1/with-address
```

---

## 📊 What Happens Behind the Scenes

```
1. Client calls: GET /employee/1/with-address
   ↓
2. EmployeeController receives request
   ↓
3. EmployeeService.getEmployeeWithAddress(1)
   ├─> Gets employee from database
   └─> Calls Address service via RestTemplate
       │
       └─> GET http://localhost:8089/address/1
           ↓
4. Address service returns address data
   ↓
5. Employee service combines both responses
   ↓
6. Returns combined JSON to client
```

---

## 🎯 Endpoints Available

| Endpoint | Service | Description |
|----------|---------|-------------|
| `GET /employee/{id}` | Employee | Get employee only |
| `GET /employee/{id}/with-address` | Employee | Get employee + address (calls Address service) |
| `GET /address/{empid}` | Address | Get address only |

---

## 💡 Key Concepts

1. **RestTemplate**: Spring class for making HTTP calls
2. **DTO (Data Transfer Object)**: Classes to transfer data between services
3. **Service-to-Service Communication**: One microservice calling another via HTTP

---

## ⚠️ Troubleshooting

**Problem:** Address service not found
- **Solution:** Make sure Address service is running on port 8089

**Problem:** Connection refused
- **Solution:** Check both services are running and ports are correct

**Problem:** Null address in response
- **Solution:** Check if address exists in database for that employee ID

