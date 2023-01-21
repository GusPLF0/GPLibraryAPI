package br.com.gusapi.integrationtests.controller.withxml;

import br.com.gusapi.configs.TestConfigs;
import br.com.gusapi.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.gusapi.integrationtests.vo.AccountCredentialsVO;
import br.com.gusapi.integrationtests.vo.PersonVO;
import br.com.gusapi.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static br.com.gusapi.configs.TestConfigs.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {


    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonVO personVO;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        personVO = new PersonVO();
    }

    @Test
    @Order(0)
    public void authorization() throws IOException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        String accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(CONTENT_TYPE_XML)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class)
                .getAcessToken();

        specification = new RequestSpecBuilder()
                .addHeader(HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

    }

    private static TokenVO tokenVO;

    @Test
    @Order(1)
    public void testCreate() throws Exception{
        mockPerson();

        String content = given(specification)
                .contentType(CONTENT_TYPE_XML)
                    .body(personVO)
                    .when()
                    .post()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        personVO = createdPerson;

        assertNotNull(createdPerson);

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Richard", createdPerson.getFirstName());
        assertEquals("Stallman", createdPerson.getLastName());
        assertEquals("US-New York", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(2)
    public void testUpdate() throws Exception{
        personVO.setLastName("Stallman JR");

        String content = given(specification)
                .contentType(CONTENT_TYPE_XML)
                .body(personVO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);

        assertNotNull(createdPerson);

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertEquals(personVO.getId(), createdPerson.getId());

        assertEquals("Richard", createdPerson.getFirstName());
        assertEquals("Stallman JR", createdPerson.getLastName());
        assertEquals("US-New York", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(3)
    public void testFindById() throws IOException {
        mockPerson();


        String content =
                given()
                        .spec(specification)
                        .contentType(CONTENT_TYPE_XML)
                        .header(HEADER_PARAM_ORIGIN, ORIGIN_MAIN_URL)
                        .pathParam("id", personVO.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        personVO = createdPerson;

        assertNotNull(createdPerson);

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertEquals(personVO.getId(), createdPerson.getId());

        assertEquals("Richard", createdPerson.getFirstName());
        assertEquals("Stallman JR", createdPerson.getLastName());
        assertEquals("US-New York", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(4)
    public void testDelete() throws IOException {
                given()
                        .spec(specification)
                        .pathParam("id", personVO.getId())
                        .when()
                        .delete("{id}")
                        .then()
                        .statusCode(204);
    }


    @Test
    @Order(5)
    public void testFindAll() throws Exception{

        var content = given(specification)
                .contentType(CONTENT_TYPE_XML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<PersonVO> people = objectMapper.readValue(content, new TypeReference<List<PersonVO>>() {});

        PersonVO person01 = people.get(0);
        personVO = person01;

        assertNotNull(person01.getId());
        assertNotNull(person01.getFirstName());
        assertNotNull(person01.getLastName());
        assertNotNull(person01.getAddress());
        assertNotNull(person01.getGender());

        assertEquals(1, (long) person01.getId());

        assertEquals("Michal", person01.getFirstName());
        assertEquals("Jackson", person01.getLastName());
        assertEquals("EUA", person01.getAddress());
        assertEquals("Male", person01.getGender());

        PersonVO person02 = people.get(1);
        personVO = person02;

        assertNotNull(person02.getId());
        assertNotNull(person02.getFirstName());
        assertNotNull(person02.getLastName());
        assertNotNull(person02.getAddress());
        assertNotNull(person02.getGender());

        assertEquals(2, (long) person02.getId());

        assertEquals("Albert", person02.getFirstName());
        assertEquals("Einstein", person02.getLastName());
        assertEquals("Alemanha", person02.getAddress());
        assertEquals("Male", person02.getGender());
    }

    private void mockPerson() {
        personVO.setFirstName("Richard");
        personVO.setLastName("Stallman");
        personVO.setAddress("US-New York");
        personVO.setGender("Male");
    }
}
