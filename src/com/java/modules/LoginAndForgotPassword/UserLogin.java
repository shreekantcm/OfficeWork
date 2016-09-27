package com.java.modules.LoginAndForgotPassword;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.java.common.DriverUtil;
import com.java.common.ReadExcelFile;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
import com.java.common.WaitUtil;
/**
 * Reads the test data from Excel file and perform testing using different test data on login module
 * @author nandushende
 */
public class UserLogin {
	
	String input_username = ReadLocatorsFromExcel.readExcel("input_username","LoginHome");
	String input_password  = ReadLocatorsFromExcel.readExcel("input_password","LoginHome");
	
	String button_submit = ReadLocatorsFromExcel.readExcel("button_submit","LoginHome");
	String button_close  = ReadLocatorsFromExcel.readExcel("button_close","LoginHome");
	String button_closeByCrossImage = ReadLocatorsFromExcel.readExcel("button_closeByCrossImage","LoginHome");
	
	String text_errorMessageSpan =  ReadLocatorsFromExcel.readExcel("text_errorMessageSpan","LoginHome");
	String text_invalidCredentialErrMsg = ReadLocatorsFromExcel.readExcel("text_invalidCredentialErrMsg","LoginHome"); 
	String text_welcomeUserName = ReadLocatorsFromExcel.readExcel("text_welcomeUserName", "LoginHome");
	
	String urlAdminPage = ReadPropertiesFile.getPropValues("urlOfAdminPage");
	public static WebDriver driver;
	@Test
	public void initializedDriver(){
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		driver = DriverUtil.getDriver(driver,drivername);
	}
	
	// This function will check the login functionality of application using different set of data
	@Test(dataProvider = "getData")
	public void userLogin(String username,String password,String message) throws InterruptedException{
		System.out.println("username="+username+"    password="+password+"     message="+message);
		
		WaitUtil.waitForElementID(input_username,driver);
		Thread.sleep(3000);
		driver.findElement(By.id(input_username)).clear();
		driver.findElement(By.id(input_password)).clear();
		driver.findElement(By.id(input_username)).click();
		driver.findElement(By.id(input_password)).click();
		driver.findElement(By.id(input_username)).sendKeys(username);
		driver.findElement(By.id(input_password)).sendKeys(password);
		driver.findElement(By.xpath(button_submit)).click();
		
		if(message.equals("Invalid UserName/Password.")){
			WaitUtil.waitForElementXPATH(text_invalidCredentialErrMsg,driver);
			String actualErroMsg = driver.findElement(By.xpath(text_invalidCredentialErrMsg)).getText();
			driver.findElement(By.xpath(button_close)).click();
			Assert.assertEquals(actualErroMsg, message, "The Expecte error message <"+message+"> does not match with the actual message");
		}
		else if(message.equals(urlAdminPage)){
			WaitUtil.waitForElementXPATH(text_welcomeUserName,driver);
			String welcomeText = driver.findElement(By.xpath(text_welcomeUserName)).getText();
			int a = welcomeText.indexOf("Welcome");
			int b = welcomeText.lastIndexOf("|");
			
			String userNameOnWelcomePage = welcomeText.substring(a+8, b);
			//System.out.println("a="+a+"   b="+b+"    sub1="+sub1+"   userNameOnWelcomePage="+userNameOnWelcomePage.trim());
			if(userNameOnWelcomePage.trim().equals(username)){
				//System.out.println("User login successfully");
			}else{
				Assert.assertTrue(false, "Use is not able to login succesfully");
			}
		}
		else{
			// Wait until the error message start with the text please will appear on the page
			WaitUtil.waitForElementXPATH("//span[contains(.,'Please')]",driver); 
			List<WebElement> list = driver.findElements(By.xpath(text_errorMessageSpan));
			for(WebElement li : list){
				//System.out.println("li.getText() = "+li.getText());
				if(li.getText().trim().length()>0){
					String actualErrorMessage = li.getText();
					//System.out.println("actual erro mesage = "+actualErrorMessage);
					Assert.assertEquals(actualErrorMessage, message, "The Expecte error message "+message+" does not match with the actual message");
				}
			}
		}
	}
	
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
	
	@DataProvider
	public Object[][] getData() throws IOException{
		ReadExcelFile readExcelFile = new ReadExcelFile(); 
		String filePath = "/resource/testData";
		String fileName = "UserLoginTestData.xlsx";
		String sheetName = "logincredential";
		Object data[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return data;
	}
}