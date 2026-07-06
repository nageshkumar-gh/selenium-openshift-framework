# Runs this project's Selenium/Cucumber/TestNG suite headless, for use as an OpenShift Job.
# Scenario-level parallelism comes from testng.xml's data-provider-thread-count - no Selenium
# Grid/Hub/Node needed, since each thread launches its own headless Chrome inside this one
# container via DriverFactory's ThreadLocal driver.
FROM maven:3.9-eclipse-temurin-21

# Google Chrome isn't bundled with the base image; Selenium Manager only fetches the matching
# chromedriver automatically, not the browser itself, so it's installed explicitly here.
RUN apt-get update \
    && apt-get install -y wget gnupg \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace

# Copy just the POM first so dependency resolution is cached in its own layer.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY testng.xml .
COPY src ./src

# -Dbrowser=chrome/-Dheadless=true override config.properties regardless of its current values,
# since a container has no display server and this image only installs Chrome.
ENTRYPOINT ["mvn", "-B", "test", "-Dbrowser=chrome", "-Dheadless=true"]
