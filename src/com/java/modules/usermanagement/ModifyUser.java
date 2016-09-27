package com.java.modules.usermanagement;

import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.java.common.AddUserUtil;
import com.java.common.DriverUtil;
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
import com.java.common.WaitUtil;

public class ModifyUser {
	String button_previlegeDropDown = ReadLocatorsFromExcel.readExcel("button_previlegeDropDown","ManageUser");//
	String button_Go = ReadLocatorsFromExcel.readExcel("button_Go", "ManageUser");//
	String button_submitEditForm = ReadLocatorsFromExcel.readExcel("button_submitEditForm", "ManageUser");//
	
	String input_search = ReadLocatorsFromExcel.readExcel("input_search","ManageUser");  //
	
	String widget_edit = ReadLocatorsFromExcel.readExcel("widget_edit", "ManageUser"); //
	
	String ddLink_associate = ReadLocatorsFromExcel.readExcel("ddLink_associate","ManageUser");
	
	String text_editUsertd  = ReadLocatorsFromExcel.readExcel("text_editUsertd","ManageUser");  //
	String text_successMsgOfUsrModified  = ReadLocatorsFromExcel.readExcel("text_successMsgOfUsrModified","ManageUser");  //
	
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
	
	@Test(priority=4)
	public void checkAndCreateUserIfNotPresent()throws InterruptedException{
		Actions actions = new Actions(driver);
		Thread.sleep(1000);
		actions.moveToElement(driver.findElement(By.className(button_previlegeDropDown))).click().build().perform();
		Thread.sleep(1000);
		driver.findElement(By.xpath(ddLink_associate)).click();
		driver.findElement(By.id(button_Go)).click();
		driver.findElement(By.id(input_search)).sendKeys("EditUser");
			
		boolean searchResultUser = false;
		
		try{
			Thread.sleep(1000);
			WaitUtil.waitForElementLocated(By.xpath(text_editUsertd), driver);
			searchResultUser = driver.findElement(By.xpath(text_editUsertd)).isDisplayed();
		}
		catch(Exception e){
			System.out.println("In catch");
		}
		if(searchResultUser){
			driver.findElement(By.xpath(text_editUsertd)).click();
		}else{
			AddUserUtil.addUser("EditUser", "firstName", "lastName", "test@gmail.com", "123456", "123456",
					"9999999999", "test@hmail.com", "Active", "Associate", "Unit", "User successfully added.",driver);
			Thread.sleep(5000);
			driver.findElement(By.id(input_search)).clear();
			driver.findElement(By.id(input_search)).sendKeys("EditUser");
			WaitUtil.waitForElementLocated(By.xpath(text_editUsertd), driver);
			driver.findElement(By.xpath(text_editUsertd)).click();
		}
	}
	
	@Test(dataProvider = "getUserData" ,priority=5,dependsOnMethods="checkAndCreateUserIfNotPresent")
	public void modifyUser(String privFilter, String userID, String fistName, String lastName, 
			String emailID, String password, String confPassword, String phoneNo, String 
			altrEmaiID,String DDStatus, String DDPrivilege, String DDRole, String msg) throws InterruptedException{
		
		WaitUtil.waitForElementLocated(By.xpath(text_editUsertd), driver);
		Thread.sleep(1000);
		if(driver.findElement(By.xpath(text_editUsertd)).getText().equals("EditUser")){
			driver.findElement(By.xpath(widget_edit)).click();
			Thread.sleep(2000);
			WaitUtil.waitForElementID("modifiedFirstNameId",driver);
			try{
			driver.findElement(By.id("modifiedFirstNameId")).click();
			driver.findElement(By.id("modifiedFirstNameId")).clear();	
			driver.findElement(By.id("modifiedFirstNameId")).sendKeys(fistName);
			
			driver.findElement(By.id("modifiedLastNameId")).click();
			driver.findElement(By.id("modifiedLastNameId")).clear();	
			driver.findElement(By.id("modifiedLastNameId")).sendKeys(lastName);
			
			driver.findElement(By.id("modifiedEmailId")).click();
			driver.findElement(By.id("modifiedEmailId")).clear();	
			driver.findElement(By.id("modifiedEmailId")).sendKeys(emailID);
			
			driver.findElement(By.id("modifiedPasswordId")).click();
			driver.findElement(By.id("modifiedPasswordId")).clear();	
			driver.findElement(By.id("modifiedPasswordId")).sendKeys(password);
			
			driver.findElement(By.id("modifiedConfirmPasswordId")).click();
			driver.findElement(By.id("modifiedConfirmPasswordId")).clear();	
			driver.findElement(By.id("modifiedConfirmPasswordId")).sendKeys(confPassword);
			
			driver.findElement(By.id("phoneId")).click();
			driver.findElement(By.id("phoneId")).clear();
			driver.findElement(By.id("phoneId")).sendKeys(phoneNo);
			
			driver.findElement(By.xpath("//input[@placeholder='Alternate Email ID']")).clear(); 
			driver.findElement(By.xpath("//input[@placeholder='Alternate Email ID']")).sendKeys(altrEmaiID);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//span[@class='caret']/parent::button")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//button[contains(.,'Active')]")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//form//div[2]//div[10]//div//button[2]")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//li[@class='ng-scope']/a[text()='"+DDPrivilege+"']")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//form//div[2]//div[11]//div//button[2]")).click();
			Thread.sleep(1000);
			driver.findElement(By.partialLinkText(DDRole)).click();
			
			driver.findElement(By.xpath(button_submitEditForm)).click();
			Thread.sleep(2000);
			WaitUtil.waitForElementLocated(By.xpath(text_successMsgOfUsrModified), driver);
			String expectedMsg = "User Successfully Modified.";
			String successMsg = driver.findElement(By.xpath(text_successMsgOfUsrModified)).getText();
			Assert.assertEquals(successMsg, expectedMsg);
			}
			// If any element not found then while filling up form then "add user" form was not closing therefore try catch used here
			catch(Exception e){   
				WaitUtil.waitForElementXPATH("//div[@class='modal-header']//button[@class='close']",driver);
				Actions actions1 =  new Actions(driver);
				actions1.moveToElement(driver.findElement(By.xpath("//div[@class='modal-header']//button[@class='close']"))).click().build().perform();
			}
		}
	}
	
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
	
	@DataProvider
	public Object[][] getUserData(){
		Object userData[][] = new Object[1][13];
		
		//first row - valid Data = Pass
		userData[0][0] = "Associate";		// Privilege filter
		userData[0][1] = "icompass1";		// user id
		userData[0][2] = "efirstName";		// first name
		userData[0][3] = "elastName";		//last n
		userData[0][4] = "eTest@gmail.com";	// email id 			
		userData[0][5] = "111111";			// password
		userData[0][6] = "111111";			// conform password
		userData[0][7] = "9365236589";		// phone no 
		userData[0][8] = "eTest1@gmail.com";// alternate email id
		userData[0][9] = "Active";			// status
		userData[0][10] = "Associate";		// privilege
		userData[0][11] = "Unit";			//role
		userData[0][12] = "";				// message
		
		return userData;
	}
}
