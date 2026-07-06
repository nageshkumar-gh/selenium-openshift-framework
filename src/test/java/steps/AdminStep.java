package steps;

import actions.AdminAction;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class AdminStep {

    private final AdminAction adminAction = new AdminAction();

    @When("the user searches for the system user with username {string}")
    public void the_user_searches_for_the_system_user_with_username(String username) {
        adminAction.searchUserByAdmin(username);
    }

    @Then("at least one user record should be displayed in the results")
    public void at_least_one_user_record_should_be_displayed_in_the_results() {
        Assert.assertTrue(adminAction.verifyAdminIsSearched("Admin"),"Admin is present");
    }
}
