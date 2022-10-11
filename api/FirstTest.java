package Example;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.testng.Assert.assertEquals;


public class FirstTest {
    //Basic uri
    String BaseURI= "https://petstore.swagger.io/v2/pet";

    @Test
    public void GetRequestQueryParam(){
        //generate response
        // Get https://petstore.swagger.io/v2/pet/findByStatus?status=sold
        Response response =
                given().header("content-type","application/json").queryParam("status","sold").
                when().get(BaseURI + "/findByStatus");
        //get response body
        System.out.println("****** Response body is ********");
        System.out.println(response.getBody().asPrettyString());
        //get response header
       // System.out.println("*********** Response Header is **************");
       // System.out.println(response.getHeaders().asList());
       // System.out.println(response.getHeader("content-type"));
        String petStatus = response.then().extract().path("[0].status");
        System.out.println(petStatus);
        assertEquals (petStatus,"sold");

       // for (i=0; i<=
         //       response.then().extract().path("[i].status"))
// assertions
        response.then().statusCode(200);
        response.then().time(lessThan(3000L));
        response.then().body("[0].status", equalTo("sold"));

       }

       @Test
    public void getRequestPathParam(){
        //Generate response
           given().header("content-type","application/json").pathParam("petId",12).
                   when().get(BaseURI + "/{petId}").
                   then().statusCode(200).time(lessThan(3000L));
           //.body("status", equalTo("available"));
       }
}
