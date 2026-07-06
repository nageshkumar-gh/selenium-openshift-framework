package config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

/*
 * Central test configuration loader.
 *
 * Loads properties from the classpath (config/config.properties) so runs are independent of the
 * working directory. Supports CI/local overrides with this precedence:
 * 1) System properties (-Dkey=value)
 * 2) Environment variables (KEY or KEY_WITH_UNDERSCORES)
 * 3) Properties file values
 *
 * This class is intentionally not tied to WebDriver so it can be used across hooks, drivers,
 * and any future utilities.
 */
public final class ConfigReader {
    private static final String DEFAULT_RESOURCE_PATH = "config/config.properties";

    // Lazy-initialized singleton; safe under concurrent access after construction.
    private static volatile ConfigReader instance;

    private final Properties properties;

    private ConfigReader(Properties properties) {
        this.properties = properties;
    }

    public static ConfigReader getInstance() {
        ConfigReader local = instance;
        if (local != null) {
            return local;
        }
        synchronized (ConfigReader.class) {
            if (instance == null) {
                instance = new ConfigReader(loadFromClasspath(DEFAULT_RESOURCE_PATH));
            }
            return instance;
        }
    }

    public String getBaseUrl() {
        // Normalize trailing slashes so URL composition remains predictable.
        return requireNonBlank("base.url").replaceAll("/+$", "");
    }

    public String getBrowser() {
        String browser = getString("browser", "chrome");
        return browser.toLowerCase(Locale.ROOT).trim();
    }

    public boolean isHeadless() {
        return getBoolean("headless", false);
    }

    public Duration getWaitTimeout() {
        /*
         * wait.timeout is expressed in seconds to keep configuration simple and readable.
         * Any non-positive value is treated as invalid because it would make waits meaningless.
         */
        int seconds = getInt("wait.timeout", 15);
        if (seconds <= 0) {
            throw new IllegalArgumentException("wait.timeout must be > 0 (seconds).");
        }
        return Duration.ofSeconds(seconds);
    }

    private String requireNonBlank(String key) {
        String value = getString(key, null);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required config value: " + key);
        }
        return value.trim();
    }

    private String getString(String key, String defaultValue) {
        String fromSystem = System.getProperty(key);
        if (fromSystem != null && !fromSystem.trim().isEmpty()) {
            return fromSystem.trim();
        }

        // Convert dotted keys (base.url) to ENV style (BASE_URL).
        String envKey = key.toUpperCase(Locale.ROOT).replace('.', '_');
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.trim().isEmpty()) {
            return fromEnv.trim();
        }

        String fromFile = properties.getProperty(key);
        if (fromFile != null && !fromFile.trim().isEmpty()) {
            return fromFile.trim();
        }

        return defaultValue;
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        String raw = getString(key, null);
        if (raw == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(raw);
    }

    private int getInt(String key, int defaultValue) {
        String raw = getString(key, null);
        if (raw == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value for " + key + ": " + raw, e);
        }
    }

    public boolean isScreenshotOnFailure() {
        return getBoolean("screenshot.on.failure", true);
    }

    public boolean isScreenshotOnEveryStep() {
        return getBoolean("screenshot.on.every.step", false);
    }

    public Duration getExplicitWaitTimeout() {
        int seconds = getInt("explicit.wait.timeout", 20);
        if (seconds <= 0)
            throw new IllegalArgumentException("explicit.wait.timeout must be > 0 (seconds).");
        return Duration.ofSeconds(seconds);
    }

    public int getRetryCount() {
        int retries = getInt("retry.count", 0);
        if (retries < 0)
            throw new IllegalArgumentException("retry.count must be >= 0.");
        return retries;
    }

    public String getScreenshotPath() {
        return getString("screenshot.path", "target/screenshots");
    }

    public String getValidUsername() {
        return getString("valid.username",null);
    }

    public String getValidPassword() {
        return getString("valid.password",null);
    }
    public String getInvalidUsername() {
        return getString("invalid.username",null);
    }
    public String getInvalidPassword() {
        return getString("invalid.password",null);
    }

    private static Properties loadFromClasspath(String resourcePath) {
        Objects.requireNonNull(resourcePath, "resourcePath");

        Properties props = new Properties();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ConfigReader.class.getClassLoader();
        }

        try (InputStream is = cl.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalStateException("Config resource not found on classpath: " + resourcePath);
            }
            props.load(is);
            return props;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config resource: " + resourcePath, e);
        }
    }
}
