package framework;

import java.util.logging.Logger;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BaseClassForUI {
	static Logger logger = Logger.getLogger(BaseClassForUI.class.getName());
	public Playwright playwright;
	public Browser browser;
	public Page page;
	
	@BeforeTest
	public void launchPlayWright(){
		playwright = Playwright.create();
		browser =playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
	}
	
	
	@AfterTest
	public void tearDown() {
		page.close();
		playwright.close();
	}

}
