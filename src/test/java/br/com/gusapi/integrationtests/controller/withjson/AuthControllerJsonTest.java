package br.com.gusapi.integrationtests.controller.withjson;

import br.com.gusapi.configs.TestConfigs;
import br.com.gusapi.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.gusapi.integrationtests.vo.AccountCredentialsVO;
import br.com.gusapi.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static br.com.gusapi.configs.TestConfigs.HEADER_PARAM_AUTHORIZATION;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

    private static TokenVO tokenVO;

    @Test
    @Order(1)
    public void testSignn() throws IOException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        tokenVO = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class);

        assertNotNull(tokenVO.getAcessToken());
        assertNotNull(tokenVO.getRefreshToken());

    }

    @Test
    @Order(2)
    public void testRefresh() throws IOException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var newtokenVO = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .pathParam("username", tokenVO.getUsername())
                    .header(HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class);

        assertNotNull(newtokenVO.getAcessToken());
        assertNotNull(newtokenVO.getRefreshToken());

    }
}
