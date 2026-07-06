package steps;

import actions.MenuItemAction;
import actions.TopbarAction;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class NavigationStep {

    private final MenuItemAction menuItemAction = new MenuItemAction();
    private final TopbarAction topbarAction = new TopbarAction();

    @When("the user navigates to the {string} module")
    public void the_user_navigates_to_the_module(String moduleName) {
        menuItemAction.navigateToModule(moduleName);
    }

    @Then("the top bar should show the {string} page title")
    public void the_top_bar_should_show_the_page_title(String pageTitle) {
        Assert.assertTrue(topbarAction.isNavigatedToModule(pageTitle), "Expected top bar title to contain: " + pageTitle);

    }
}

