package co.com.udea.certificacion.authb.stepdefinitions;

import co.com.udea.certificacion.authb.tasks.ConnectTo;
import co.com.udea.certificacion.authb.tasks.DeleteUser;
import co.com.udea.certificacion.authb.tasks.LoginAsAdmin;
import co.com.udea.certificacion.authb.tasks.PostTo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
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
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;
import java.util.Map;

public class RegisterStepDefinition {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterStepDefinition.class);
    private Actor user;
    private String registeredUsername;

    @Before
    public void config() {
        OnStage.setTheStage(new OnlineCast());
        user = OnStage.theActorCalled("User");
    }

    @Given("I am connected to capacities of the service")
    public void iAmConnectedToCapacitiesOfTheService() {
        user.attemptsTo(ConnectTo.theService());
    }

    @When("I enter the correct user data:")
    public void iEnterTheCorrectUserData(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> userData = data.get(0);
        registeredUsername = userData.get("username");

        user.attemptsTo(
                PostTo.service("/users/v1/register", userData)
        );
    }

    @Then("I can see a 200 status code with successful registration message")
    public void iCanSeeA200StatusCodeWithSuccessfulRegistrationMessage() {
        user.should(
                seeThatResponse("Registration response verification",
                        response -> response
                                .statusCode(200)
                                .body("status", equalTo("success"))
                )
        );

    }

    @SuppressWarnings("unused")
    @After
    public void cleanUp() {
        if (registeredUsername != null) {
            user.attemptsTo(
                    LoginAsAdmin.withCredentials(Map.of(
                            "username", "admin1",
                            "password", "admin1"
                    )),
                    DeleteUser.withUsername("usuarioprueba1")
            );
            LOGGER.info("User {} deleted after test.", registeredUsername);
        }
    }

}