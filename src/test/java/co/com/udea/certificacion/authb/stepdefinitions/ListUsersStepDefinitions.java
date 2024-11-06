package co.com.udea.certificacion.authb.stepdefinitions;

import co.com.udea.certificacion.authb.tasks.ConnectTo;
import co.com.udea.certificacion.authb.tasks.GetUserList;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;


import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ListUsersStepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListUsersStepDefinitions.class);
    private Actor user;

    @Before
    public void setUp() {
        OnStage.setTheStage(new OnlineCast());
        user = OnStage.theActorCalled("User");
    }

    @Given("I'm connected to the service")
    public void iAmConnectedToCapacitiesOfTheService() {
        user.attemptsTo(ConnectTo.theService());
    }

    @When("I request the list of users")
    public void iRequestTheListOfUsers() {
        user.attemptsTo(GetUserList.fromService());
        LOGGER.info("Requested list of users");
    }

    @Then("I should receive a {int} status code with the list of users")
    public void iShouldReceiveStatusCodeWithListOfUsers(int statusCode) {
        user.should(
                seeThatResponse("The user should see a successful response with status code",
                        response -> response.statusCode(statusCode)
                                .body("users", notNullValue())
                )
        );
        LOGGER.info("Received {} status code with list of users", statusCode);
    }
    @And("the list should contain the following users:")
    public void theListShouldContainTheFollowingUsers(DataTable dataTable) {
        List<Map<String, String>> expectedUsers = dataTable.asMaps(String.class, String.class);

        List<String> expectedEmails = expectedUsers.stream()
                .map(user -> user.get("email"))
                .collect(Collectors.toList());

        List<String> expectedUsernames = expectedUsers.stream()
                .map(user -> user.get("username"))
                .collect(Collectors.toList());

        user.should(
                seeThatResponse("Verify the list contains expected users",
                        response -> response.body("users.email", hasItems(expectedEmails.toArray()))
                                .body("users.username", hasItems(expectedUsernames.toArray()))
                )
        );

        LOGGER.info("Verified users in the list");
    }
}
