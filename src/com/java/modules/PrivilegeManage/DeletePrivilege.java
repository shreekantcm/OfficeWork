package com.java.modules.PrivilegeManage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.java.common.AddPrvilegeUtil;
import com.java.common.DriverUtil;
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
import com.java.common.WaitUtil;

public class DeletePrivilege {
	public static WebDriver driver;
	
	static String widget_delete = ReadLocatorsFromExcel.readExcel("widget_delete", "MangPrivilegesAndModl");
	static String text_errMsgSelectPrivBeforeDelete = ReadLocatorsFromExcel.readExcel
			("text_errMsgSelectPrivBeforeDelete", "MangPrivilegesAndModl");
	static String links_allPrivilege = ReadLocatorsFromExcel.readExcel("links_allPrivilege", "MangPrivilegesAndModl");
	static String text_deletePrivConfrmationMsg = ReadLocatorsFromExcel.readExcel("text_deletePrivConfrmationMsg",
			"MangPrivilegesAndModl");
	
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
	
	// Method will check error message display when user click on delete button without selected privilege
	@Test(priority=4)
	public void veritySelectPrivilegeMsg() throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(By.xpath(widget_delete)).click();
		String expectedMsg = "Please Select the Role Before Delete";
		Thread.sleep(1000);
		String actualMsg = driver.findElement(By.xpath(text_errMsgSelectPrivBeforeDelete)).getText();
		Assert.assertEquals(actualMsg, expectedMsg);
	}
	
	// Method check if element which you want to delete is present or not
	// If not present then it will create first and then delete
	@Test(priority=5)
	public void checkIfDeletePrivilegePresent() throws InterruptedException{
		String deletePrivilege = "AutomationPrivilege";
		
		boolean foundPrivilege = false;
		Thread.sleep(1000);
		List<WebElement> allPrivilege= driver.findElements(By.xpath(links_allPrivilege));
		for(WebElement allPrvg:allPrivilege){
			if(allPrvg.getText().equals(deletePrivilege)){
				Thread.sleep(1000);
				allPrvg.click();
				foundPrivilege=true;
			}
		}
		if(!foundPrivilege){
			AddPrvilegeUtil addPrivilege = new AddPrvilegeUtil();
			addPrivilege.addPrivilege(deletePrivilege,"PrivilegeDescription","Privilege Successfully Added.",driver);
			Thread.sleep(1000);
			List<WebElement> afterAddedNewPriv = driver.findElements(By.xpath(links_allPrivilege));
			for(WebElement addedNewPriv:afterAddedNewPriv){
				if(addedNewPriv.getText().equals(deletePrivilege)){
					Thread.sleep(1000);
					addedNewPriv.click();
					foundPrivilege=true;
				}
			}
		}
		Assert.assertTrue(foundPrivilege, "Privilege ["+deletePrivilege+"] which you want to delete is not added succesfully...");
	}
	
	@Test(priority=6,dependsOnMethods="checkIfDeletePrivilegePresent")
	public void deletePrivilege() throws InterruptedException{
		String deletePrivilege = "AutomationPrivilege";
		Actions action = new Actions(driver);
		Thread.sleep(1000);
		action.moveToElement(driver.findElement(By.xpath(widget_delete))).click().build().perform();
		WaitUtil.waitForElementLocated(By.xpath(text_deletePrivConfrmationMsg), driver);
		Thread.sleep(2000);
		String deletingPrivilegeconfarm = driver.findElement(By.xpath(text_deletePrivConfrmationMsg)).getText();
		//System.out.println("deletingPrivilegeconfarm"+deletingPrivilegeconfarm);
		int initialIndex = deletingPrivilegeconfarm.indexOf("privilege-");
		int newIndex = initialIndex+11;
		//System.out.println("initialIndex"+initialIndex);
		String deletePrivilegeOnConfrmPopup = deletingPrivilegeconfarm.substring(newIndex, deletingPrivilegeconfarm.indexOf("?"));
		
		if(deletePrivilegeOnConfrmPopup.trim().equals(deletePrivilege)){
			Thread.sleep(1000);
			driver.findElement(By.xpath("//button[@ng-click='deleteRole()']")).click();
			Thread.sleep(1000);
			String actualMsg = driver.findElement(By.xpath("//span[contains(.,'Deleted Successfully.')]")).getText();
			String expectedMsg = "Privilege Has Been Deleted Successfully.";
			Assert.assertEquals(actualMsg, expectedMsg,"Privilege not deleted succesfully");
		}else{
			Assert.assertTrue(false, "Privilege ["+deletePrivilege+"] which you want to delete is not matching with the " +
					"Privilege ["+deletePrivilegeOnConfrmPopup+"] which is diplaying on delete privilege confarmation popup...");
		}
	}
	
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
}
