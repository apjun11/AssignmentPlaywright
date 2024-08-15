package framework;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;

public class BaseClassForAPI {
	public Playwright playwright;
	public APIRequest request;
	public APIRequestContext requestContext;
	
	
	@BeforeTest
	public void apiSetup() {
		
		playwright = Playwright.create();
		request = playwright.request();
		requestContext = request.newContext();
		
		
	}
	
	
	@AfterTest
	public void tearDown() {
		
		playwright.close();
		
	}

}
