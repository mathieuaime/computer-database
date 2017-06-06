package com.excilys.computerdatabase.selenium;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;

public class TestAddComputer {

    private static WebDriver driver;

    private static String baseUrl;

//    @BeforeClass
//    public static void beforeClass() {
//        ChromeDriverManager.getInstance().setup();
//        driver = new ChromeDriver();
//        baseUrl = "http://localhost:8080/computerDatabase";
//        login();
//    }
//
//    @AfterClass
//    public static void after() throws IOException {
//        driver.quit();
//    }
//
//    @Test
//    public void testAddShouldNotWork() {
//        driver.get(baseUrl + "/add");
//        WebElement inputName = driver.findElement(By.id("computerName"));
//        WebElement buttonAdd = driver.findElement(By.id("submit"));
//        inputName.sendKeys("dfd&é'(-@");
//        buttonAdd.click();
//        assertEquals(driver.getCurrentUrl(), baseUrl + "/add");
//    }
//
//    @Test
//    public void testAddShouldWork() {
//        driver.get(baseUrl + "/add");
//        // assertEquals(driver.getTitle(), "dashboard");
//        WebElement inputName = driver.findElement(By.id("computerName"));
//        WebElement buttonAdd = driver.findElement(By.id("submit"));
//        inputName.sendKeys("name is valid");
//        buttonAdd.click();
//        assertEquals(driver.getCurrentUrl(), baseUrl + "/");
//    }
//    
//    private static void login(){
//        driver.get(baseUrl+ "/login");
//
//        driver.findElement(By.id("username")).sendKeys("admin");
//        driver.findElement(By.id("password")).sendKeys("admin");
//        driver.findElement(By.id("submit")).click();
//    }
}
