package com.java.modules.usermanagement;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.java.common.DriverUtil;
import com.java.common.Log;
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
import com.java.common.WaitUtil;

public class FilterPrivilege {
	
	String text_noRecordFound = ReadLocatorsFromExcel.readExcel("text_noRecordFound","ManageUser");
	String text_userEntriesRecord = ReadLocatorsFromExcel.readExcel("text_userEntriesRecord","ManageUser");
	String text_NoMatchingRecord = ReadLocatorsFromExcel.readExcel("text_NoMatchingRecord","ManageUser");
	
	String button_previlegeDropDown = ReadLocatorsFromExcel.readExcel("button_previlegeDropDown","ManageUser");
	String button_Go = ReadLocatorsFromExcel.readExcel("button_Go","ManageUser");
	
	String input_searchInput = ReadLocatorsFromExcel.readExcel("input_searchInput","ManageUser");
	
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
		NavigationUtil.navigationOnSubMenu("User", "Manage User",driver);
	}
	
	// When Landing on Manage User page there should not be any data on table
	// and message should display as ("No Records Found. Please Try Some Other Combination.")
	// We are validation that message present or not
	@Test(priority = 4)
	public void noRecordFound() throws InterruptedException{
		Thread.sleep(2000);
		String actualMessage = driver.findElement(By.xpath(text_noRecordFound)).getText();
		String expectedMessage = "No Records Found. Please Try Some Other Combination.";
		Assert.assertEquals(expectedMessage, actualMessage);
	}
	
	 // Verify On click of Go button check records are populated or not 
	// By verifying the message "Showing 1 to 10 of 12,237 entries"
	@Test(priority = 5)
	public void displayResult() throws InterruptedException{
		driver.findElement(By.id(button_Go)).click();
		Thread.sleep(3000);
		String text = "1";
		WaitUtil.waitForTextToBePresentInElementXPATH(text_userEntriesRecord,text,driver);
		String showingEntryMsg = driver.findElement(By.xpath(text_userEntriesRecord)).getText();
		String arr[] = showingEntryMsg.split(" ");
		if(Integer.parseInt(arr[1]) == 0 ||  Integer.parseInt(arr[3]) == 0){
			Assert.assertTrue("Application ISSUE, Records are not displaying", false);
		}
	}
	
	// check- Either filter should displayed only selected result or not = Pass
	@Test(priority =6)
	public void verifyAssociate() throws InterruptedException{
		Actions actions=new Actions(driver);
		Thread.sleep(1000);
		actions.moveToElement(driver.findElement(By.className(button_previlegeDropDown))).click().build().perform();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//ul[@id='ui-id-1']//li[text()='Associate']")).click();
		driver.findElement(By.id(button_Go)).click();
		Thread.sleep(2000);
		WaitUtil.waitForElementXPATH(text_userEntriesRecord,driver);
		String showingEntryMsg = driver.findElement(By.xpath(text_userEntriesRecord)).getText();
		Log.info(showingEntryMsg);
		String arr[] = showingEntryMsg.split(" ");
		if(Integer.parseInt(arr[1]) == 0 ||  Integer.parseInt(arr[3]) == 0){
			Assert.assertTrue("Class Name = Filter Privilege::Code not working properly, In ideal condition " +
					"resord should be displayed if it is present for the privilege Associate", false);
		}else{
			driver.findElement(By.id(input_searchInput)).clear();
			driver.findElement(By.id(input_searchInput)).sendKeys("SubAdmin");	
			// Above line should show the message "No matching records found"
			Thread.sleep(1000);
			WaitUtil.waitForElementXPATH(text_NoMatchingRecord, driver);
			String actualMsg1 = driver.findElement(By.xpath(text_NoMatchingRecord)).getText();
			String expectedMsg = "No matching records found";
			
			Assert.assertEquals(expectedMsg, actualMsg1);
			
			driver.findElement(By.id(input_searchInput)).clear();
			driver.findElement(By.id(input_searchInput)).sendKeys("Report Admin");
			WaitUtil.waitForElementXPATH(text_NoMatchingRecord,driver);
			Thread.sleep(1000);
			String actualMsg2 = driver.findElement(By.xpath(text_NoMatchingRecord)).getText();
			Assert.assertEquals(expectedMsg, actualMsg2);
			
			driver.findElement(By.id(input_searchInput)).clear();
			driver.findElement(By.id(input_searchInput)).sendKeys("Bench admin");
			WaitUtil.waitForElementXPATH(text_NoMatchingRecord,driver);
			Thread.sleep(1000);
			String actualMsg3 = driver.findElement(By.xpath(text_NoMatchingRecord)).getText();
			Assert.assertEquals(expectedMsg, actualMsg3);
			driver.findElement(By.id(input_searchInput)).clear();
		}
	}
	
	@Test(priority =7)
	public void verfySubAdmin() throws InterruptedException{
		
		driver.findElement(By.id(input_searchInput)).clear();
		Actions actions=new Actions(driver);
		Thread.sleep(1000);
		actions.moveToElement(driver.findElement(By.className(button_previlegeDropDown))).click().build().perform();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//ul[@id='ui-id-1']//li[text()='SubAdmin']")).click();
		Thread.sleep(1000);
		driver.findElement(By.id(button_Go)).click();
		driver.findElement(By.id(input_searchInput)).clear();
		driver.findElement(By.id(input_searchInput)).sendKeys("  ");	
		WaitUtil.waitForElementXPATH(text_userEntriesRecord,driver);
		Thread.sleep(1000);
		String showingEntryMsg = driver.findElement(By.xpath(text_userEntriesRecord)).getText();
		String arr[] = showingEntryMsg.split(" ");
		if(Integer.parseInt(arr[1]) == 0 ||  Integer.parseInt(arr[3]) == 0){
			Assert.assertTrue("Code not working properly, In ideal condition it should not come in this " +
					"if condition if at least one record is present for the privilege SubAdmin", 1>2);
		}else{
			driver.findElement(By.id(input_searchInput)).clear();
			driver.findElement(By.id(input_searchInput)).sendKeys("Associate");	
			// Above line should show the message "No matching records found"
			Thread.sleep(1000);
			WaitUtil.waitForElementXPATH(text_NoMatchingRecord,driver);
			String actualMsg1 = driver.findElement(By.xpath(text_NoMatchingRecord)).getText();
			String expectedMsg = "No matching records found";
			Assert.assertEquals(expectedMsg, actualMsg1);
			
			driver.findElement(By.id(input_searchInput)).clear();
			driver.findElement(By.id(input_searchInput)).sendKeys("Report Admin");
			Thread.sleep(1000);
			WaitUtil.waitForElementXPATH(text_NoMatchingRecord,driver);
			String actualMsg2 = driver.findElement(By.xpath(text_NoMatchingRecord)).getText();
			Assert.assertEquals(expectedMsg, actualMsg2);
			
			driver.findElement(By.id(input_searchInput)).clear();
			driver.findElement(By.id(input_searchInput)).sendKeys("Bench admin");
			Thread.sleep(1000);
			WaitUtil.waitForElementXPATH(text_NoMatchingRecord,driver);
			String actualMsg3 = driver.findElement(By.xpath(text_NoMatchingRecord)).getText();
			Assert.assertEquals(expectedMsg, actualMsg3);
			driver.findElement(By.id(input_searchInput)).clear();
		}
	}
	
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
}
