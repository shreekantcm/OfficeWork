package com.java.modules.usermanagement;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class DeleteUser {
	
	String button_previlegeDropDown = ReadLocatorsFromExcel.readExcel("button_previlegeDropDown","ManageUser");
	String button_Go = ReadLocatorsFromExcel.readExcel("button_Go", "ManageUser");
	String button_selectUserBeforeDelete = ReadLocatorsFromExcel.readExcel("button_selectUserBeforeDelete","ManageUser");
	String button_deleteButOnConfPopup = ReadLocatorsFromExcel.readExcel("button_deleteButOnConfPopup","ManageUser");
	String button_closeButOnConfPopup = ReadLocatorsFromExcel.readExcel("button_closeButOnConfPopup","ManageUser");
	
	String input_search = ReadLocatorsFromExcel.readExcel("input_search","ManageUser");
	
	String widget_delete = ReadLocatorsFromExcel.readExcel("widget_delete", "ManageUser");
	
	String ddLink_associate = ReadLocatorsFromExcel.readExcel("ddLink_associate","ManageUser");
	
	String text_selectUserBeforeDelete = ReadLocatorsFromExcel.readExcel("text_selectUserBeforeDelete","ManageUser");
	String text_deleteUserNameOnPopup = ReadLocatorsFromExcel.readExcel("text_deleteUserNameOnPopup","ManageUser");
	String text_deleteUserSuccessMsg = ReadLocatorsFromExcel.readExcel("text_deleteUserSuccessMsg","ManageUser"); 
	
	public static int count=1;
	public static WebDriver driver;
	Actions actions;
	@Test(priority=1)
	public void initializedDriver(){
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		driver = DriverUtil.getDriver(driver,drivername);
		actions = new Actions(driver);
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
	
	// If user click on Delete button without selected user then should display the error msg = Pass
	// In finally used to closed the error message
	@Test(priority=4)
	public void checkErrorMessageWhenUserNotSelected() throws InterruptedException{
		Thread.sleep(9000);
		WaitUtil.waitForElementToBeClickable(driver,By.className(button_previlegeDropDown),20);
		
		Thread.sleep(1000);
		actions.moveToElement(driver.findElement(By.className(button_previlegeDropDown))).click().build().perform();
		driver.findElement(By.xpath(ddLink_associate)).click();
		driver.findElement(By.id(button_Go)).click();
		Thread.sleep(5000);
		actions.moveToElement(driver.findElement(By.xpath(widget_delete))).click().build().perform();
		WaitUtil.waitForElementXPATH(text_selectUserBeforeDelete, driver);
		Thread.sleep(1000);
		String actualErrorMsg = driver.findElement(By.xpath(text_selectUserBeforeDelete)).getText();
		String expectedErrorMsg = "Select the User Before delete !";
		try{
		Assert.assertEquals(expectedErrorMsg, actualErrorMsg);
		}catch(Exception e){
			System.out.println(e);
		}finally{
			driver.findElement(By.xpath(button_selectUserBeforeDelete)).click();
		}
	}
	
	
	@Test(priority=5 , dataProvider="getDataOfDeleteUSer")
	public void deleteUser(String userId,String firstName,String lastName,String emailID,String password,
			String confPassword,String mobileNo,String altrEmaiID,String status,String privilege,String 
			role,String msg) throws InterruptedException{
		boolean searchResultUser = false;
				
		//User which we are displayed and going to delete will be stored in this arraylist
		ArrayList<String> deletingUser = new ArrayList<String>();
		//User which are displaying on Delete user confirmation pop up
		ArrayList<String> deleteUserConfirmPopup = new ArrayList<String>();
		//Thread.sleep(5000);
		driver.findElement(By.id(input_search)).clear();
		driver.findElement(By.id(input_search)).sendKeys(userId);
		try{
			Thread.sleep(1000);
		searchResultUser = driver.findElement(By.xpath("//tr//td[contains(text(),'"+userId+"')]")).isDisplayed();
		}
		catch(Exception e){
			//System.out.println("Exception Catch");
		}
		if(searchResultUser){
			Thread.sleep(1000);
			driver.findElement(By.xpath("//tr//td[contains(text(),'"+userId+"')]")).click();
			Thread.sleep(2000);
			// Adding the user which we had clicked for delete
			deletingUser.add(userId);
		}else{
			actions.moveToElement(driver.findElement(By.className(button_previlegeDropDown))).build().perform();
			Thread.sleep(2000);
			AddUserUtil.addUser(userId, firstName, lastName, emailID, password, confPassword, mobileNo, altrEmaiID, status, privilege, role, msg,driver);
			//Thread.sleep(5000);
			driver.findElement(By.id(input_search)).clear();
			driver.findElement(By.id(input_search)).sendKeys(userId);
			driver.findElement(By.xpath("//tr//td[contains(text(),'"+userId+"')]")).click();
			deletingUser.add(userId);
		}
		
		driver.findElement(By.xpath(widget_delete)).click();
		List<WebElement> deleteUsers= driver.findElements(By.xpath(text_deleteUserNameOnPopup));
		for(WebElement deleteUsr : deleteUsers){
			System.out.println("="+deleteUsr.getText()+"\n");
			String temp = deleteUsr.getText().substring(0, deleteUsr.getText().indexOf(" "));
			deleteUserConfirmPopup.add(temp);
		}
		
		if(deleteUserConfirmPopup.containsAll(deletingUser)){
			//Thread.sleep(5000);
			driver.findElement(By.xpath(button_deleteButOnConfPopup)).click();
			String expectedMsg = "User deleted successfully.";
			Thread.sleep(2000);
			WaitUtil.waitForElementToBeClickable(driver,By.xpath(text_deleteUserSuccessMsg),10);
			Thread.sleep(5000);
			String actualMsg = driver.findElement(By.xpath(text_deleteUserSuccessMsg)).getText();
			Assert.assertEquals(expectedMsg, actualMsg);
		}else{
			Thread.sleep(1000);
			driver.findElement(By.xpath(button_closeButOnConfPopup)).click();
			Assert.assertTrue("User which are going to delete are not matching with selected user", false);
		}
	}
	
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
	
	@DataProvider
	public Object[][] getDataOfDeleteUSer(){
		Object delteUserData[][] = new Object[1][12];
		
		//first row - valid Data = Pass
		delteUserData[0][0] = "UID.-_1";		// dont change the value it is hardcoded
		delteUserData[0][1] = "firstName";		
		delteUserData[0][2] = "lastName";
		delteUserData[0][3] = "deleteUser1@hmail.com";
		delteUserData[0][4] = "123456";
		delteUserData[0][5] = "123456";
		delteUserData[0][6] = "9999999999";
		delteUserData[0][7] = "deleteUser2@hmail.com";
		delteUserData[0][8] = "Active";
		delteUserData[0][9] = "Associate";
		delteUserData[0][10] = "Mainframe";
		delteUserData[0][11] = "User successfully added.";
		
		return delteUserData;
	}
}
