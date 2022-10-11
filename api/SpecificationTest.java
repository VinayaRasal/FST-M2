package Example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class SpecificationTest {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    int petId;

    @BeforeClass
            public void SetUp(){
        //RequestSpecification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                .addHeader("Content-Type","application/json")
                .build();


        // Response specification
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(3000L))
                .expectBody("status",equalTo("alive"))
                .build();

    }

    @Test (priority = 1)
            public void PostRequest(){
        // request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id",1010);
        reqBody.put("name","Tommy");
        reqBody.put("status","alive");

        Response response = given().spec(requestSpec).body(reqBody).when().post();
        //Extract petid
        petId = response.then().extract().path("id");
        System.out.println(petId);

        // assertion
        response.then().spec(responseSpec);
    }

    @Test (priority = 2)
    public void GetRequest(){
        //generate response and assert

        given().spec(requestSpec).pathParam("petId",petId)
                .when().get("/{petId}")
                .then().spec(responseSpec);
    }

    @Test (priority = 3)
    public void DeleteRequest(){
        //generate response and assert

        given().spec(requestSpec).pathParam("petId",petId)
                .when().delete("/{petId}")
                .then().statusCode(200).time(lessThan(3000L));
    }



}
