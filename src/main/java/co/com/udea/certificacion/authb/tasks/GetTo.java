package co.com.udea.certificacion.authb.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import static net.serenitybdd.screenplay.Tasks.instrumented;

import java.util.Map;

public class GetTo implements Task {
    private final String endpoint;
    private final Map<String, String> queryParams;

    public GetTo(String endpoint, Map<String, String> queryParams) {
        this.endpoint = endpoint;
        this.queryParams = queryParams;
    }

    public static GetTo service(String endpoint, Map<String, String> queryParams) {
        return instrumented(GetTo.class, endpoint, queryParams);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource(endpoint)
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .queryParams(queryParams)
                                .relaxedHTTPSValidation()
                        )
        );
    }
}
