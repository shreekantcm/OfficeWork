package com.java.modules.PrivilegeManage;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.java.common.DriverUtil;
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadExcelFile;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
import com.java.common.WaitUtil;

public class AddPrivilege {
	
	static String widget_add = ReadLocatorsFromExcel.readExcel("widget_add", "MangPrivilegesAndModl");
	static String widget_delete = ReadLocatorsFromExcel.readExcel("widget_delete", "MangPrivilegesAndModl");
	
	static String input_privilege = ReadLocatorsFromExcel.readExcel("input_privilege", "MangPrivilegesAndModl");
	static String input_privDesciprtion = ReadLocatorsFromExcel.readExcel("input_privDesciprtion", "MangPrivilegesAndModl");
	
	static String links_allPrivilege = ReadLocatorsFromExcel.readExcel("links_allPrivilege", "MangPrivilegesAndModl");
	
	static String modelFooter = ReadLocatorsFromExcel.readExcel("modelFooter", "MangPrivilegesAndModl");
	
	static String button_submitAddPriv = ReadLocatorsFromExcel.readExcel("button_submitAddPriv", "MangPrivilegesAndModl");
	static String button_closeAddPrivForm = ReadLocatorsFromExcel.readExcel("button_closeAddPrivForm", "MangPrivilegesAndModl");
	
	static String text_successMsgAddPriv = ReadLocatorsFromExcel.readExcel("text_successMsgAddPriv", "MangPrivilegesAndModl");
	static String text_errorMsg = ReadLocatorsFromExcel.readExcel("text_errorMsg", "MangPrivilegesAndModl");
	static String text_duplicateErrorMsg = ReadLocatorsFromExcel.readExcel("text_duplicateErrorMsg", "MangPrivilegesAndModl");
	static String text_deletePrivConfrmationMsg = ReadLocatorsFromExcel.readExcel("text_deletePrivConfrmationMsg",
			"MangPrivilegesAndModl");
	
	static String list_allPrivilege = ReadLocatorsFromExcel.readExcel("list_allPrivilege", "MangPrivilegesAndModl");
	//String button_submitAddPriv = ReadLocatorsFromExcel.readExcel("button_submitAddPriv", "MangPrivilegesAndModl");
	//String button_submitAddPriv = ReadLocatorsFromExcel.readExcel("button_submitAddPriv", "MangPrivilegesAndModl");
	
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
		NavigationUtil.navigationOnSubMenu("User","Privilege",driver);
	}
	
	@Test(dataProvider="getData", priority=4)
	public void addPrivilege(String privilege,String privilegeDescription,String message) throws InterruptedException{
		//System.out.println(privilege+"  "+privilegeDescription+"  "+message);
		
		if(message.equalsIgnoreCase("Privilege Successfully Added.")){
			String addPrivilege = "AutomationPrivilege";
			
			boolean foundPrivilege = false;
			Thread.sleep(1000);
			List<WebElement> allPrivilege= driver.findElements(By.xpath(links_allPrivilege));
			for(WebElement allPrvg:allPrivilege){
				if(allPrvg.getText().equals(addPrivilege)){
					Thread.sleep(1000);
					allPrvg.click();
					foundPrivilege=true;
				}
			}
			if(foundPrivilege){
				Actions action = new Actions(driver);
				Thread.sleep(1000);
				action.moveToElement(driver.findElement(By.xpath(widget_delete))).click().build().perform();
				WaitUtil.waitForElementLocated(By.xpath(text_deletePrivConfrmationMsg), driver);
				Thread.sleep(2000);
				String deletingPrivilegeconfarm = driver.findElement(By.xpath(text_deletePrivConfrmationMsg)).getText();
				int initialIndex = deletingPrivilegeconfarm.indexOf("privilege-");
				int newIndex = initialIndex+11;
				String deletePrivilegeOnConfrmPopup = deletingPrivilegeconfarm.substring(newIndex, deletingPrivilegeconfarm.indexOf("?"));
				
				if(deletePrivilegeOnConfrmPopup.trim().equals(addPrivilege)){
					Thread.sleep(1000);
					driver.findElement(By.xpath("//button[@ng-click='deleteRole()']")).click();
					Thread.sleep(1000);
					String actualMsg = driver.findElement(By.xpath("//span[contains(.,'Deleted Successfully.')]")).getText();
					String expectedMsg = "Privilege Has Been Deleted Successfully.";
					Assert.assertEquals("Privilege not deleted succesfully", expectedMsg, actualMsg);
				}else{
					Assert.assertTrue("Privilege ["+addPrivilege+"] which you want to delete is not matching with the " +
							"Privilege ["+deletePrivilegeOnConfrmPopup+"] which is diplaying on delete privilege confarmation popup...", false);
				}
			}
		}
		
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@class='widget1']//img")).click();
		Thread.sleep(3000);
		driver.findElement(By.id(input_privilege)).sendKeys(privilege);
		driver.findElement(By.id(input_privDesciprtion)).sendKeys(privilegeDescription);
		driver.findElement(By.xpath(modelFooter)).click();
		WebElement submitButton  =driver.findElement(By.id(button_submitAddPriv));
		if(submitButton.isEnabled()){
			submitButton.click();
			String expectedSuccessMsg = "Privilege Successfully Added.";
			if(message.equals(expectedSuccessMsg)){
				String actualSuccessMsg = driver.findElement(By.xpath(text_successMsgAddPriv)).getText();
				Assert.assertTrue("Privilege not added in the database May be because of duplicate privilege",actualSuccessMsg.equals(expectedSuccessMsg));
			}else{
				Thread.sleep(3000);
				String duplicateUserErrorMsg = driver.findElement(By.xpath(text_duplicateErrorMsg)).getText();
				driver.findElement(By.xpath(button_closeAddPrivForm)).click();
				// If error message diplaying fine then the no issue otherwise assertion fails
				Assert.assertTrue("Duplicate Login Name - Error message not matching",message.equals(duplicateUserErrorMsg));
			}
		}else{
			Thread.sleep(3000);
			String actualErrorMsg = driver.findElement(By.xpath(text_errorMsg)).getText();
			driver.findElement(By.xpath(button_closeAddPrivForm)).click();
			Thread.sleep(3000);
			Assert.assertTrue("Error message does not matched",actualErrorMsg.equals(message));
		}
	}
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
	
	@DataProvider
	public Object[][] getData() throws IOException{
		ReadExcelFile readExcelFile = new ReadExcelFile();
			String filePath ="/resource/testData";
			String fileName= "ManagePrivilegesAndModules.xlsx";
			String sheetName ="addPrivilege";
		Object userData[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return userData;
	}
}
