package LiveProject;

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


public class GitHubTest {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    String sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDJkjgq3w375M76aVIWX2RvfLZU13RB4PROiPVU8l7PMWNld9toP0wv3cKOhyLZG4hGbbZyL06ll5yrITbbOebJnKiA7QKWKBLY0mCenbQ3RWvh/wpMgPU4ieAn5aeIwVJrV68ANmUY7c4EQEq86wq9bTtTTW250WhIiTivUFqg29H6lHJmrdoAPol9LckZxY3kC1j/pvuoWfmKljTOS9o/7/WpLn678/Sg5B/n+UPJqdtSIVop3a6pJfSc+h3ZoIiW4VoTFN9vpIUd7Jrjb4PcznVOi7doD3oK6VL0O0ZGl2whd0yukf89NMZGFoDON4B/rkuQaFR8gHqmlmDRQDTn";
    int sshId;

    @BeforeClass
    public void SetUp() {
        //RequestSpecification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","token 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")
                .build();
        // Response specification
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(201)
                .build();

    }
    @Test(priority = 1)
    public void PostRequest(){
        // request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("title", "TestAPIKey");
        reqBody.put("key", sshKey);


        Response response = given().spec(requestSpec).body(reqBody).when().post("/user/keys");
        //Extract sshId
        sshId = response.then().extract().path("id");
       System.out.println(sshId);

        // assertion
        response.then().spec(responseSpec);
    }

    @Test (priority = 2)
    public void GetRequest(){
        //generate response and assert

        Response response =given().spec(requestSpec).pathParam("sshId", sshId)
                .when().get("/user/keys/{sshId}");
        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(200).body("key",equalTo(sshKey));


    }

    @Test (priority = 3)
    public void DeleteRequest(){
        //generate response and assert

        given().spec(requestSpec).pathParam("sshId",sshId)
                .when().delete("/user/keys/{sshId}")
                .then().statusCode(204);



    }



}
