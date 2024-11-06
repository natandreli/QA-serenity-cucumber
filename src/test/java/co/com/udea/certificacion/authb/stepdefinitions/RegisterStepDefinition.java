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

    @When("I enter the following correct user data for registration")
    public void iEnterTheFollowingCorrectUserDataForRegistration(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> userData = data.get(0);
        registeredUsername = userData.get("username");

        user.attemptsTo(
                PostTo.service("/users/v1/register", userData)
        );
    }

    @When("I enter the following user data for registration")
    public void iEnterTheFollowingUserDataForRegistration(DataTable dataTable) {
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

    @When("I attempt to register with the same user data again")
    public void iAttemptToRegisterWithTheSameUserDataAgain() {
        Map<String, String> duplicateUserData = Map.of(
                "email", "usuario2@correo.com",
                "password", "micontraseñaSEGURA!2035",
                "username", registeredUsername
        );

        user.attemptsTo(
                PostTo.service("/users/v1/register", duplicateUserData)
        );
    }

    @Then("I see a 409 status code with a message about username already taken")
    public void iSeeA409StatusCodeWithAMessageAboutUsernameAlreadyTaken() {
        user.should(
                seeThatResponse("Username already taken response",
                        response -> response
                                .statusCode(409)
                                .body("message", equalTo("Username already taken"))
                )
        );
    }

    @Then("I see a 400 status code with a message about weak password")
    public void iSeeA400StatusCodeWithAMessageAboutWeakPassword() {
        user.should(
                seeThatResponse("Weak password response",
                        response -> response
                                .statusCode(400)
                                .body("message", equalTo("Password does not meet security requirements"))
                )
        );
    }

    @When("I attempt to register with the same email again")
    public void iAttemptToRegisterWithTheSameEmailAgain() {
        Map<String, String> duplicateEmailData = Map.of(
                "email", "usuario1@correo.com",
                "password", "micontraseñaSEGURA!2035",
                "username", "usuarioprueba1"
        );

        user.attemptsTo(
                PostTo.service("/users/v1/register", duplicateEmailData)
        );
    }

    @Then("I see a 409 status code with a message about email already taken")
    public void iSeeA409StatusCodeWithAMessageAboutEmailAlreadyTaken() {
        user.should(
                seeThatResponse("Email already taken response",
                        response -> response
                                .statusCode(409)
                                .body("message", equalTo("Email already taken"))
                )
        );
    }

    @After
    public void cleanUp() {
        if (registeredUsername != null) {
            new Thread(() -> {
                    user.attemptsTo(
                            LoginAsAdmin.withCredentials(Map.of(
                                    "username", "admin1",
                                    "password", "admin1"
                            )),
                            DeleteUser.withUsername(registeredUsername)
                    );
                    LOGGER.info("User {} deleted after test.", registeredUsername);
            }).start();
        }
    }

}