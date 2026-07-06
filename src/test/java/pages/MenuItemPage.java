package pages;

import bases.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MenuItemPage extends BasePage {

    private static final By MENU_ITEM = By.xpath("//li[@class='oxd-main-menu-item-wrapper']");

    public void selectMenuItem(String menuItemName) {
        List<WebElement> menuItems = waitForVisibilityOfElements(MENU_ITEM);
        for (WebElement item : menuItems) {
            if (item.getText().equalsIgnoreCase(menuItemName)) {
                click(item);
                return;
            }
        }
    }

}
