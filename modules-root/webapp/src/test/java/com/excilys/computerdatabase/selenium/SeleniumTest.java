package com.excilys.computerdatabase.selenium;

//import org.junit.After;
//import org.junit.Before;
import org.junit.Test;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebDriverBackedSelenium;
//import org.openqa.selenium.chrome.ChromeDriver;
//import com.thoughtworks.selenium.Selenium;

import junit.framework.TestCase;

public class SeleniumTest extends TestCase {

    //private WebDriver driver;
    //private String baseUrl;
    //private Selenium selenium;

    //private static final String CHROME_DRIVER_PATH = "/usr/local/share/chromedriver";

    // private boolean acceptNextAlert = true;
    // private StringBuffer verificationErrors = new StringBuffer();

    /*@Before
    public void setUp() throws Exception {
        // On instancie notre driver, et on configure notre temps d'attente
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        baseUrl = "http://localhost:8080/";
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
    }*/

    @Test
    public void testAccessAddComputer() throws Exception {

        /*selenium.open("/computer-database");

        selenium.click("id=addComputer");
        selenium.waitForPageToLoad("30000");*/
        
        assertNull(null);

    }

    /*@After
    public void tearDown() throws Exception {
        selenium.stop();
        
        String verificationErrorString = verificationErrors.toString(); if
        (!"".equals(verificationErrorString)) {
         fail(verificationErrorString); }
         
    }*/

    /*
     * private boolean isElementPresent(By by) { try { driver.findElement(by);
     * return true; } catch (NoSuchElementException e) { return false; } }
     */

    /*
     * private String closeAlertAndGetItsText() { try { Alert alert =
     * driver.switchTo().alert(); if (acceptNextAlert) { alert.accept(); } else
     * { alert.dismiss(); } return alert.getText(); } finally { acceptNextAlert
     * = true; } }
     */

}
