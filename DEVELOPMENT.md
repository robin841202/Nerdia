# Nerdia - Developer Guide

This document provides technical details for developers. For a product overview and screenshots, please refer to the main [README.md](README.md).

## Project Guidelines

To maintain consistency in development, please refer to the following documentation:

- **Git Workflow**: See [GIT_WORKFLOW.md](GIT_WORKFLOW.md) for branching, commit message conventions, and release procedures.
- **AI Agent Team Workflow**: See [AI_WORKFLOW_GUIDE.md](AI_WORKFLOW_GUIDE.md) for our 4+1 Multi-Agent (PM, UI/UX, Senior RD, QA, and Human RD) workflow, rules, and slash commands in Google Antigravity.

## Setup

### Prerequisites
- **Android Studio**: Ladybug | 2024.1.3 or higher.
- **JDK**: 17+ (JDK 21 recommended for Gradle 9.6.0).
- **Gradle**: 9.6.0.

### Instructions
1. Clone the repository.
2. Add your API keys to `gradle.properties`:
   ```properties
   TMDB_API_KEY="your_key"
   YOUTUBE_API_KEY="your_key"
   OMDB_API_KEY="your_key"
   ```
3. Open the project in Android Studio (Ladybug | 2024.1.3 or higher recommended).
4. Build and run.
