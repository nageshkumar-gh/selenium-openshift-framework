package pages;

import bases.BasePage;
import org.openqa.selenium.By;

public class AdminPage extends BasePage {

    private static final By SEARCH_USERNAME_FIELD = By.xpath("//label[contains(text(),'Username')]/parent::div/parent::div/div[2]/input");
    private static final By SEARCH_BUTTON = By.xpath("//button[@type='submit']");
    private static final By SEARCH_RESULT = By.xpath("//div[@class='oxd-table-card']//div[@role='cell'][2]");

    public void enterUsernameToSearch(String username) {
        type(SEARCH_USERNAME_FIELD, username);
    }
    public void clickAdminSearch() {
        click(SEARCH_BUTTON);
    }
    public boolean getResultFromTable(String username) {
        return getText(SEARCH_RESULT,username);
    }


}

