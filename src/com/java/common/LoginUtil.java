package com.java.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginUtil {

	static String input_userName = "//*[@id='txtusername']";
	static String input_password = "//*[@id='txtpassword']";
	static String button_submitButton = "//button[text()='LOGIN']";

	public static void userLogin(String username,String password,WebDriver driver) {
			WaitUtil.waitForElementXPATH(input_userName,driver);
			driver.findElement(By.xpath(input_userName)).clear();
			driver.findElement(By.xpath(input_userName)).sendKeys(username);
			
			driver.findElement(By.xpath(input_password)).clear();
			driver.findElement(By.xpath(input_password)).sendKeys(password);
			
			driver.findElement(By.xpath(button_submitButton)).click();
			Log.info("User loggedin succesfully");
	}
}
