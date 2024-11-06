package co.com.udea.certificacion.authb.stepdefinitions;

import co.com.udea.certificacion.authb.tasks.ConnectTo;
import co.com.udea.certificacion.authb.tasks.GetTo;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.greaterThan;
import java.util.Map;

public class BooksStepDefinitions {

    private Actor user;

    @Before
    public void config() {
        OnStage.setTheStage(new OnlineCast());
        user = OnStage.theActorCalled("User");
    }

    @Given("I am connected to the service")
    public void iAmConnectedToTheService() {
        user.attemptsTo(ConnectTo.theService());
    }

    @When("I request the list of books from the {string} endpoint")
    public void iRequestTheListOfBooksFromTheEndpoint(String endpoint) {
        user.attemptsTo(
                GetTo.service(endpoint, Map.of())
        );
    }

    @Then("I can see a 200 status code with a list of books")
    public void iCanSeeA200StatusCodeWithAListOfBooks() {
        user.should(
                seeThatResponse("Books list response verification",
                        response -> response
                                .statusCode(200)
                                .body("Books.size()", greaterThan(0))
                )
        );
    }

}
