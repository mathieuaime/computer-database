package com.excilys.computerdatabase.selenium;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;

public class TestDashboard {

    private static WebDriver driver;

    private static String baseUrl;

    @BeforeClass
    public static void beforeClass() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        baseUrl = "http://localhost:8080/computerDatabase";
        login();
    }

    @AfterClass
    public static void after() throws IOException {
        driver.quit();
    }

    @Test
    public void testUrl() {
        driver.get(baseUrl + "/dashboard");
        assertEquals(driver.getCurrentUrl(), baseUrl + "/dashboard");
    }

    @Test
    public void testRedirectAddComputeur() {
        driver.get(baseUrl + "/dashboard");
        WebElement addComputer = driver.findElement(By.id("addComputer"));
        addComputer.click();
        assertEquals(driver.getCurrentUrl(), baseUrl + "/add");
    }

    private static void login() {
        driver.get(baseUrl + "/login");

        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.id("submit")).click();
    }
}
