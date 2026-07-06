package bases;

import config.ConfigReader;
import driver.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.Locator;

import java.util.List;

/*
 * Base class for page objects.
 *
 * Provides small, reusable wrappers around common element interactions to keep page objects concise.
 * Intentionally minimal: higher-level waiting/retry logic should live in a dedicated wait utility
 * to avoid hiding timing issues behind every action.
 */
public abstract class BasePage {

    // Protected by inheritance, but kept package-private to avoid leaking into test code directly.
    protected WebDriver driver;
    protected WebDriverWait wait;
    ConfigReader configReader = ConfigReader.getInstance();
    public BasePage() {
        // Pages are created after Hooks initializes the driver for the current scenario thread.
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, configReader.getWaitTimeout());
    }

    /*type, click, get Text with Implicit wait*/
    protected void type(By locator, String text) {
        waitForVisibility(locator).sendKeys(text);
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected boolean getText(By locator,String title) {
         return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator,title));
    }
    protected boolean getTextFromElement(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element,text));
    }

    //click with wait for element passed as argument
    protected void click(WebElement element) {
        waitForClickable(element).click();
    }

    // Waits until element is visible on the page.
    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    //Waits until element located by locator is visible.
    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //Waits until all elements located by locator is visible.
    protected List<WebElement> waitForVisibilityOfElements(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }


    //Waits until element is clickable.
    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    //Waits until element located by locator is clickable.
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

     //Waits until the URL contains expected text.
    protected boolean waitForUrlContains(String urlFragment) {
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }

}
