package pages;

import bases.BasePage;
import org.openqa.selenium.By;

public class ClaimPage extends BasePage {

    private static final By ASSIGN_CLAIM_BTN= By.xpath("//div[@class='orangehrm-header-container']/button");
    private static final By EMP_NAME_INPUT = By.xpath("//input[@placeholder='Type for hints...']");
    private static final By EVENT_LIST_BTN = By.xpath("//label[contains(text(),'Event')]/parent::div/following-sibling::div/div[@class='oxd-select-wrapper']");
    private static final By CURRENCY_LIST_BTN = By.xpath("//label[contains(text(),'Currency')]/parent::div/following-sibling::div/div[@class='oxd-select-wrapper']");
    private static final By REMARKS_TEXTAREA = By.xpath("//label[contains(text(),'Remarks')]/parent::div/following-sibling::div/textarea");
    private static  final By CLAIM_CREAT_BTN= By.xpath("//button[@type='submit']");
    private static  final By SUCCESS_MESSAGE= By.xpath("//div[@class='oxd-toast-start']/div[2]/p[2]");

    public void clickAssignClaimButton() {
        click(ASSIGN_CLAIM_BTN);
    }
    public void enterEmployeeName(String employeeName) {
        type(EMP_NAME_INPUT, employeeName);
    }
    public void clickEventListButton() {
        click(EVENT_LIST_BTN);
    }
    public void selectOption(String event) {
        click(By.xpath("//div[@role='option' and normalize-space()='"+event+"']"));
    }
    public void clickCurrentListButton() {
        click(CURRENCY_LIST_BTN);
    }
    public void enterRemarks(String remarks) {
        type(REMARKS_TEXTAREA, remarks);
    }
    public void clickCreateClaimButton() {
        click(CLAIM_CREAT_BTN);
    }
    public boolean getSuccessMessage(String successMsg)
    {
        return getText(SUCCESS_MESSAGE,successMsg);
    }
}

