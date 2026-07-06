package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PIMStep {

	@When("the user clicks on Add Employee button")
	public void the_user_clicks_on_add_employee_button() {
	}

	@When("the user enters first name {string}")
	public void the_user_enters_first_name(String firstName) {
		String ignored = firstName;
	}

	@When("the user enters middle name {string}")
	public void the_user_enters_middle_name(String middleName) {
		String ignored = middleName;
	}

	@When("the user enters last name {string}")
	public void the_user_enters_last_name(String lastName) {
		String ignored = lastName;
	}

	@When("the user enters employee ID {string}")
	public void the_user_enters_employee_id(String employeeID) {
		String ignored = employeeID;
	}

	@When("the user saves the employee")
	public void the_user_saves_the_employee() {
	}

	@When("the user saves the employee without entering name")
	public void the_user_saves_the_employee_without_entering_name() {
	}

	@Then("the employee should be added successfully")
	public void the_employee_should_be_added_successfully() {
	}

	@Then("the employee ID should be generated")
	public void the_employee_id_should_be_generated() {
	}

	@Then("a validation error message should be displayed for required fields")
	public void a_validation_error_message_should_be_displayed_for_required_fields() {
	}

	@When("the user should see the employee list")
	public void the_user_should_see_the_employee_list() {
	}

	@Then("the employee list should contain at least one employee")
	public void the_employee_list_should_contain_at_least_one_employee() {
	}

	@When("the user enters employee name {string} in search field")
	public void the_user_enters_employee_name_in_search_field(String employeeName) {
		String ignored = employeeName;
	}

	@When("the user clicks search button")
	public void the_user_clicks_search_button() {
	}

	@Then("the search results should display matching employees")
	public void the_search_results_should_display_matching_employees() {
	}

	@Then("the employee record should be accessible for editing")
	public void the_employee_record_should_be_accessible_for_editing() {
	}
}
