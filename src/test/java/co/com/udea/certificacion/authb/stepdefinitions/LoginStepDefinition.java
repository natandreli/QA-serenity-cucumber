package co.com.udea.certificacion.authb.stepdefinitions;

import co.com.udea.certificacion.authb.tasks.ConnectTo;
import co.com.udea.certificacion.authb.tasks.PostTo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;
import java.util.Map;
public class LoginStepDefinition {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterStepDefinition.class);
    private Actor user;

    @Before
    public void config() {
        OnStage.setTheStage(new OnlineCast());
        user = OnStage.theActorCalled("User");
    }

    @Given("I am connected to the service capabilities")
    public void iAmConnectedToCapacitiesOfTheService() {
        user.attemptsTo(ConnectTo.theService());
        LOGGER.info("Connected to service successfully");
    }

    @When("I submit the login data:")
    public void iSubmitTheLoginData(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> userData = data.get(0);
        String username = data.get(0).get("username");

        user.attemptsTo(
                PostTo.service("/users/v1/login", userData)
        );

        LOGGER.info("Login request sent for username: {}", username);
    }

    @Then("I should receive a {int} status code with successful login message")
    public void iShouldReceiveAStatusCodeWithSuccessfulLoginMessage(int statusCode) {
        user.should(
                seeThatResponse("The user should see a successful login status",
                        response -> response.statusCode(statusCode)
                                .body("message", equalTo("Successfully logged in."))
                                .body("status", equalTo("success"))
                )
        );

        LOGGER.info("Received {} status code with successful login message", statusCode);
    }

    @Then("I should receive a {int} status code with invalid credentials message")
    public void iShouldReceiveAStatusCodeWithInvalidCredentialsMessage(int statusCode) {
        user.should(
                seeThatResponse("The user should see an invalid credentials message",
                        response -> response.statusCode(statusCode)
                                .body("message", equalTo("Password is not correct for the given username."))
                                .body("status", equalTo("fail"))
                )
        );

        LOGGER.info("Received {} status code with invalid credentials message", statusCode);
    }

    @Then("I should receive a {int} status code with user not found message")
    public void iShouldReceiveAStatusCodeWithUserNotFoundMessage(int statusCode) {
        user.should(
                seeThatResponse("The user should see a user not found message",
                        response -> response.statusCode(statusCode)
                                .body("message", equalTo("Username does not exist"))
                                .body("status", equalTo("fail"))
                )
        );

        LOGGER.info("Received {} status code with user not found message", statusCode);
    }

    @Then("I should receive a {int} status code with missing password message")
    public void iShouldReceiveAStatusCodeWithMissingPasswordMessage(int statusCode) {
        user.should(
                seeThatResponse("The user should see a missing password error",
                        response -> response.statusCode(statusCode)
                                .body("detail", equalTo("None is not of type 'string' - 'password'"))
                                .body("status", equalTo(400))
                )
        );

        LOGGER.info("Received {} status code with missing password error", statusCode);
    }

    @Then("I should receive a {int} status code with missing username message")
    public void iShouldReceiveAStatusCodeWithMissingUsernameMessage(int statusCode) {
        user.should(
                seeThatResponse("The user should see a missing username error",
                        response -> response.statusCode(statusCode)
                                .body("detail", equalTo("None is not of type 'string' - 'username'"))
                                .body("status", equalTo(400))
                )
        );

        LOGGER.info("Received {} status code with missing username error", statusCode);
    }

    @Then("I should receive a {int} status code with empty username message")
    public void iShouldReceiveAStatusCodeWithEmptyUsernameMessage(int statusCode) {
        user.should(
                seeThatResponse("The user should see an empty username error",
                        response -> response.statusCode(statusCode)
                                .body("detail", equalTo("Username cannot be empty"))
                                .body("status", equalTo(400))
                )
        );

        LOGGER.info("Received {} status code with empty username error", statusCode);
    }

    @Then("I should receive a {int} status code with empty password message")
    public void iShouldReceiveAStatusCodeWithEmptyPasswordMessage(int statusCode) {
        user.should(
                seeThatResponse("The user should see an empty password error",
                        response -> response.statusCode(statusCode)
                                .body("detail", equalTo("Password cannot be empty"))
                                .body("status", equalTo(400))
                )
        );

        LOGGER.info("Received {} status code with empty password error", statusCode);
    }

    @Then("I should receive a {int} status code with empty credentials message")
    public void iShouldReceiveAStatusCodeWithEmptyCredentialsMessage(int statusCode) {
        user.should(
                seeThatResponse("The user should see an empty credentials error",
                        response -> response.statusCode(statusCode)
                                .body("detail", equalTo("Username and password cannot be empty"))
                                .body("status", equalTo(400))
                )
        );

        LOGGER.info("Received {} status code with empty credentials error", statusCode);
    }

}
