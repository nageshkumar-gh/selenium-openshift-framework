package steps;

import actions.LoginAction;
import actions.TopbarAction;
import io.cucumber.java.en.*;
import org.testng.Assert;

/*
 * Step definitions for login feature scenarios.
 *
 * Guideline:
 * - Keep assertions and scenario intent in steps
 * - Keep UI mechanics in the action/page layers
 */
public class LoginStep {

    // Stateless action object; constructed once per step-definition instance.
    private final LoginAction loginAction=new LoginAction();
    private final TopbarAction  topbarAction=new TopbarAction();

    @Given("the user has logged in successfully navigating to the {string}")
    public void the_user_has_logged_in_successfully(String pageTitle) {
        Assert.assertTrue(topbarAction.isNavigatedToModule(pageTitle), "Dashboard");
    }

    @Given("the user is on the OrangeHRM login page")
    public void the_user_is_on_the_orange_hrm_login_page(){
        Assert.assertTrue(loginAction.verifyLoginPage("auth/login"));
    }

    /*@When("the user logs in with username {string} and password {string}")
    public void the_user_logs_in_with_username_and_password(String username, String password ) {
        loginAction.login(username, password);
    }*/

    @When("the user logs in with correct credentials")
    public void the_user_logs_in_with_correct_credentials( ) {
        loginAction.validLogin();
    }

    @When("the user logs in with wrong credentials")
    public void the_user_logs_in_with_wrong_credentials( ){
        loginAction.inValidLogin();
    }

    @When("the user logs in with username {string} and password {string}")
    public void the_user_logs_in_with_username_and_password(String user, String pass  ) {
        loginAction.login(user, pass);
    }

    @Then("the user should be logged in successfully navigating to the {string}")
    public void the_user_should_be_logged_in_successfully(String pageTitle) {
        Assert.assertTrue(topbarAction.isNavigatedToModule(pageTitle),"Dashboard");
    }

    @Then("an authentication error {string} message should be displayed")
    public void an_authentication_error_message_should_be_displayed(String message) {
        // These are stable application messages; if AUT text changes, update expected values here.
        Assert.assertTrue(loginAction.getInvalidLoginMsg(message), "Expected: Invalid credentials");
    }

    @Then("a {string} field validation message should be displayed")
    public void a_required_field_validation_message_should_be_displayed(String requiredFieldValidationMessage) {
        Assert.assertTrue(loginAction.getMissingCredMsg(requiredFieldValidationMessage), "Expected: Required");
    }


}
