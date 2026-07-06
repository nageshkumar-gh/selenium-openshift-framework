package steps;

import actions.ClaimAction;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class ClaimStep {

    private final ClaimAction claimAction = new ClaimAction();

    @When("the user creates a new claim with employee name {string} event {string}, currency {string} and remarks {string}")
    public void the_user_creates_a_new_claim_with_event_and_remarks(String empName, String event, String currency, String remarks){
        claimAction.createNewClaim(empName, event, currency, remarks);
    }

    @Then("the claim should be submitted {string}")
    public void the_claim_should_be_submitted_successfully(String successMsg){
        Assert.assertTrue(claimAction.isClaimSubmittedSuccessfully(successMsg), "Expected claim submission success message not found.");

    }
}
