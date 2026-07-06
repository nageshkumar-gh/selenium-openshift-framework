package pages;

import bases.BasePage;
import org.openqa.selenium.By;

/*
 * Page object for the OrangeHRM PIM module.
 *
 * Exposes user-level actions for managing employees and keeps selectors private.
 * This reduces coupling between tests and locator details.
 */
public class PIMPage extends BasePage {

    // Locators for PIM module - keep as constants
    private static final By PIM_MENU = By.xpath("//a[@href=\"#/pim\"]");
    private static final By ADD_EMPLOYEE_BTN = By.xpath("//button[contains(text(), 'Add Employee')]");
    private static final By FIRST_NAME_INPUT = By.name("firstName");
    private static final By MIDDLE_NAME_INPUT = By.name("middleName");
    private static final By LAST_NAME_INPUT = By.name("lastName");
    private static final By EMPLOYEE_ID_INPUT = By.name("employeeId");
    private static final By SAVE_BTN = By.xpath("//button[@type='submit' and contains(text(), 'Save')]");
    private static final By SUCCESS_MESSAGE = By.xpath("//div[contains(@class, 'oxd-toast--success')]");
    private static final By VALIDATION_ERROR = By.xpath("//span[contains(@class, 'oxd-input-field-error')]");
    private static final By EMPLOYEE_LIST = By.xpath("//div[@class='oxd-table-body']");
    private static final By EMPLOYEE_ID_DISPLAY = By.xpath("//label[contains(text(), 'Employee Id')]/following-sibling::div");
    private static final By PIM_HEADER = By.xpath("//h6[contains(text(), 'Employee Information')]");
    private static final By SEARCH_INPUT = By.xpath("//input[@placeholder='Search']");
    private static final By SEARCH_BTN = By.xpath("//button[contains(text(), 'Search')]");
    private static final By SEARCH_RESULTS = By.xpath("//div[@class='oxd-table-body']/div");
    private static final By EMPLOYEE_CARD = By.xpath("//div[@class='oxd-table-body']/div[@class='oxd-table-card']");


    /**
     * Verify PIM dashboard is displayed
     */
    public void verifyPIMDashboard() {
        waitForVisibility(PIM_HEADER);
    }

    /**
     * Click Add Employee button
     */
    public void clickAddEmployeeButton() {
        click(ADD_EMPLOYEE_BTN);
    }

    /**
     * Enter first name
     */
    public void enterFirstName(String firstName) {
        type(FIRST_NAME_INPUT, firstName);
    }

    /**
     * Enter middle name
     */
    public void enterMiddleName(String middleName) {
        type(MIDDLE_NAME_INPUT, middleName);
    }

    /**
     * Enter last name
     */
    public void enterLastName(String lastName) {
        type(LAST_NAME_INPUT, lastName);
    }

    /**
     * Enter employee ID
     */
    public void enterEmployeeID(String employeeID) {
        type(EMPLOYEE_ID_INPUT, employeeID);
    }

    /**
     * Click Save button
     */
    public void clickSaveButton() {
        click(SAVE_BTN);
    }

    /**
     * Verify employee added successfully
     */
    public void verifyEmployeeAddedSuccessfully() {
        waitForVisibility(SUCCESS_MESSAGE);
    }


    /**
     * Verify validation error message is displayed
     */
    public void verifyValidationErrorMessage() {
        waitForVisibility(VALIDATION_ERROR);
    }

    /**
     * Verify employee list is visible
     */
    public void verifyEmployeeListVisible() {
        waitForVisibility(EMPLOYEE_LIST);
    }

    /**
     * Verify employee list is not empty
     */
    public void verifyEmployeeListNotEmpty() {
        java.util.List<org.openqa.selenium.WebElement> employees = waitForVisibilityOfElements(EMPLOYEE_CARD);
        assert !employees.isEmpty() : "Employee list is empty";
    }

    /**
     * Enter search employee name
     */
    public void enterSearchEmployeeName(String employeeName) {
        type(SEARCH_INPUT, employeeName);
    }

    /**
     * Click Search button
     */
    public void clickSearchButton() {
        click(SEARCH_BTN);
    }

    /**
     * Verify search results
     */
    public void verifySearchResults() {
        java.util.List<org.openqa.selenium.WebElement> results = waitForVisibilityOfElements(SEARCH_RESULTS);
        assert !results.isEmpty() : "Search returned no results";
    }

    /**
     * Verify employee record is editable
     */
    public void verifyEmployeeRecordEditable() {
        waitForVisibility(FIRST_NAME_INPUT);
    }
}

