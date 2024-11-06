package co.com.udea.certificacion.authb.tasks;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetUserList implements Task {

    public static GetUserList fromService() {
        return instrumented(GetUserList.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/users/v1")
                        .with(request -> request
                                .relaxedHTTPSValidation()
                                .accept("application/json")
                        )
        );
        SerenityRest.lastResponse().prettyPrint();
    }
}

