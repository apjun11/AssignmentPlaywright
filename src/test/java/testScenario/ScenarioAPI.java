package testScenario;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;

import framework.BaseClassForAPI;
import framework.User;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ScenarioAPI extends BaseClassForAPI{
	static Logger logger = Logger.getLogger(ScenarioAPI.class.getName());

	static String emailId, username;
	User user,actUser;
	APIResponse apiPostResponse,apiGetResponse;
	

    @Test
    @Description("Create a new user and verify the response status and body")
    @Story("User Creation")
    @Severity(SeverityLevel.CRITICAL)
    public void createNewUser() throws IOException {
        Reporter.log("Creating new user");

        String username = "TestUser" + System.currentTimeMillis();
        String emailId = username.toLowerCase() + "@gmail.com";
        user = new User(username, emailId, "male", "active");

        // POST request to create a user
        apiPostResponse = requestContext.post("https://gorest.co.in/public/v2/users",
        		RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer 328fe2cde1ee9f80eee87ac5e26021e2358f551bc9092e0dede8d8c728ca0463")
                        .setData(user));
    }
    
    @Test(dependsOnMethods = "createNewUser",description = "validate response status")
    public void validatePostResponseStatus() {

        // Attach request and response details to the Allure report
        Allure.addAttachment("POST Request Data", apiPostResponse.text());
        Reporter.log("User created with response: " + apiPostResponse.text());

        // Validate response status
        Assert.assertEquals(apiPostResponse.status(), 201);
        Assert.assertEquals(apiPostResponse.statusText(), "Created");
    }

    @Test(dependsOnMethods = "validatePostResponseStatus")
    @Description("Validate the response body of the created user")
    @Story("Response Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void validatePostResponseBody() throws IOException {
        // Extract and validate response
        String responseText = apiPostResponse.text();
        ObjectMapper objectMapper = new ObjectMapper();
        User actUser = objectMapper.readValue(responseText, User.class);

        // Attach response body to Allure report
        Allure.addAttachment("User Response Body", responseText);
        Reporter.log("Validating response body");

        Assert.assertEquals(actUser.getName(), user.getName());
        Assert.assertEquals(actUser.getEmail(), user.getEmail());
        Assert.assertEquals(actUser.getStatus(), user.getStatus());
        Assert.assertEquals(actUser.getGender(), user.getGender());
        Assert.assertNotNull(actUser.getId());
    }
}

