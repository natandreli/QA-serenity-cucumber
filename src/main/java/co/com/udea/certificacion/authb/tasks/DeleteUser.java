package co.com.udea.certificacion.authb.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteUser implements Task {
    private final String username;

    public DeleteUser(String username) {
        this.username = username;
    }

    public static DeleteUser withUsername(String username) {
        return instrumented(DeleteUser.class, username);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String authToken = actor.recall("authToken");

        actor.attemptsTo(
                Delete.from("/users/v1/" + username)
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + authToken)
                                .relaxedHTTPSValidation()
                        )
        );
    }
}
