package pages;

import bases.BasePage;
import org.openqa.selenium.By;

public class TopbarPage extends BasePage {

    private static final By TOP_HEADER_TITLE=By.xpath("//span[@class='oxd-topbar-header-breadcrumb']");

    public boolean getTopbarTitle(String title) {
        return getText(TOP_HEADER_TITLE, title);
    }


}
