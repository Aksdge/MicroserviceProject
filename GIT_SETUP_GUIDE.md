# Git Setup Guide - Push to GitHub

## 🚀 Step-by-Step Guide to Create Git Repository and Push to GitHub

---

## 📋 Prerequisites

1. **Git installed** - Check with: `git --version`
2. **GitHub account** - Create at [github.com](https://github.com)
3. **GitHub repository** - Create a new repository on GitHub (we'll do this in steps)

---

## 🔧 Step 1: Initialize Git Repository

Open PowerShell in your project root directory:

```powershell
# Navigate to project root
cd C:\javaPracties\MicroserviceProject

# Initialize Git repository
git init
```

**Expected output:**
```
Initialized empty Git repository in C:/javaPracties/MicroserviceProject/.git/
```

---

## 👤 Step 2: Configure Git (If Not Already Done)

Set your name and email (only needed once per machine):

```powershell
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

---

## 📝 Step 3: Check What Files Will Be Added

```powershell
git status
```

This shows all files. We'll add them in the next step.

---

## ➕ Step 4: Add Files to Git

```powershell
# Add all files
git add .

# Or add specific files
git add Employee/
git add Address/
git add *.md
git add .gitignore
```

**Verify what's staged:**
```powershell
git status
```

---

## 💾 Step 5: Create Initial Commit

```powershell
git commit -m "Initial commit: Employee and Address microservices with inter-service communication"
```

**Expected output:**
```
[main (root-commit) xxxxxxx] Initial commit: Employee and Address microservices with inter-service communication
 X files changed, Y insertions(+)
```

---

## 🌐 Step 6: Create GitHub Repository

1. Go to [github.com](https://github.com)
2. Click **"+"** → **"New repository"**
3. Repository name: `MicroserviceProject` (or your preferred name)
4. Description: `Employee and Address Microservices with Spring Boot`
5. Choose **Public** or **Private**
6. **DO NOT** initialize with README, .gitignore, or license (we already have files)
7. Click **"Create repository"**

---

## 🔗 Step 7: Connect Local Repository to GitHub

After creating the repository, GitHub will show you commands. Use these:

```powershell
# Add remote repository (replace YOUR_USERNAME with your GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/MicroserviceProject.git

# Verify remote was added
git remote -v
```

**Example:**
```powershell
git remote add origin https://github.com/johndoe/MicroserviceProject.git
```

---

## 📤 Step 8: Push to GitHub

```powershell
# Push to GitHub (first time)
git branch -M main
git push -u origin main
```

**If prompted for credentials:**
- Username: Your GitHub username
- Password: Use a **Personal Access Token** (not your GitHub password)

---

## 🔑 Step 9: Create Personal Access Token (If Needed)

If Git asks for password, you need a Personal Access Token:

1. Go to GitHub → **Settings** → **Developer settings** → **Personal access tokens** → **Tokens (classic)**
2. Click **"Generate new token"** → **"Generate new token (classic)"**
3. Give it a name: `MicroserviceProject`
4. Select scopes: Check **`repo`** (full control of private repositories)
5. Click **"Generate token"**
6. **Copy the token** (you won't see it again!)
7. Use this token as your password when pushing

---

## ✅ Step 10: Verify Push

1. Go to your GitHub repository page
2. Refresh the page
3. You should see all your files!

---

## 🔄 Common Git Commands

### **Check Status**
```powershell
git status
```

### **Add Files**
```powershell
git add .
git add filename.java
```

### **Commit Changes**
```powershell
git commit -m "Your commit message"
```

### **Push to GitHub**
```powershell
git push origin main
```

### **Pull Latest Changes**
```powershell
git pull origin main
```

### **View Commit History**
```powershell
git log
```

### **Create New Branch**
```powershell
git checkout -b feature-branch-name
```

---

## 📝 Step-by-Step Summary (Quick Reference)

```powershell
# 1. Initialize Git
git init

# 2. Add files
git add .

# 3. Commit
git commit -m "Initial commit: Microservices project"

# 4. Add remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/MicroserviceProject.git

# 5. Push to GitHub
git branch -M main
git push -u origin main
```

---

## 🎯 Complete Example Workflow

```powershell
# Navigate to project
cd C:\javaPracties\MicroserviceProject

# Initialize Git
git init

# Configure Git (if first time)
git config --global user.name "John Doe"
git config --global user.email "john@example.com"

# Add all files
git add .

# Commit
git commit -m "Initial commit: Employee and Address microservices"

# Add remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/MicroserviceProject.git

# Push
git branch -M main
git push -u origin main
```

---

## ⚠️ Troubleshooting

### **Issue: "remote origin already exists"**
```powershell
# Remove existing remote
git remote remove origin

# Add again
git remote add origin https://github.com/YOUR_USERNAME/MicroserviceProject.git
```

### **Issue: "Authentication failed"**
- Use Personal Access Token instead of password
- Make sure token has `repo` scope

### **Issue: "Failed to push some refs"**
```powershell
# Pull first, then push
git pull origin main --allow-unrelated-histories
git push -u origin main
```

### **Issue: "Nothing to commit"**
- Make sure you've added files: `git add .`
- Check status: `git status`

---

## 📚 Next Steps After Pushing

1. **Add README.md** - Describe your project
2. **Add LICENSE** - Choose a license
3. **Create branches** - For features/bugs
4. **Set up CI/CD** - Automate builds/tests
5. **Add collaborators** - Invite team members

---

## 🎉 You're Done!

Your microservices project is now on GitHub! 🚀

Share the repository URL with others:
```
https://github.com/YOUR_USERNAME/MicroserviceProject
```

