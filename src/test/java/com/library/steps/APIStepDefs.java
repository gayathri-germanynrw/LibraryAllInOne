package com.library.steps;


import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import org.junit.Assert;


import static org.hamcrest.Matchers.*;

public class APIStepDefs {

    RequestSpecification givenPart= RestAssured.given().log().uri();
    Response response;
    ValidatableResponse thenPart;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {
        givenPart.header("x-library-token", LibraryAPI_Util.getToken(userType));
    }
    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        givenPart.accept(acceptHeader);
    }
    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
         response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint);

         thenPart = response.then();

    }
    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        // OPT 1
        thenPart.statusCode(expectedStatusCode);

        // OPT 2
        Assert.assertEquals(expectedStatusCode,response.statusCode());

    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        // OPT 1
        thenPart.contentType(contentType);

        // OPT 2
        Assert.assertEquals(contentType,response.contentType());
    }
    @Then("Each {string} field should not be null")
    public void each_field_should_not_be_null(String path) {
        thenPart.body(path, everyItem(notNullValue()));

        /*
        JsonPath jp = thenPart.extract().jsonPath();
        List<Object> allData = jp.getList(path);
        for (Object eachData : allData) {
            Assert.assertNotNull(eachData);
        }
         */


    }

}
