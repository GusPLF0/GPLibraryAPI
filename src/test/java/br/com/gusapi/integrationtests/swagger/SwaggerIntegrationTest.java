package br.com.gusapi.integrationtests.swagger;

import br.com.gusapi.configs.TestConfigs;
import br.com.gusapi.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void shouldDisplaySwaggerUiPage() {
        String content =
                given()
                    .basePath("/swagger-ui/index.html")
                    .port(TestConfigs.SERVER_PORT)
                    .when()
                        .get()
                    .then()
                        .statusCode(200)
                    .extract()
                        .body()
                            .asString();
        Assertions.assertTrue(content.contains("Swagger UI"));
    }

}
