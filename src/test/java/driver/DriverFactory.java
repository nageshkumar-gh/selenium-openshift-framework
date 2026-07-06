package driver;

import config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

/*
 * WebDriver lifecycle manager.
 *
 * Key design choice: use ThreadLocal so parallel execution does not share a single driver instance.
 * Each scenario thread gets its own browser session; teardown must call quitDriver() to prevent leaks.
 *
 * Browser selection and headless behavior come from ConfigReader (browser/headless).
 */
public class DriverFactory {

    /*
     * Thread-confined WebDriver storage.
     * Always remove() in quitDriver() so one thread cannot accidentally reuse a previous scenario's driver.
     */
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void initDriver() {
        if (DRIVER.get() != null) {
            return;
        }

        ConfigReader config = ConfigReader.getInstance();
        String browser = config.getBrowser();
        boolean headless = config.isHeadless();

        WebDriver driver;
        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    // Prefer the new headless implementation when available (Selenium/Chrome modern default).
                    chromeOptions.addArguments("--headless=new");
                    // window().maximize() has no real screen to size against in headless mode and
                    // silently falls back to a small default, so fix the viewport explicitly instead.
                    chromeOptions.addArguments("--window-size=1920,1080");
                    // Chrome's own internal sandbox needs user-namespace privileges that a
                    // restricted-SCC container (running as an arbitrary non-root UID) doesn't have,
                    // so it fails to start unless sandboxing is disabled. /dev/shm is also tiny by
                    // default in containers, which crashes Chrome under load unless disabled.
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                }
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                    firefoxOptions.addArguments("--width=1920", "--height=1080");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }
                driver = new EdgeDriver(edgeOptions);
                break;
            case "safari":
                // Safari has no headless mode; also requires `safaridriver --enable` to have been
                // run once on the host and only works on macOS.
                if (headless) {
                    throw new IllegalArgumentException("Safari does not support headless mode.");
                }
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        if (!headless) {
            driver.manage().window().maximize();
        }
        DRIVER.set(driver);
    }

    public static boolean isDriverInitialized() {
        return DRIVER.get() != null;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            // Fail fast: callers should initialize in hooks before accessing pages/actions.
            throw new IllegalStateException("WebDriver is not initialized. Call DriverFactory.initDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            // Always clear ThreadLocal, even if quit() throws, to avoid memory/thread retention.
            DRIVER.remove();
        }
    }

}
