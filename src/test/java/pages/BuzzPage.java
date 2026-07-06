package pages;

import bases.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BuzzPage extends BasePage {

    private static final By BUZZ_POST_TEXTAREA = By.xpath("//textarea[@class='oxd-buzz-post-input']");
    private static final By BUZZ_POST_BTN= By.xpath("//button[@type='submit']");
    private static final By BUZZ_POSTED_TEXT = By.xpath("//div[@class='orangehrm-buzz-post-body']/div/p[1]");

    public void writePost(String message) {
        type(BUZZ_POST_TEXTAREA,message);
    }
    public void clickPost() {
        click(BUZZ_POST_BTN);
    }

    public boolean getPostText(String message) {
        List<WebElement> elementList=waitForVisibilityOfElements(BUZZ_POSTED_TEXT);
        for(WebElement element:elementList){
            String text=element.getText();
            System.out.println(text);
            if(text.equals(message)){
                return true;
            }
        }
        return false;
    }
}

