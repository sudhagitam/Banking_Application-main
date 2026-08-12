---
name: repo-config-init
description: 'Automatically analyzes your repository structure and creates or updates configuration files needed for the project.'
---

# Repository Configuration Initializer Skill

## Purpose
This skill helps initialize and maintain configuration files in a repository by analyzing the project structure, detected technologies, and dependencies. Use this when you need to set up or update configuration files like `.env`, build configs, Docker configs, or application settings.

## When to Use
- Analyzing a new repository to detect its technology stack
- Creating missing configuration files based on detected dependencies
- Updating existing configs when dependencies or structure changes
- Preparing a project for deployment or containerization
- Setting up environment-specific configurations

## Workflow

### 1. Repository Analysis
- Scan the project structure (recursively up to configurable depth)
- Detect programming languages and frameworks
- Identify build systems (Maven, Gradle, npm, pip, etc.)
- Extract dependencies and versions
- Note key ports and entry points

### 2. Technology Detection
- Look for language identifiers: `pom.xml`, `package.json`, `requirements.txt`, `.csproj`, `go.mod`
- Parse version information from build files
- Identify frameworks: Spring Boot, React, Django, .NET, etc.
- Detect services and their expected ports

### 3. Configuration Generation
- Create base configuration templates based on detected tech
- Include environment variables template
- Set up build tool configurations if missing
- Generate Docker/container configs if containerization is needed
- Create deployment-specific configs (dev, staging, prod)

### 4. File Updates
- Preserve existing manual configurations
- Merge new settings with existing configs
- Create backups before major changes
- Validate configuration syntax

## Key Files to Check
- `pom.xml`, `build.gradle` → Java projects
- `package.json`, `package-lock.json` → Node.js projects
- `requirements.txt`, `setup.py` → Python projects
- `.csproj`, `.sln` → .NET projects
- `go.mod` → Go projects
- `Dockerfile`, `docker-compose.yml` → Container configs
- `.env`, `.env.example` → Environment variables

## Output
- Auto-created/updated configuration files
- Summary report of changes made
- List of required vs. detected configurations
- Recommendations for missing configs
