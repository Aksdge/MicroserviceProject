# Inter-Service Communication Example

This guide shows you how to make your Employee service call the Address service.

## 📦 Step 1: Add RestTemplate Configuration

Create a configuration class in the **Employee** service:

**File: `Employee/src/main/java/com/example/employeeapp/config/RestTemplateConfig.java`**

```java
package com.example.employeeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

## 📦 Step 2: Create Combined Response DTO

**File: `Employee/src/main/java/com/example/employeeapp/EmployeeResponse/EmployeeWithAddressResponse.java`**

```java
package com.example.employeeapp.EmployeeResponse;

public class EmployeeWithAddressResponse {
    
    private EmployeeResponse employee;
    private AddressResponse address;
    
    // Getters and Setters
    public EmployeeResponse getEmployee() {
        return employee;
    }
    
    public void setEmployee(EmployeeResponse employee) {
        this.employee = employee;
    }
    
    public AddressResponse getAddress() {
        return address;
    }
    
    public void setAddress(AddressResponse address) {
        this.address = address;
    }
}
```

## 📦 Step 3: Create AddressResponse DTO in Employee Service

**File: `Employee/src/main/java/com/example/employeeapp/EmployeeResponse/AddressResponse.java`**

```java
package com.example.employeeapp.EmployeeResponse;

public class AddressResponse {
    
    private int add_Id;
    private String lane_1;
    private String lane_2;
    private String state;
    private int zip;
    
    // Getters and Setters
    public int getAdd_Id() {
        return add_Id;
    }
    
    public void setAdd_Id(int add_Id) {
        this.add_Id = add_Id;
    }
    
    public String getLane_1() {
        return lane_1;
    }
    
    public void setLane_1(String lane_1) {
        this.lane_1 = lane_1;
    }
    
    public String getLane_2() {
        return lane_2;
    }
    
    public void setLane_2(String lane_2) {
        this.lane_2 = lane_2;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public int getZip() {
        return zip;
    }
    
    public void setZip(int zip) {
        this.zip = zip;
    }
}
```

## 📦 Step 4: Update EmployeeService to Call Address Service

**Update: `Employee/src/main/java/com/example/employeeapp/EmployeeService/EmployeeService.java`**

```java
package com.example.employeeapp.EmployeeService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.employeeapp.EmployeeResponse.AddressResponse;
import com.example.employeeapp.EmployeeResponse.EmployeeResponse;
import com.example.employeeapp.EmployeeResponse.EmployeeWithAddressResponse;
import com.example.employeeapp.employeeEntity.Employee;
import com.example.employeeapp.employeeRepo.EmployeeRepo;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private RestTemplate restTemplate;
    
    // Address Service URL - in production, use configuration
    private static final String ADDRESS_SERVICE_URL = "http://localhost:8089/address/";

