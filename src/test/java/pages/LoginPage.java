package pages;

import bases.BasePage;
import org.openqa.selenium.By;

/*
 * Page object for the OrangeHRM login page.
 *
 * Exposes user-level actions and readable page state while keeping selectors private. This reduces
 * coupling between tests and locator details, making selector changes localized to the page object.
 */
public class LoginPage extends BasePage {

    // Keep locators as constants to avoid duplication across methods.
    private static final By USERNAME_INPUT = By.cssSelector("input[name='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("input[name='password']");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");

    private static final By AUTH_ERROR_MESSAGE = By.cssSelector("div.oxd-alert-content p.oxd-alert-content-text");

    private static final By REQUIRED_FIELD_VALIDATION_MESSAGE = By.cssSelector("span.oxd-input-field-error-message");



    public void enterUsername(String username) {type(USERNAME_INPUT, username);}
    public void enterPassword(String password) {
        type(PASSWORD_INPUT, password);
    }
    public void clickLoginButton() {
        click(LOGIN_BUTTON);
    }
    public boolean getAuthErrorMessage(String InvalidMessage) {return getText(AUTH_ERROR_MESSAGE,InvalidMessage);}
    public boolean getRequiredFieldValidationMessage(String  MissingMessage) {
        return getText(REQUIRED_FIELD_VALIDATION_MESSAGE,MissingMessage);
    }
    public boolean isDisplayed(String urlPage) {
        return waitForUrlContains(urlPage);
    }
}
