package co.com.udea.certificacion.authb.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import java.util.Map;

public class Login implements Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(Login.class);
    private final Map<String, String> credentials;
    private String authToken;

    public Login(Map<String, String> credentials) {
        this.credentials = credentials;
    }

    public static Login withCredentials(Map<String, String> credentials) {
        return instrumented(Login.class, credentials);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/users/v1/login")
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .body(credentials)
                                .relaxedHTTPSValidation()
                        )
        );

        authToken = SerenityRest.lastResponse().jsonPath().getString("auth_token");
        LOGGER.info("User Auth Token: {}", authToken);

        // Guardar el token en el contexto de Serenity para uso posterior
        actor.remember("authToken", authToken);
    }
}
