package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {

	Faker faker;
	User userPayload;
	
	public Logger logger;

	@BeforeClass
	public void setup() { // to generate test data using faker dependency
		faker = new Faker();
		userPayload = new User();

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
//		userPayload.setUserStatus(faker.phoneNumber().cellPhone());
		
		//logs
		logger = LogManager.getLogger(this.getClass());
		
		logger.debug("debugging ..........");

	}

	@Test(priority=1)
	public void testPostUser() {
		
		logger.info("**********************CREATING USER********************************");
		Response response = UserEndPoints.createUser(userPayload);
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200); // response contains all things like body, status and all.

	}

	@Test(priority=2)
	public void testGetUserByName() {
		logger.info("**********************READING USER INFO********************************");
		Response response = UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200); // also can be done like repsonse.statusCode()

	}

	@Test(priority= 3)
	public void testUpdateUserByName() {

		// update data
		logger.info("**********************UPDATING USER********************************");
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());

		Response response = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);

		response.then().log().body();

//		response.then().log().body().statusCode(200);
		Assert.assertEquals(response.getStatusCode(),200); // also can be done like repsonse.statusCode()

		// checking data after update
		logger.info("********************** USER UPDATED********************************");
		Response responseAfterUpdate = UserEndPoints.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterUpdate.getStatusCode(),200); // also can be done like repsonse.statusCode()

	}
	
	
	@Test(priority=4)
	public void testDeleteUserByName() {
		logger.info("**********************DELETING USER********************************");
		Response response = UserEndPoints.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(),200);
		
		logger.info("**********************DELETED USER********************************");
	}

}
