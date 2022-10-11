package LiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
// Headers object
Map<String,String> headers = new HashMap<>();

// add resource path
     String resourcePath = "/api/users";
//Generate a contract
    @Pact(consumer = "UserConsumer",provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){
        // add headers
        headers.put("Content-Type","application/json");
        //create json body for request and response. Provide default values
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id",101)
                .stringType("firstName","Vinaya")
                .stringType("lastName","Rasal")
                .stringType("email","vinaya.rasal1@ibm.com");
        //Write the fragment(interaction)  to Pact
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                .method("POST")
                .headers(headers)
                .path(resourcePath)
                .body(requestResponseBody)
                .willRespondWith()
                .status(201)
                .body(requestResponseBody)
                .toPact();

    }
    @Test
    @PactTestFor(providerName = "UserProvider", port = "8080")
    public  void consumerTest(){
        String reqURI = "http://localhost:8080"+ resourcePath;

        //request Body
        Map<String,Object> reqBody = new HashMap<>();
        reqBody.put("id",101);
        reqBody.put("firstName","Vinaya");
        reqBody.put("lastName","Rasal");
        reqBody.put("email","vinaya.rasal1@ibm.com");

        //generate response
        given().headers(headers).body(reqBody)
                .when().post(reqURI)
                .then().statusCode(201).log().all();

    }
}
