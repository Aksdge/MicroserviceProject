# Build Guide for Microservices

## 🏗️ How to Build Your Microservices

### **Option 1: Build Individual Services (Using Maven Wrapper)**

#### **Build Employee Service:**
```bash
cd Employee
.\mvnw.cmd clean install
```

#### **Build Address Service:**
```bash
cd Address
.\mvnw.cmd clean install
```

**Note:** If `mvn` command is not found, use `.\mvnw.cmd` (Maven Wrapper) instead!

---

### **Option 2: Build Both Services from Root**

If you're in the root directory (`MicroserviceProject`):

```bash
# Build Employee Service
cd Employee
.\mvnw.cmd clean install
cd ..

# Build Address Service
cd Address
.\mvnw.cmd clean install
cd ..
```

---

## 📦 What Happens During Build

1. **`mvn clean`** - Removes old compiled files (`target` folder)
2. **`mvn install`** - Compiles, tests, and packages the project
3. Creates JAR file in `target` folder

---

## 🎯 Build Commands Explained

### **Basic Build Commands:**

| Command | Description |
|---------|-------------|
| `mvn clean` | Removes `target` directory (old build files) |
| `mvn compile` | Compiles source code only |
| `mvn test` | Runs tests |
| `mvn package` | Compiles, tests, and creates JAR file |
| `mvn install` | Same as package + installs to local Maven repository |
| `mvn clean install` | Clean + compile + test + package + install |

---

## 🚀 Step-by-Step Build Process

### **Step 1: Navigate to Service Directory**
```bash
cd Employee
# or
cd Address
```

### **Step 2: Clean Previous Builds (Optional but Recommended)**
```bash
.\mvnw.cmd clean
```

### **Step 3: Build the Project**
```bash
.\mvnw.cmd install
```

### **Step 4: Check Build Output**
After successful build, you'll see:
```
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### **Step 5: Find the JAR File**
The JAR file will be in:
```
target/demo-0.0.1-SNAPSHOT.jar
```

---

## 🏃 Running the Built JAR

After building, you can run the JAR directly:

### **Run Employee Service:**
```bash
cd Employee
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### **Run Address Service:**
```bash
cd Address
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## 🔧 Common Build Issues & Solutions

### **Issue 1: Maven Not Found**
**Error:** `'mvn' is not recognized`

**Solution:** 
- Use Maven Wrapper (already included in your project):
```bash
# Windows PowerShell
.\mvnw.cmd clean install

# Windows CMD
mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

### **Issue 2: Port Already in Use**
**Error:** `Port 8088/8089 already in use`

**Solution:**
- Stop the service running on that port
- Or change port in `application.properties`

### **Issue 3: Database Connection Failed**
**Error:** `Cannot connect to MySQL`

**Solution:**
- Make sure MySQL is running
- Check database credentials in `application.properties`
- Verify database `selenium` exists

### **Issue 4: Compilation Errors**
**Error:** `BUILD FAILURE`

**Solution:**
```bash
# Clean and rebuild
mvn clean install

# Or skip tests if tests are failing
mvn clean install -DskipTests
```

---

## 📋 Complete Build & Run Workflow

### **For Employee Service:**
```bash
# 1. Navigate
cd Employee

# 2. Build
.\mvnw.cmd clean install

# 3. Run
java -jar target/demo-0.0.1-SNAPSHOT.jar
# OR
.\mvnw.cmd spring-boot:run
```

### **For Address Service:**
```bash
# 1. Navigate
cd Address

# 2. Build
.\mvnw.cmd clean install

# 3. Run
java -jar target/demo-0.0.1-SNAPSHOT.jar
# OR
.\mvnw.cmd spring-boot:run
```

---

## 🎯 Quick Build Scripts

### **Windows Batch Script (build-all.bat):**
```batch
@echo off
echo Building Employee Service...
cd Employee
call mvn clean install
cd ..

echo Building Address Service...
cd Address
call mvn clean install
cd ..

echo Build Complete!
pause
```

### **Linux/Mac Shell Script (build-all.sh):**
```bash
#!/bin/bash
echo "Building Employee Service..."
cd Employee
mvn clean install
cd ..

echo "Building Address Service..."
cd Address
mvn clean install
cd ..

echo "Build Complete!"
```

---

## ✅ Build Verification

After building, verify:

1. **Check JAR file exists:**
   ```bash
   ls target/*.jar
   ```

2. **Check build size:**
   ```bash
   # Should be around 20-50 MB
   dir target\*.jar
   ```

3. **Test the JAR:**
   ```bash
   java -jar target/demo-0.0.1-SNAPSHOT.jar
   ```

---

## 📊 Build Output Location

After successful build:

```
Employee/
├── target/
│   ├── demo-0.0.1-SNAPSHOT.jar  ← Executable JAR
│   ├── classes/                  ← Compiled classes
│   └── ...
└── ...

Address/
├── target/
│   ├── demo-0.0.1-SNAPSHOT.jar  ← Executable JAR
│   ├── classes/                  ← Compiled classes
│   └── ...
└── ...
```

---

## 🎉 Summary

**To build:**
```bash
cd Employee
.\mvnw.cmd clean install
```

**To run:**
```bash
.\mvnw.cmd spring-boot:run
# OR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

That's it! Your microservices are built and ready to run! 🚀

