package actions;

import driver.DriverFactory;
import pages.MenuItemPage;
import pages.PIMPage;

/*
 * Action layer for PIM operations.
 *
 * Keeps step definitions focused on business intent by placing UI interaction sequences here.
 * Provides higher-level operations for managing employee information.
 */
public class PIMAction extends MenuItemPage {

    private final PIMPage pimPage;

    public PIMAction() {
        pimPage = new PIMPage();
    }

    /**
     * Add a new employee with first, middle and last name
     */
    public void addEmployeeWithFullDetails(String firstName, String middleName, String lastName) {

        pimPage.clickAddEmployeeButton();
        pimPage.enterFirstName(firstName);
        pimPage.enterMiddleName(middleName);
        pimPage.enterLastName(lastName);
        pimPage.clickSaveButton();
    }

    /**
     * Add a new employee with first and last name only
     */
    public void addEmployeeWithMinimalDetails(String firstName, String lastName) {

        pimPage.clickAddEmployeeButton();
        pimPage.enterFirstName(firstName);
        pimPage.enterLastName(lastName);
        pimPage.clickSaveButton();
    }

    /**
     * Add a new employee with employee ID
     */
    public void addEmployeeWithID(String firstName, String lastName, String employeeID) {

        pimPage.clickAddEmployeeButton();
        pimPage.enterFirstName(firstName);
        pimPage.enterLastName(lastName);
        pimPage.enterEmployeeID(employeeID);
        pimPage.clickSaveButton();
    }

    /**
     * Search for an employee by name
     */
    public void searchEmployee(String employeeName) {

        pimPage.enterSearchEmployeeName(employeeName);
        pimPage.clickSearchButton();
    }

    /**
     * Navigate to PIM module
     */
    public void navigateToPIMModule() {

    }

    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return DriverFactory.getDriver().getCurrentUrl();
    }
}

