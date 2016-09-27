package com.java.modules.LoginAndForgotPassword;


import java.sql.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.java.common.DriverUtil;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
/**
 * Check the functionality of forgot password
 * @author nandushende
 */
public class ForgotPassword {
	
	String link_forgotPassword = ReadLocatorsFromExcel.readExcel("link_forgotPassword","LoginHome");
	String text_blankUserNameErrMsg  = ReadLocatorsFromExcel.readExcel("text_blankUserNameErrMsg","LoginHome");
	String text_invalidUserNameErrMsg = ReadLocatorsFromExcel.readExcel("text_invalidUserNameErrMsg","LoginHome");
	String text_forgotPasswordSuccessMsg = ReadLocatorsFromExcel.readExcel("text_forgotPasswordSuccessMsg","LoginHome");
	
	String input_username = ReadLocatorsFromExcel.readExcel("input_username","LoginHome");
	
	public static WebDriver driver;
	@Test
	public void initializedDriver(){
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		driver = DriverUtil.getDriver(driver,drivername);
	}
	
	// when click on forgot password link without entered username 
	// it show error message = Please enter user name !!
	@Test(priority = 1)
	public void blankUserName(){
		System.out.println("in blankUserName");
		driver.findElement(By.xpath(link_forgotPassword)).click();
		String actualErrorMsg = driver.findElement
				(By.xpath(text_blankUserNameErrMsg)).getText();
		String expectedErrorMsg = "Please enter user name !!";
		
		if(driver.findElement(By.id(input_username)).getText().equals("")){
			Assert.assertEquals(expectedErrorMsg,actualErrorMsg );
		}
	}
	// When user entered invalid user name and click on forgot password link
	// It show error message = Invalid User
	@Test(priority = 2)
	public void invalidUserName() throws InterruptedException{
		System.out.println("in invalidUserName");
		driver.findElement(By.id(input_username)).click();
		driver.findElement(By.id(input_username)).sendKeys("invalidName");
		driver.findElement(By.xpath(link_forgotPassword)).click();
		Thread.sleep(2000);
		String actualErrorMsg =driver.findElement
		(By.xpath(text_invalidUserNameErrMsg)).getText();
		String expectedErrorMsg = "Invalid User";
		Assert.assertEquals(expectedErrorMsg,actualErrorMsg );
	}
	
	// When click user entered the user name and click on forgot password
	// Link then should show success message = Password has been sent to user mail-id
	@Test(priority = 3)
	public void forgotPasswordSuccess() throws InterruptedException{
		System.out.println("in forgotPassword");
		Thread.sleep(2000);
		driver.findElement(By.id(input_username)).click();
		driver.findElement(By.id(input_username)).clear();
		driver.findElement(By.id(input_username)).sendKeys("java_admin");
		driver.findElement(By.xpath(link_forgotPassword)).click();
		Thread.sleep(2000);
		String actualErrorMsg = driver.findElement(By.xpath(text_forgotPasswordSuccessMsg)).getText();
		String expectedErrorMsg = "Password has been sent to user mail-id";
		Assert.assertEquals(expectedErrorMsg,actualErrorMsg );
	}

	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
}
