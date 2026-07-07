# BDD Selenium OpenShift

![CI](https://github.com/nageshkumar-gh/bdd-selenium-framework/actions/workflows/ci.yml/badge.svg?style=flat-square)

![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.43.0-43B02A?style=flat-square&logo=selenium&logoColor=white)
![Cucumber](https://img.shields.io/badge/Cucumber-7.34.3-23D96C?style=flat-square&logo=cucumber&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.x-FF6C37?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
![Platform](https://img.shields.io/badge/Platform-Chrome%20%7C%20Firefox-yellow?style=flat-square)

A production-ready **BDD (Behaviour-Driven Development) test automation framework** built with Selenium WebDriver, Cucumber, and TestNG, targeting the [OrangeHRM](https://opensource-demo.orangehrmlive.com) open-source HR management demo application.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Setup & Installation](#setup--installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Coverage](#test-coverage)
- [Reports](#reports)
- [Design Patterns](#design-patterns)

---

## Overview

This framework automates end-to-end functional tests for the OrangeHRM demo application using a clean layered architecture: **Feature files → Step Definitions → Action classes → Page Objects**. Tests are written in plain English (Gherkin), making them readable by non-technical stakeholders while remaining fully executable.

The framework is designed for:
- **Parallel-safe execution** via `ThreadLocal` WebDriver management
- **Cross-browser support** (Chrome and Firefox, headless or headed)
- **CI/CD compatibility** with all settings overridable via system properties
- **Easy extension** — adding a new module requires only a new feature file, step class, action class, and page class

---

## Features

- **BDD with Cucumber** — Gherkin feature files as the single source of truth for test scenarios
- **Page Object Model (POM)** — encapsulates element locators and interactions per page
- **Action Layer** — separates business-level operations from raw element interactions
- **ThreadLocal WebDriver** — safe for parallel test execution; no shared browser state between scenarios
- **Configurable via properties file** — browser, headless mode, timeouts, screenshot behaviour, retry count
- **System property overrides** — every config value can be overridden at runtime (e.g. `-Dheadless=false`)
- **Tag-based filtering** — run any subset of scenarios using Cucumber tags (`@sanity`, `@regression`, `@auth`, etc.)
- **HTML & JSON reports** — Cucumber generates human-readable reports out of the box
- **Auto-login hook** — scenarios that are not tagged `@auth` are automatically logged in before the scenario starts

---

## Tech Stack

| Tool | Version | Purpose                           |
|------|---------|-----------------------------------|
| Java | 24 | Primary language                  |
| Selenium WebDriver | 4.43.0 | Browser automation                |
| Cucumber Java | 7.34.3 | BDD framework   |
| Cucumber TestNG | 7.34.3 | TestNG integration for Cucumber   |
| TestNG | 7.x | Test execution engine             |
| Maven | 3.x | Build & dependency management     |
| Maven Surefire Plugin | 3.5.5 | Runs TestNG suite via `testng.xml` |

---

## Project Structure

```
bdd-selenium-framework/
├── pom.xml                              # Maven build descriptor
├── testng.xml                           # TestNG suite configuration
├── qodana.yaml                          # Static analysis config
└── src/
    └── test/
        ├── java/
        │   ├── bases/
        │   │   └── BasePage.java        # Abstract base with reusable Selenium helpers
        │   ├── config/
        │   │   └── ConfigReader.java    # Singleton config loader (config.properties)
        │   ├── driver/
        │   │   └── DriverFactory.java   # ThreadLocal WebDriver lifecycle manager
        │   ├── hooks/
        │   │   └── Hooks.java           # Cucumber @Before/@After scenario hooks
        │   ├── pages/
        │   │   ├── LoginPage.java       # Login screen locators & raw interactions
        │   │   ├── AdminPage.java       # Admin module page elements
        │   │   ├── PIMPage.java         # PIM module page elements
        │   │   ├── ClaimPage.java       # Claim module page elements
        │   │   ├── BuzzPage.java        # Buzz module page elements
        │   │   ├── MenuItemPage.java    # Left-nav menu elements
        │   │   └── TopbarPage.java      # Top navigation bar elements
        │   ├── actions/
        │   │   ├── LoginAction.java     # Business operations for login
        │   │   ├── AdminAction.java     # Business operations for admin module
        │   │   ├── PIMAction.java       # Business operations for PIM module
        │   │   ├── ClaimAction.java     # Business operations for claims
        │   │   ├── BuzzAction.java      # Business operations for buzz feed
        │   │   ├── MenuItemAction.java  # Navigation menu interactions
        │   │   └── TopbarAction.java    # Top bar interactions
        │   ├── steps/
        │   │   ├── LoginStep.java       # Gherkin step bindings for login
        │   │   ├── AdminStep.java       # Gherkin step bindings for admin
        │   │   ├── PIMStep.java         # Gherkin step bindings for PIM
        │   │   ├── ClaimStep.java       # Gherkin step bindings for claims
        │   │   ├── BuzzStep.java        # Gherkin step bindings for buzz
        │   │   └── NavigationStep.java  # Gherkin step bindings for navigation
        │   └── runner/
        │       └── TestRunner.java      # Cucumber + TestNG runner entry point
        └── resources/
            ├── config/
            │   └── config.properties    # All runtime configuration
            └── features/
                ├── login.feature        # Login scenarios
                ├── admin.feature        # Admin module scenarios
                ├── Pim.feature          # PIM module scenarios
                ├── claim.feature        # Claim module scenarios
                ├── buzz.feature         # Buzz module scenarios
                └── navigation.feature   # Navigation scenarios
```

---

## Architecture

The framework uses a **four-layer architecture** to keep each concern isolated:

```
┌─────────────────────────────────────────────┐
│           Feature Files (Gherkin)           │  ← What to test (business language)
├─────────────────────────────────────────────┤
│              Step Definitions               │  ← Map Gherkin → Java method calls
├─────────────────────────────────────────────┤
│               Action Classes                │  ← How to perform a business operation
├─────────────────────────────────────────────┤
│               Page Objects                  │  ← Where elements live on the page
└─────────────────────────────────────────────┘
                      │
              BasePage (common Selenium helpers + explicit wait)
                      │
              DriverFactory (ThreadLocal WebDriver)
                      │
              ConfigReader (singleton, config.properties)
```

### Key Design Decisions

**ThreadLocal WebDriver** — `DriverFactory` stores each `WebDriver` in a `ThreadLocal`, so parallel scenario execution never shares a browser instance. `Hooks.after()` calls `quitDriver()` which also calls `DRIVER.remove()` to prevent thread pool retention.

**Auto-login hook** — `Hooks` has a `@Before(order = 3, value = "not @auth")` hook that logs in automatically for any scenario that isn't testing authentication itself. Scenarios tagged `@auth` get a clean session without being pre-logged-in.

**Singleton ConfigReader** — All configuration is loaded once from `config.properties` and cached. Any value can be overridden at runtime with a `-D` system property, making CI parameterisation simple.

---

## Prerequisites

| Requirement | Minimum Version |
|-------------|----------------|
| JDK | 24 |
| Maven | 3.8+ |
| Google Chrome | Latest stable |
| Mozilla Firefox | Latest stable (optional) |
| Internet access | Required (tests hit the OrangeHRM demo site) |

> **Note:** Selenium 4.x manages browser drivers automatically via Selenium Manager — no manual `chromedriver` download is needed.

---

## Setup & Installation

```bash
# 1. Clone the repository
git clone https://github.com/nageshkumar-gh/bdd-selenium-framework.git
cd bdd-selenium-framework

# 2. Verify Java and Maven are available
java -version
mvn -version

# 3. Install dependencies (downloads all jars into your local .m2 cache)
mvn dependency:resolve
```

---

## Configuration

All settings live in [`src/test/resources/config/config.properties`](src/test/resources/config/config.properties):

| Property | Default | Description |
|----------|---------|-------------|
| `base.url` | OrangeHRM demo login URL | Entry URL opened before each scenario |
| `username` | `Admin` | Default login username |
| `password` | `admin123` | Default login password |
| `browser` | `chrome` | Browser to launch (`chrome` or `firefox`) |
| `headless` | `true` | Run browser in headless mode |
| `wait.timeout` | `15` | Explicit wait timeout in seconds |
| `explicit.wait.timeout` | `20` | Element visibility wait timeout in seconds |
| `screenshot.on.failure` | `false` | Capture screenshot when a scenario fails |
| `screenshot.path` | `target/screenshots` | Where failure screenshots are saved |
| `retry.count` | `2` | How many times to retry a failed test |

### Runtime Overrides

Every property can be overridden with a `-D` system property at test execution time:

```bash
# Run in headed Chrome with screenshots on failure
mvn test -Dheadless=false -Dscreenshot.on.failure=true

# Run on Firefox
mvn test -Dbrowser=firefox

# Increase wait timeout for slower environments
mvn test -Dexplicit.wait.timeout=30
```

---

## Running Tests

### Run all tests (default tag `@auth` as configured in `TestRunner`)

```bash
mvn test
```

### Run a specific tag

Edit `TestRunner.java` to change the `tags` value, or override at runtime using the Surefire property:

```bash
# Run only @regression tagged scenarios
mvn test -Dcucumber.filter.tags="@regression"

# Run @sanity but not @slow
mvn test -Dcucumber.filter.tags="@sanity and not @slow"
```

### Run all features (remove tag filter)

Set `tags = ""` in `TestRunner.java` or pass an empty filter:

```bash
mvn test -Dcucumber.filter.tags=""
```

### Run a specific feature file

```bash
mvn test -Dcucumber.features="src/test/resources/features/login.feature"
```

---

## Test Coverage

| Module | Feature File | Tags | Scenarios |
|--------|-------------|------|-----------|
| Authentication | `login.feature` | `@auth @sanity @regression` | Valid login, invalid password, empty fields |
| Admin | `admin.feature` | `@admin @regression` | Search system user by username |
| PIM | `Pim.feature` | `@pim` | PIM module operations |
| Claims | `claim.feature` | `@claim` | Claim submission workflows |
| Buzz | `buzz.feature` | `@buzz` | Buzz feed interactions |
| Navigation | `navigation.feature` | — | Menu and page navigation |

---

## Reports

After a test run, Cucumber generates reports in `target/`:

| Report | Path | Format |
|--------|------|--------|
| HTML Report | `target/cucumber-reports/report.html` | Human-readable, browser-viewable |
| JSON Report | `target/cucumber.json` | Machine-readable, CI-integration friendly |
| Console | stdout | Pretty-printed step-by-step output |


---

## Design Patterns

| Pattern | Where Used | Why |
|---------|-----------|-----|
| **Page Object Model** | `pages/` package | Decouples element locators from test logic; a selector change only updates one class |
| **Action Layer** | `actions/` package | Encapsulates multi-step business workflows above raw element interactions |
| **ThreadLocal** | `DriverFactory` | Safe parallel execution without a shared global browser instance |
| **Singleton** | `ConfigReader` | Configuration loaded once, consistent across all classes in a test run |
| **Hooks** | `Hooks.java` | Centralised setup/teardown — no repeated browser init boilerplate in step classes |
| **Abstract Base Class** | `BasePage` | Common Selenium helpers (`type`, `click`, `waitForVisibility`) shared by all page objects |


---

> **Application Under Test:** [OrangeHRM Open Source Demo](https://opensource-demo.orangehrmlive.com/web/index.php/auth/login)
