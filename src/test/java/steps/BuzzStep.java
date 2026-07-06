package steps;

import actions.BuzzAction;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class BuzzStep {

    private final BuzzAction buzzAction = new BuzzAction();

    @When("the user shares a post with the message {string}")
    public void the_user_shares_a_post_with_the_message(String message) {
        buzzAction.sharePost(message);
    }

    @Then("the post {string} should appear at the top of the Buzz feed")
    public void the_post_should_appear_at_the_top_of_the_buzz_feed(String message) {

        Assert.assertTrue(buzzAction.whatPosted(message), "Expected the shared post to appear at the top of the feed");
    }
}
