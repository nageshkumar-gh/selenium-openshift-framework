# Selenium OpenShift Framework

![CI](https://github.com/nageshkumar-gh/selenium-openshift-framework/actions/workflows/ci.yml/badge.svg?style=flat-square)
![OpenShift Pipeline](https://github.com/nageshkumar-gh/selenium-openshift-framework/actions/workflows/openshift-deploy.yml/badge.svg?style=flat-square)

![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.43.0-43B02A?style=flat-square&logo=selenium&logoColor=white)
![Cucumber](https://img.shields.io/badge/Cucumber-7.34.3-23D96C?style=flat-square&logo=cucumber&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.x-FF6C37?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=flat-square&logo=docker&logoColor=white)
![OpenShift](https://img.shields.io/badge/OpenShift-Deployed-EE0000?style=flat-square&logo=redhatopenshift&logoColor=white)
![Platform](https://img.shields.io/badge/Browsers-Chrome%20%7C%20Firefox%20%7C%20Edge%20%7C%20Safari-yellow?style=flat-square)

A **BDD test automation framework** (Selenium WebDriver + Cucumber + TestNG) for the [OrangeHRM](https://opensource-demo.orangehrmlive.com) demo application, containerized and deployed to **OpenShift** as a parallel, one-shot test-execution Job, driven end-to-end by a **GitHub Actions CI/CD pipeline**.

This repo started as a fork of [`bdd-selenium-framework`](https://github.com/nageshkumar-gh/bdd-selenium-framework) — the framework itself is unchanged; everything added here is the containerization and OpenShift deployment layer.

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
- [Running Tests Locally](#running-tests-locally)
- [Running on OpenShift](#running-on-openshift)
- [CI/CD Pipeline](#cicd-pipeline)
- [Test Coverage](#test-coverage)
- [Reports](#reports)
- [Design Patterns](#design-patterns)

---

## Overview

The framework automates end-to-end functional tests using a layered architecture: **Feature files → Step Definitions → Action classes → Page Objects**. Tests are written in plain English (Gherkin), executable via Cucumber + TestNG.

On top of that, this repo adds a full **containerized OpenShift execution path**:
- A `Dockerfile` bundles the suite with a headless Chrome browser
- An OpenShift `BuildConfig` builds that image directly from this GitHub repo (no local Docker needed)
- A one-shot OpenShift `Job` runs the suite, with **N scenarios executing in parallel inside a single pod** via TestNG's data-provider thread pool
- A GitHub Actions workflow automates the whole build → deploy → run → report loop on every push

---

## Features

- **BDD with Cucumber** — Gherkin feature files as the single source of truth for test scenarios
- **Page Object Model (POM)** — encapsulates element locators and interactions per page
- **Action Layer** — separates business-level operations from raw element interactions
- **ThreadLocal WebDriver** — safe for parallel test execution; no shared browser state between scenarios
- **Cross-browser** — Chrome, Firefox, Edge, and Safari all supported via `DriverFactory`
- **Parallel execution** — TestNG's `data-provider-thread-count` runs multiple scenarios concurrently, each with its own isolated browser session
- **Retry on failure** — a custom `IRetryAnalyzer` (`RetryAnalyzer` + `AnnotationTransformer`) retries failed scenarios up to `retry.count` times, keyed per-scenario so parallel runs don't share retry counters
- **Screenshots** — on failure and/or after every step, saved per-browser/per-scenario under `target/screenshots/`
- **Containerized & OpenShift-deployable** — runs identically on a laptop or as a Kubernetes/OpenShift Job
- **Runtime-tunable parallelism** — thread count is set via a `THREAD_COUNT` environment variable at container start, so tuning it never requires an image rebuild
- **Automated CI/CD** — GitHub Actions builds the image on OpenShift and runs the suite as a Job on every push

---

## Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 21 | Primary language |
| Selenium WebDriver | 4.43.0 | Browser automation |
| Cucumber Java | 7.34.3 | BDD framework |
| Cucumber TestNG | 7.34.3 | TestNG integration for Cucumber |
| TestNG | 7.x | Test execution engine |
| Maven | 3.x | Build & dependency management |
| Maven Surefire Plugin | 3.5.5 | Runs TestNG suite via `testng.xml` |
| Docker | — | Containerizes the suite + Chrome for OpenShift |
| OpenShift (Red Hat) | 4.21 (Developer Sandbox / ROSA) | Runs the suite as a `Job`, builds the image via `BuildConfig` |
| GitHub Actions | — | CI/CD orchestration (`redhat-actions/oc-login`, `oc-installer`) |

---

## Project Structure

```
selenium-openshift-framework/
├── Dockerfile                           # Builds the test image: JDK + Maven + Chrome
├── entrypoint.sh                        # Patches THREAD_COUNT into testng.xml, then runs mvn test
├── pom.xml                              # Maven build descriptor
├── testng.xml                           # TestNG suite configuration (data-provider-thread-count)
├── openshift/
│   └── job.yaml                         # OpenShift Job manifest (image, env, resource limits)
├── .github/workflows/
│   ├── ci.yml                           # Plain local-runner CI (mvn test on Ubuntu)
│   └── openshift-deploy.yml             # Build + run on OpenShift, stream logs, upload reports
└── src/
    └── test/
        ├── java/
        │   ├── bases/BasePage.java          # Abstract base with reusable Selenium helpers
        │   ├── config/ConfigReader.java     # Singleton config loader (config.properties)
        │   ├── driver/DriverFactory.java    # ThreadLocal WebDriver; Chrome/Firefox/Edge/Safari
        │   ├── hooks/Hooks.java             # @Before/@After hooks: driver lifecycle, screenshots
        │   ├── listeners/
        │   │   ├── RetryAnalyzer.java       # Per-scenario keyed retry (safe under parallel exec)
        │   │   └── AnnotationTransformer.java  # Wires RetryAnalyzer onto Cucumber's runScenario
        │   ├── utils/ScreenshotUtil.java    # Captures + saves screenshots per browser/scenario
        │   ├── pages/                       # Page Object classes (one per screen/module)
        │   ├── actions/                     # Business-level operations above raw page objects
        │   ├── steps/                       # Gherkin step bindings
        │   └── runner/TestRunner.java       # Cucumber + TestNG runner; reads browser/headless params
        └── resources/
            ├── config/config.properties     # All runtime configuration
            └── features/                    # Gherkin feature files
```

---

## Architecture

### Framework layers

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

### OpenShift execution model

```
GitHub push
     │
     ▼
OpenShift BuildConfig  ──►  clones this repo, runs Dockerfile  ──►  ImageStream (selenium-tests:latest)
     │
     ▼
OpenShift Job (openshift/job.yaml)
     │
     ▼
One pod, one container
     │
     ├── entrypoint.sh reads THREAD_COUNT, patches testng.xml
     └── mvn test
              │
              ▼
     TestNG data-provider thread pool (N threads)
              │
        ┌─────┼─────┬─────┐
        ▼     ▼     ▼     ▼
     Chrome Chrome Chrome Chrome   ← each thread launches its own headless Chrome
     (scenario 1) (scenario 2) (scenario 3) (scenario N)
```

Scenario-level parallelism happens **inside one pod** via TestNG's thread pool — there's no Selenium Grid/Hub/Node split. Each thread gets its own isolated `WebDriver` instance through `DriverFactory`'s `ThreadLocal`, exactly as it does when running locally.

### Key Design Decisions

**ThreadLocal WebDriver** — `DriverFactory` stores each `WebDriver` in a `ThreadLocal`, so parallel scenario execution never shares a browser instance.

**Runtime-tunable thread count** — `testng.xml`'s `data-provider-thread-count` would normally require an image rebuild to change. `entrypoint.sh` instead reads a `THREAD_COUNT` environment variable and patches it into `testng.xml` at container startup, so retuning parallelism only needs a new `Job` (delete + reapply), not a rebuild.

**OpenShift SCC-safe container** — the default `restricted-v2` SecurityContextConstraint runs containers as an arbitrary non-root UID with no `/etc/passwd` entry. The `Dockerfile` sets `HOME=/workspace` and `chgrp -R 0 + chmod -R g=u` on it (any UID OpenShift assigns always belongs to group `0`), so Maven can create `.m2` at runtime. `DriverFactory` also passes `--no-sandbox --disable-dev-shm-usage` to headless Chrome, since Chrome's own internal sandbox needs privileges a restricted-SCC container doesn't have.

**Per-scenario keyed retry** — `RetryAnalyzer` keys its attempt counter by method + scenario parameters instead of an instance field, because TestNG can share one `IRetryAnalyzer` instance across all rows of a parallel `@DataProvider` — an instance counter would count retries cumulatively across scenarios instead of per-scenario.

---

## Prerequisites

**For local runs:**

| Requirement | Minimum Version |
|-------------|----------------|
| JDK | 21 |
| Maven | 3.8+ |
| Google Chrome / Firefox / Edge | Latest stable |
| Safari | macOS only; requires `safaridriver --enable` and "Allow Remote Automation" enabled once in Safari's Develop menu |

> Selenium 4.x manages browser drivers automatically via Selenium Manager — no manual driver download needed.

**For OpenShift deployment:**

| Requirement | Notes |
|-------------|-------|
| OpenShift cluster access | e.g. [Red Hat Developer Sandbox](https://developers.redhat.com/developer-sandbox) (free) |
| `oc` CLI | For manual build/Job management |
| GitHub repo secrets | `OPENSHIFT_SERVER`, `OPENSHIFT_TOKEN`, `OPENSHIFT_NAMESPACE` — see [CI/CD Pipeline](#cicd-pipeline) |

---

## Setup & Installation

```bash
# 1. Clone the repository
git clone https://github.com/nageshkumar-gh/selenium-openshift-framework.git
cd selenium-openshift-framework

# 2. Verify Java and Maven are available
java -version
mvn -version

# 3. Install dependencies
mvn dependency:resolve
```

---

## Configuration

All settings live in [`src/test/resources/config/config.properties`](src/test/resources/config/config.properties):

| Property | Default | Description |
|----------|---------|-------------|
| `base.url` | OrangeHRM demo login URL | Entry URL opened before each scenario |
| `valid.username` / `valid.password` | `Admin` / `admin123` | Valid login credentials |
| `invalid.username` / `invalid.password` | `wrong` / `wrong` | Invalid login credentials |
| `browser` | `firefox` | `chrome`, `firefox`, `edge`, or `safari` |
| `headless` | `true` | Run browser in headless mode (not supported for Safari) |
| `wait.timeout` | `15` | Explicit wait timeout in seconds |
| `explicit.wait.timeout` | `20` | Element visibility wait timeout in seconds |
| `screenshot.on.failure` | `true` | Capture screenshot when a scenario fails |
| `screenshot.on.every.step` | `false` | Capture a screenshot after every step (independent of the above) |
| `screenshot.path` | `target/screenshots` | Root folder for screenshots (organized as `<browser>/<scenario>/<label>.png`) |
| `retry.count` | `1` | How many times to retry a failed scenario |

### Runtime Overrides

Every property can be overridden with a `-D` system property:

```bash
mvn test -Dbrowser=chrome -Dheadless=false -Dscreenshot.on.failure=true
mvn test -Dbrowser=edge
mvn test -Dretry.count=2
```

---

## Running Tests Locally

```bash
# Run whichever browser block is active in testng.xml (Chrome by default)
mvn test

# Run a specific tag
mvn test -Dcucumber.filter.tags="@regression"

# Cross-browser: uncomment the Firefox/Edge/Safari blocks in testng.xml, then
mvn test -DsuiteFile=testng.xml
```

`testng.xml` runs one browser by default (Chrome). To also run Firefox/Edge/Safari, uncomment their `<test>` blocks in `testng.xml` — they run **sequentially** (browser selection is a JVM-wide system property set per block, so concurrent blocks would race on it), with Safari last since it forces `headless=false`.

---

## Running on OpenShift

### One-time setup

```bash
# Build the image from this GitHub repo (Docker build strategy, since a Dockerfile-based build
# needs OS-level packages - Chrome - that a Java S2I builder image doesn't support)
oc new-build --name=selenium-tests --strategy=docker https://github.com/nageshkumar-gh/selenium-openshift-framework.git
```

### Every time you want to run the suite

```bash
# Rebuild only if the code changed
oc start-build selenium-tests --follow

# Job's pod template is immutable, so always delete before reapplying
oc delete job selenium-test-run --ignore-not-found=true
oc apply -f openshift/job.yaml

# Watch it run
oc logs -f job/selenium-test-run
```

### Tuning parallelism without a rebuild

`openshift/job.yaml` sets `THREAD_COUNT` as an environment variable — `entrypoint.sh` reads it and patches `testng.xml` at container startup. To change concurrency, just edit that one value and reapply — **no image rebuild needed**:

```yaml
env:
  - name: THREAD_COUNT
    value: "8"
```

### Sizing memory/CPU for parallelism

Each thread launches its own headless Chrome instance, so the Job's resource limits must scale with `THREAD_COUNT`. Check your project's actual quota first:

```bash
oc describe quota
```

`openshift/job.yaml` currently requests `1 CPU` / `2Gi`, limited to `3 CPU` / `6Gi` — sized for `THREAD_COUNT=8`. Roughly budget ~500Mi per concurrent Chrome instance plus JVM/Maven overhead.

---

## CI/CD Pipeline

[`.github/workflows/openshift-deploy.yml`](.github/workflows/openshift-deploy.yml) automates the entire loop above on every push to `main` (or manually via the **Actions** tab's "Run workflow" button — it's also `workflow_dispatch`-enabled):

1. Installs the `oc` CLI (`redhat-actions/oc-installer`)
2. Logs into OpenShift (`redhat-actions/oc-login`)
3. Triggers `oc start-build` (pulls and rebuilds from the pushed commit)
4. Deletes the previous Job, applies `openshift/job.yaml`
5. Streams live test output into the workflow log (retrying until the container is actually running, not just scheduled)
6. Copies `target/` (Cucumber/Surefire reports, screenshots) out of the pod and uploads it as a downloadable **workflow artifact**
7. Polls the Job's `Complete`/`Failed` condition directly and writes a clear / summary to the run's Summary page

### Required GitHub secrets

| Secret | Value |
|--------|-------|
| `OPENSHIFT_SERVER` | External API URL, e.g. `https://api.<cluster>.openshiftapps.com:6443` — get via console → username → "Copy login command" → "Display Token" (**not** `oc whoami --show-server` from an in-cluster terminal, which returns an internal-only address) |
| `OPENSHIFT_TOKEN` | A token from that same "Display Token" page |
| `OPENSHIFT_NAMESPACE` | Your OpenShift project name |

Set them via:
```bash
gh secret set OPENSHIFT_SERVER --repo nageshkumar-gh/selenium-openshift-framework
gh secret set OPENSHIFT_TOKEN --repo nageshkumar-gh/selenium-openshift-framework
gh secret set OPENSHIFT_NAMESPACE --repo nageshkumar-gh/selenium-openshift-framework
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

| Report | Path | Format |
|--------|------|--------|
| HTML Report | `target/cucumber-reports/report.html` | Human-readable, browser-viewable |
| JSON Report | `target/cucumber.json` | Machine-readable, CI-integration friendly |
| Screenshots | `target/screenshots/<browser>/<scenario>/<label>.png` | Per-scenario, per-browser, on failure and/or every step |


---

## Design Patterns

| Pattern | Where Used | Why |
|---------|-----------|-----|
| **Page Object Model** | `pages/` package | Decouples element locators from test logic |
| **Action Layer** | `actions/` package | Encapsulates multi-step business workflows above raw element interactions |
| **ThreadLocal** | `DriverFactory` | Safe parallel execution without a shared global browser instance |
| **Singleton** | `ConfigReader` | Configuration loaded once, consistent across a test run |
| **Hooks** | `Hooks.java` | Centralised setup/teardown, screenshot capture |
| **Abstract Base Class** | `BasePage` | Common Selenium helpers shared by all page objects |
| **Strategy-like retry** | `RetryAnalyzer` + `AnnotationTransformer` | Retry logic attached dynamically to Cucumber's generated test method |

---

> **Application Under Test:** [OrangeHRM Open Source Demo](https://opensource-demo.orangehrmlive.com/web/index.php/auth/login)
