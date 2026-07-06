package utils;

import config.ConfigReader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private static final DateTimeFormatter TIMESTAMP_PATTERN = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");

    private ScreenshotUtil() {
    }

    /*
     * Captures a PNG screenshot from the given driver, saves it under
     * <screenshot.path>/<browser>/<scenarioName>/<label>_<timestamp>.png, and returns the raw
     * bytes so callers can also attach them to the Cucumber report. Namespacing by browser keeps
     * cross-browser runs (same scenario name, different browser) from mixing screenshots together.
     */
    public static byte[] capture(WebDriver driver, String scenarioName, String label) {
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        saveToDisk(bytes, scenarioName, label);
        return bytes;
    }

    private static void saveToDisk(byte[] bytes, String scenarioName, String label) {
        try {
            String browser = ConfigReader.getInstance().getBrowser();
            Path scenarioDir = Path.of(ConfigReader.getInstance().getScreenshotPath(), sanitize(browser), sanitize(scenarioName));
            Files.createDirectories(scenarioDir);
            String fileName = sanitize(label) + "_" + TIMESTAMP_PATTERN.format(LocalDateTime.now()) + ".png";
            Files.write(scenarioDir.resolve(fileName), bytes);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save screenshot for scenario: " + scenarioName, e);
        }
    }

    private static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
