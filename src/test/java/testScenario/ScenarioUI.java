package testScenario;

import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import com.microsoft.playwright.*;
import com.microsoft.playwright.Page.ScreenshotOptions;

import framework.BaseClassForUI;


import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Attachment;

public class ScenarioUI extends BaseClassForUI {
    static Logger logger = Logger.getLogger(ScenarioUI.class.getName());

    
    // Locators
    String locator_SearchDropdown = "#searchDropdownBox"; // By ID
    String selectDropdownOption = "Electronics";
    String locator_ActualDropdownValue = "#nav-search-label-id";
    String locator_searchBox = "#twotabsearchtextbox";
    String locator_suggestion = ".left-pane-results-container";
    String locator_Iphone128GBFromSearchBox = "div[aria-label='iphone 13 128 gb']";
    String locator_Iphone128GBPink = "(//span[@class='a-size-medium a-color-base a-text-normal'][text()='Apple iPhone 13 (128GB) - Pink'])";
    String locator_Iphone128GBPinkTitle = "#productTitle";
    String locator_visitStoreLink = "#bylineInfo";
    String locator_WatchDropdownInAppleStore = "(//span[text()='Apple Watch'])[1]";
    String locator_SelectWatch = "(//span[text()='Apple Watch SE (GPS + Cellular)'])";
    String locator_QuickLook = "div[id='4k2av5ugzs'] span[class='QuickLook__label__tOBqR']";
    String locator_WatchiImage = "(//a[contains(@title,'Apple Watch SE (2nd Gen)[GPS + Cellular 40 mm]smart watch w/Starlight Aluminium Case')])";
    String locator_WatchTitleFromQuickView = ".ProductShowcase__title__SBCBw";
    
    Page newPage;
    Locator suggestion, searchBox, visitStoreLink;
    String url="https://www.amazon.in/";

    @Test(description="Launch Amazon Url")
    @Step("Launching Amazon URL")
    @Description("Launches the Amazon URL and logs the page title.")
    @Severity(SeverityLevel.CRITICAL)
    public void launchUrl() {
    	
        page = browser.newPage();
        page.navigate(url);
        
        Reporter.log("Launch Url:  "+url);
        
        logger.info(page.title());
    }
    
    @Test(dependsOnMethods = {"launchUrl"}, description="Assert Page Title with Expected Title")
    @Step("Asserting Page Title")
    @Description("Asserts the page title against the expected title.")
    public void assertTitle() {
    	
    	Reporter.log("AssertTitle: "+ page.title());
    	
        String pageTitle = page.title();
        Assert.assertEquals(pageTitle, "Online Shopping site in India: Shop Online for Mobiles, Books, Watches, Shoes and More - Amazon.in");
    }

    @Test(dependsOnMethods = {"assertTitle"}, description="Select Electronics for Search Box Dropdown")
    @Step("Selecting Electronics from Dropdown")
    @Description("Selects 'Electronics' from the search box dropdown.")
    public void selectDropdown() {
    	Reporter.log("Select Dropdown");
    	
        Locator dropdown = page.locator(locator_SearchDropdown);
        dropdown.selectOption(selectDropdownOption);
        logger.info("Dropdown Clicked");
    }

    @Test(dependsOnMethods = {"selectDropdown"}, description="Validate if Electronics is selected")
    @Step("Validating Dropdown Value")
    @Description("Validates if 'Electronics' is selected from the dropdown.")
    public void assertDropdownValue() {
    	Reporter.log("Assert Dropdown Value");
    	
        String actualDropdownValue = page.textContent(locator_ActualDropdownValue);
        Assert.assertEquals(actualDropdownValue, selectDropdownOption);
    }

    @Test(dependsOnMethods = {"assertDropdownValue"}, description="Search For Iphone 13")
    @Step("Searching for iPhone 13")
    @Description("Fills 'iPhone 13' in the search box and waits for suggestions.")
    public void searchIPhone() {
    	
    	Reporter.log("SearchIPhone");
    	
        searchBox = page.locator(locator_searchBox);
        searchBox.fill("Iphone 13");
        suggestion = page.locator(locator_suggestion);
        suggestion.waitFor();
    }

    @Test(dependsOnMethods = {"searchIPhone"}, description="Screenshot Of Suggestions For iPhone 13 related products")
    @Step("Taking Screenshot of Suggestions")
    @Description("Takes a screenshot of the suggestions related to iPhone 13.")
    public void takeScreenshotOfSuggestions() {
    	Reporter.log("takeScreenshotOfSuggestions");
    	ScreenshotOptions screenshotoptions= new ScreenshotOptions();
		page.screenshot(screenshotoptions.setPath(Paths.get("./screenshots/searchboxsuggestions.png")));
		
		
    }

    @Test(dependsOnMethods = {"takeScreenshotOfSuggestions"}, description="Validate if the suggestions are related to iPhone 13")
    @Step("Validating Suggestions from Dropdown")
    @Description("Validates if the suggestions are related to iPhone 13.")
    public void validateSuggestionsfromDropdown() {
    	Reporter.log("validateSuggestionsfromDropdown");
    	
    	//Storing all String values from suggestion into the List
        List<String> allValues = suggestion.allTextContents();
        for (String actualStr : allValues) {
        	
        	//Assert value if String contains 13 from IPhone 13 which will be validation if the suggestions are related to IPhone 13
            Assert.assertTrue(actualStr.contains("13"), actualStr);
        }
    }

