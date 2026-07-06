package hooks;

import actions.LoginAction;
import config.ConfigReader;
import driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import utils.ScreenshotUtil;

/*
 * Cucumber scenario lifecycle hooks.
 *
 * Keep setup/teardown consistent for every scenario:
 * - create a clean WebDriver session for the current thread
 * - apply the configured timeout
 * - navigate to the entry page for the feature under test
 */
public class Hooks {

    ConfigReader config = ConfigReader.getInstance();

    // Per-scenario step counter so every-step screenshot filenames stay ordered and unique;
    // ThreadLocal keeps it isolated across scenarios running in parallel.
    private static final ThreadLocal<Integer> STEP_COUNTER = ThreadLocal.withInitial(() -> 0);

    @Before(order = 1)
    public void launchBrowser() {
        STEP_COUNTER.set(0);
        DriverFactory.initDriver();
    }
    @Before(order = 2)
    public void openLoginPage() {
        DriverFactory.getDriver().get(config.getBaseUrl());
    }
    @Before(order = 3,value = "not @auth")
    public void login(){
        LoginAction loginAction=new LoginAction();
        loginAction.login(config.getValidUsername(), config.getValidPassword());
    }

    @AfterStep
    public void takeScreenshotAfterStep(Scenario scenario) {
        if (config.isScreenshotOnEveryStep()) {
            int stepNumber = STEP_COUNTER.get() + 1;
            STEP_COUNTER.set(stepNumber);
            WebDriver driver = DriverFactory.getDriver();
            String label = "step" + stepNumber;
            byte[] screenshot = ScreenshotUtil.capture(driver, scenario.getName(), label);
            scenario.attach(screenshot, "image/png", label);
        }
    }

    @After(order = 2)
    public void takeScreenshotOnFailure(Scenario scenario) {
        // Cucumber runs @After hooks in descending order, so order = 2 runs before the
        // order = 1 quitDriver hook below, while the session is still alive.
        // Guard against driver init itself having failed (e.g. Safari session rejected): there is
        // no live session to screenshot, and calling getDriver() here would mask the real failure
        // with a secondary "WebDriver is not initialized" exception.
        if (scenario.isFailed() && config.isScreenshotOnFailure() && DriverFactory.isDriverInitialized()) {
            WebDriver driver = DriverFactory.getDriver();
            byte[] screenshot = ScreenshotUtil.capture(driver, scenario.getName(), "FAILURE");
            scenario.attach(screenshot, "image/png", "FAILURE");
        }
    }

    @After(order = 1)
    public void after(){
        // Always quit via DriverFactory so ThreadLocal cleanup is guaranteed.
        DriverFactory.quitDriver();
        STEP_COUNTER.remove();
    }
}