    public EmployeeResponse getEmployeeById(int id) {
        Employee employee = employeeRepo.findById(id).orElse(null);
        if (employee == null) {
            return null;
        }
        EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);
        return employeeResponse;
    }
    
    // NEW METHOD: Get Employee with Address
    public EmployeeWithAddressResponse getEmployeeWithAddress(int id) {
        // 1. Get employee from own database
        Employee employee = employeeRepo.findById(id).orElse(null);
        if (employee == null) {
            return null;
        }
        
        EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);
        
        // 2. Call Address microservice
        try {
            String addressUrl = ADDRESS_SERVICE_URL + id;
            ResponseEntity<AddressResponse> addressResponse = restTemplate.getForEntity(
                addressUrl, 
                AddressResponse.class
            );
            
            // 3. Combine data
            EmployeeWithAddressResponse response = new EmployeeWithAddressResponse();
            response.setEmployee(employeeResponse);
            
            if (addressResponse.getStatusCode() == HttpStatus.OK && addressResponse.getBody() != null) {
                response.setAddress(addressResponse.getBody());
            }
            
            return response;
        } catch (Exception e) {
            // Handle error - Address service might be down
            System.err.println("Error calling Address service: " + e.getMessage());
            EmployeeWithAddressResponse response = new EmployeeWithAddressResponse();
            response.setEmployee(employeeResponse);
            response.setAddress(null); // Address not available
            return response;
        }
    }
}
```

## 📦 Step 5: Add New Endpoint in EmployeeController

**Update: `Employee/src/main/java/com/example/employeeapp/employeeController/EmployeeController.java`**

```java
package com.example.employeeapp.employeeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeeapp.EmployeeResponse.EmployeeResponse;
import com.example.employeeapp.EmployeeResponse.EmployeeWithAddressResponse;
import com.example.employeeapp.EmployeeService.EmployeeService;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeDetails(@PathVariable int id) {
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
        if (employeeResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
    }
    
    // NEW ENDPOINT: Get Employee with Address
    @GetMapping("/employee/{id}/with-address")
    public ResponseEntity<EmployeeWithAddressResponse> getEmployeeWithAddress(@PathVariable int id) {
        EmployeeWithAddressResponse response = employeeService.getEmployeeWithAddress(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
```

## 🧪 Testing the Communication

### **1. Start Both Services:**
```bash
# Terminal 1: Start Employee Service
cd Employee
mvn spring-boot:run

# Terminal 2: Start Address Service
cd Address
mvn spring-boot:run
```

### **2. Test Individual Services:**

**Get Employee Only:**
```bash
curl http://localhost:8088/employee/1
```

**Get Address Only:**
```bash
curl http://localhost:8089/address/1
```

### **3. Test Combined Endpoint:**

**Get Employee with Address (Employee service calls Address service):**
```bash
curl http://localhost:8088/employee/1/with-address
```

**Expected Response:**
```json
{
  "employee": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "bloodgroup": "O+"
  },
  "address": {
    "add_Id": 1,
    "lane_1": "123 Main St",
    "lane_2": "Apt 4B",
    "state": "California",
    "zip": 90210
  }
}
```

## 🔄 Request Flow Diagram

```
┌─────────┐
│ Client  │
└────┬────┘
     │
     │ GET /employee/1/with-address
     ↓
┌─────────────────────────────┐
│ Employee Service (8088)     │
├─────────────────────────────┤
│ EmployeeController          │
│   ↓                         │
│ EmployeeService             │
│   ├─> getEmployeeById(1)    │
│   │   └─> Database Query    │
│   │                         │
│   └─> Call Address Service  │
│       │                      │
│       │ HTTP GET             │
│       │ http://localhost:    │
│       │ 8089/address/1       │
│       ↓                      │
└───────┼──────────────────────┘
        │
        │ HTTP Request
        ↓
┌─────────────────────────────┐
│ Address Service (8089)       │
├─────────────────────────────┤
│ AddressController           │
│   ↓                         │
│ AddressService              │
│   ↓                         │
│ AddressRepo                 │
│   ↓                         │
│ Database Query              │
│   ↓                         │
│ AddressResponse (JSON)      │
└───────┬─────────────────────┘
        │
        │ HTTP Response
        ↓
┌─────────────────────────────┐
│ Employee Service            │
│ Combines:                   │
│ - EmployeeResponse          │
│ - AddressResponse           │
│   ↓                         │
│ EmployeeWithAddressResponse │
└───────┬─────────────────────┘
        │
        │ JSON Response
        ↓
┌─────────┐
│ Client  │
└─────────┘
```

## ⚠️ Important Notes

1. **Service Discovery**: In production, use service discovery (Eureka) instead of hardcoded URLs
2. **Error Handling**: Always handle cases where Address service is down
3. **Timeout**: Configure RestTemplate timeout to avoid hanging requests
4. **Circuit Breaker**: Use Resilience4j or Hystrix for fault tolerance
5. **Load Balancing**: Use multiple instances of Address service with load balancing

## 🚀 Production-Ready Improvements

### **1. Use Configuration Properties:**

**application.properties:**
```properties
address.service.url=http://localhost:8089
```

**In Service:**
```java
@Value("${address.service.url}")
private String addressServiceUrl;
```

### **2. Add Timeout Configuration:**

```java
@Bean
public RestTemplate restTemplate() {
    HttpComponentsClientHttpRequestFactory factory = 
        new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(3000);
    factory.setReadTimeout(3000);
    return new RestTemplate(factory);
}
```

### **3. Use WebClient (Reactive - Better for Async):**

```java
@Autowired
private WebClient.Builder webClientBuilder;

public EmployeeWithAddressResponse getEmployeeWithAddress(int id) {
    // ... get employee ...
    
    AddressResponse address = webClientBuilder.build()
        .get()
        .uri("http://localhost:8089/address/" + id)
        .retrieve()
        .bodyToMono(AddressResponse.class)
        .block();
    
    // ... combine and return ...
}
```

This example shows how your microservices can communicate with each other!