    @Test(dependsOnMethods = {"validateSuggestionsfromDropdown"}, description="Clear Searchbox and search iPhone 13 128 GB")
    @Step("Clearing Search Box and Searching iPhone 13 128 GB")
    @Description("Clears the search box and searches for 'iPhone 13 128 GB'.")
    public void clearSearchBoxAndSearchIphone() {
    	Reporter.log("clearSearchBoxAndSearchIphone");
    	
    	
        logger.info("Clearing Value from the SearchBox");
        searchBox.clear();
        searchBox.fill("IPhone 13 128 GB");
        page.locator(locator_Iphone128GBFromSearchBox).click();
        page.waitForLoadState();
    }

    @Test(dependsOnMethods = {"clearSearchBoxAndSearchIphone"}, description="Select iPhone and moved to new tab")
    @Step("Selecting iPhone and Opening New Tab")
    @Description("Selects the iPhone and opens a new tab.")
    public void selectProduct() {
    	Reporter.log("selectProduct");
    	
    	
        page.waitForLoadState();
        newPage = page.waitForPopup(() -> {
            page.locator(locator_Iphone128GBPink).click();
        });
        newPage.waitForLoadState();
        logger.info("New Tab opened");
    }

    @Test(dependsOnMethods = {"selectProduct"}, description="Validate New Tab Title and Product")
    @Step("Validating New Tab Title and Product")
    @Description("Validates the title and product on the new tab.")
    public void validatePageTitleAndProduct() {
    	Reporter.log("validatePageTitleAndProduct");
    	
    	
        String actualNewPageTitle = newPage.title();
        logger.info("Switched New Tab with Title " + newPage.title());
        String expectedNewPageTitle = "Apple iPhone 13 (128GB) - Pink : Amazon.in: Electronics";
        Assert.assertEquals(actualNewPageTitle, expectedNewPageTitle);
        
        String actualProduct = newPage.textContent(locator_Iphone128GBPinkTitle);
        String expectedProduct = "Apple iPhone 13 (128GB) - Pink";
        Assert.assertTrue(actualProduct.contains(expectedProduct));
    }

    @Test(dependsOnMethods = {"validatePageTitleAndProduct"}, description="Validate and Click visit Apple Store Link")
    @Step("Validating and Clicking Visit Apple Store Link")
    @Description("Validates and clicks on the 'Visit Apple Store' link.")
    public void visitAppleStore() {
    	Reporter.log("visitAppleStore");
    	
    	
        newPage.waitForLoadState();
        visitStoreLink = newPage.locator(locator_visitStoreLink);
        Assert.assertTrue(visitStoreLink.isVisible());
        visitStoreLink.click();
        newPage.waitForLoadState();
        logger.info(newPage.title());
    }

    @Test(dependsOnMethods = {"visitAppleStore"}, description="Select Apple Watch Product")
    @Step("Selecting Apple Watch Product")
    @Description("Selects an Apple Watch product from the Apple Store.")
    public void selectAppleWatchFromDropdown() {
    	Reporter.log("selectAppleWatchFromDropdown");
    	
    	
        Assert.assertTrue(newPage.locator(locator_WatchDropdownInAppleStore).isVisible());
        newPage.locator(locator_WatchDropdownInAppleStore).click();
        newPage.locator(locator_SelectWatch).click();
        newPage.waitForLoadState();
        logger.info(newPage.title());
        
        newPage.locator(locator_WatchiImage).hover();
        Assert.assertTrue(newPage.locator(locator_QuickLook).isVisible());
        System.out.println("Quick look visible: " + newPage.locator(locator_QuickLook).isVisible());
        
        newPage.locator(locator_QuickLook).click();
        newPage.waitForLoadState();
    }
    
    @Test(dependsOnMethods = {"selectAppleWatchFromDropdown"}, description="Screenshot Of Apple Watch Product From Quick View")
    @Step("Taking Screenshot of Apple Watch from Quick View")
    @Description("Takes a screenshot of the Apple Watch product from the quick view.")
    
    public void takeScreenshotWatchFromQuickView() {
    	Reporter.log("takeScreenshotWatchFromQuickView");
    	
    	
    	ScreenshotOptions screenshotoptions= new ScreenshotOptions();
		page.screenshot(screenshotoptions.setPath(Paths.get("./screenshots/WatchFromQuickView.png")));
    }
    
    @Test(dependsOnMethods = {"takeScreenshotWatchFromQuickView"}, description="Validate Watch title From Quick View")
    @Step("Validating Watch Title from Quick View")
    @Description("Validates the title of the watch from the quick view.")
    public void validateWatchTitleFromQuickView() {
    	Reporter.log("validateWatchTitleFromQuickView");
        String expectedWatchTitle = "Apple Watch SE (2nd Gen)[GPS + Cellular 40 mm]";
        
        String watchTitle = newPage.locator(locator_WatchTitleFromQuickView).textContent();
        
        
        Assert.assertTrue(watchTitle.contains(expectedWatchTitle));
        logger.info(newPage.textContent(locator_WatchTitleFromQuickView));
    }
}

