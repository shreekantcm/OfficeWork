package com.java.modules.usermanagement;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.java.common.DriverUtil;
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadPropertiesFile;

public class UploadUserFromExcel {
	
	
	public static WebDriver driver;
	
	@Test(priority=1)
	public void initializedDriver(){
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		driver = DriverUtil.getDriver(driver,drivername);
	}
	
	@Test(priority=2)
	public void userLogin(){
		String username = ReadPropertiesFile.getPropValues("username");
		String password = ReadPropertiesFile.getPropValues("password");
		LoginUtil.userLogin(username, password,driver);
	}
	
	@Test(priority=3)
	public void navigateOnPage(){
		NavigationUtil.navigationOnSubMenu("User", "Manage",driver);
	}
	
	// Logic when uploading bulk users via excel sheet - we have 7 valid data in excel sheet 
	// Therefore When userid is unique for those entry and also other data is proper then 
	// below success will displayed
	// 7 user(s) have been saved.
	
	// If you will submit the same sheet again then below msg should displayed
	// "Total 7 user(s) are already present."
	
	// Rest of the input data having some problem so it should displayed below message
	// "14 user(s) have not been saved."   // Here 14 should be the (total entry - 7 valid entry)
	// "User(s) not added due to missing/inappropriate information"
	@Test(priority=4)
	public void uploadBulkUserFromExcel() throws IOException, InterruptedException{
		WebElement uploadUserButton = driver.findElement(By.id("uploadUsers"));
		uploadUserButton.click();
		driver.findElement(By.xpath("//input[@type='file']")).click();
		Thread.sleep(3000);
		String basePath = System.getProperty("user.dir");
		Runtime.getRuntime().exec(basePath+"/resource/autoIT/BulkUploadFromExcel.exe");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//button[@data-ng-click='submit()']")).click();
		
		// 7 user(s) have been saved.
		String actualSavedUserMsg= driver.findElement(By.xpath("//span[contains(.,'have been saved.')]")).getText();
		String expectedSavedUserMsg = "1 user(s) have been saved.";
		System.out.println("actualSavedUserMsg = "+actualSavedUserMsg);
		Assert.assertTrue(actualSavedUserMsg.equals(expectedSavedUserMsg), "Either you have not added all 7 proper valid entry" +
				"or user id is repeating. If saved user are displaying more than 7 then error in application.");
		
		//div[@class='container']//button[@class='close']//following-sibling::div//span
		// message  - 25 user(s) have not been saved.
		String expectedUserNotSavedMsg = "25 user(s) have not been saved.";
		String actualUserNotSavedMsg = driver.findElement(By.xpath("//span[contains(.,'have not been saved.')]")).getText();
		Assert.assertTrue(actualUserNotSavedMsg.equals(expectedUserNotSavedMsg), "The actual message of user not saved is ="+actualUserNotSavedMsg);
		
		// CASE:2
		// This functionality only execute when your know all valid user present in excel sheet are already in DB 
		// Total 7 user(s) are already present.
		
		driver.findElement(By.xpath("//input[@type='file']")).click();
		Thread.sleep(3000);
		Runtime.getRuntime().exec(basePath+"/resource/autoIT/BulkUploadFromExcel.exe");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//button[@data-ng-click='submit()']")).click();
		String actualUserPresentMsg = driver.findElement(By.xpath("//span[contains(.,'user(s) are already')]")).getText();
		String expectedUserPresentMsg = "Total 7 user(s) are already present.";
		Assert.assertTrue(actualUserPresentMsg.equals(expectedUserPresentMsg), "The actual message of already present is ="+actualUserPresentMsg);
		
	}
	@AfterTest
	public void closeDriver(){
		driver.quit();
	}
}
