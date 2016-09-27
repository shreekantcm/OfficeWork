package com.java.modules.PrivilegeManage;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.java.common.AddPrvilegeUtil;
import com.java.common.DriverUtil;
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;

public class EditPrivilege {
	String list_allPrivilege = ReadLocatorsFromExcel.readExcel("list_allPrivilege", "MangPrivilegesAndModl");
	String widget_edit = ReadLocatorsFromExcel.readExcel("widget_edit", "MangPrivilegesAndModl");
	String text_selectPrivBeforeEditMsg = ReadLocatorsFromExcel.readExcel("text_selectPrivBeforeEditMsg", "MangPrivilegesAndModl");
	String div_model = ReadLocatorsFromExcel.readExcel("div_model", "MangPrivilegesAndModl");
	String text_successModifiedPriv = ReadLocatorsFromExcel.readExcel("text_successModifiedPriv", "MangPrivilegesAndModl");
	String text_duplicateErrorMsg = ReadLocatorsFromExcel.readExcel("text_duplicateErrorMsg", "MangPrivilegesAndModl");
	String button_closeAddPrivForm = ReadLocatorsFromExcel.readExcel("button_closeAddPrivForm", "MangPrivilegesAndModl");
	String text_errorMsg = ReadLocatorsFromExcel.readExcel("text_errorMsg", "MangPrivilegesAndModl");
	
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
	
	@Test(priority=4)
	public void VerifySelecePrivilegeMsgDisplayed() throws InterruptedException{
		Thread.sleep(1000);
		driver.findElement(By.xpath(widget_edit)).click();
		String expectedMsg = "Please Select the Role Before Edit";
		String actualMsg = driver.findElement(By.xpath(text_selectPrivBeforeEditMsg)).getText();
		Assert.assertEquals(actualMsg, expectedMsg);
	}
	
	@Test(priority=5,dataProvider="getData")
	public void editPrivilege(String privilege, String privilegeDescription,String message) throws InterruptedException{
		NavigationUtil.navigationOnSubMenu("User","Privilege",driver);
		
		Actions actions = new Actions(driver);
		String privilegeEdit = "editPrivilege"; 
		boolean foundPrivilege = false;
		Thread.sleep(1000);
		List<WebElement> allPrivilege= driver.findElements(By.xpath(list_allPrivilege));
		for(WebElement allPrvg:allPrivilege){
			if(allPrvg.getText().equals(privilegeEdit)){
				Thread.sleep(1000);
				actions.moveToElement(allPrvg).click().build().perform();
				foundPrivilege=true;
				break;
			}
		}
		
		if(!foundPrivilege){
			AddPrvilegeUtil addPrivilege = new AddPrvilegeUtil();
			addPrivilege.addPrivilege(privilegeEdit,"PrivilegeDescription","Privilege Successfully Added.",driver);
			Actions actions1 = new Actions(driver);
			Thread.sleep(1000);
			List<WebElement> afterAddedNewPriv = driver.findElements(By.xpath(list_allPrivilege));
			for(WebElement addedNewPriv:afterAddedNewPriv){
				if(addedNewPriv.getText().equals(privilegeEdit)){
					Thread.sleep(1000);
					actions1.moveToElement(addedNewPriv).click().build().perform();
					foundPrivilege=true;
				}
			}
		}
		
		Assert.assertTrue(foundPrivilege, "Privilege ["+privilegeEdit+"] which you want to EDIT is not present.First add in privileg...");
		Thread.sleep(1000);
		driver.findElement(By.xpath(widget_edit)).click();
		driver.findElement(By.id("roleNameId")).clear();
		driver.findElement(By.id("roleNameId")).sendKeys(privilege);
		driver.findElement(By.id("descriptionId")).clear();
		driver.findElement(By.id("descriptionId")).sendKeys(privilegeDescription);
		//driver.findElement(By.id("submitRole")).click();
		driver.findElement(By.xpath(div_model)).click();
		driver.findElement(By.id("roleNameId")).click();
		driver.findElement(By.id("descriptionId")).click();
		Thread.sleep(1000);
		WebElement submitButton  = driver.findElement(By.id("submitRole"));
		if(submitButton.isEnabled()){
			Thread.sleep(1000);
			submitButton.click();
			Thread.sleep(3000); // dont delete this time
			String expectedSuccessMsg = "Privilege Successfully Modified";
			if(message.equals(expectedSuccessMsg)){
				Thread.sleep(1000);
				String actualSuccessMsg = driver.findElement(By.xpath(text_successModifiedPriv)).getText();
				Assert.assertTrue(actualSuccessMsg.equals(expectedSuccessMsg),"Privilege not added in the database May be because you trying to pass duplicate privilege");
			}else{
				Thread.sleep(3000);
				String duplicateUserErrorMsg = driver.findElement(By.xpath(text_duplicateErrorMsg)).getText();
				Thread.sleep(1000);
				driver.findElement(By.xpath(button_closeAddPrivForm)).click();
				// If error message diplaying fine then the no issue otherwise assertion fails
				//System.out.println("duplicateUserErrorMsg="+duplicateUserErrorMsg);
				//System.out.println("message**************="+message);
				Assert.assertTrue(message.equals(duplicateUserErrorMsg),"Duplicate Login Name - Error message not matching");
			}
		}else{
			Thread.sleep(3000); //Dont delete this entry
			String actualErrorMsg = driver.findElement(By.xpath(text_errorMsg)).getText();
			//System.out.println("actualErrorMsg = "+actualErrorMsg);
			//System.out.println("message = "+message);
			Thread.sleep(1000);
			driver.findElement(By.xpath(button_closeAddPrivForm)).click();
			Assert.assertTrue(actualErrorMsg.equals(message),"Error message does not matched");
		}
	}
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
	@DataProvider
	public Object[][] getData() throws IOException{
		Object data[][] = new Object[6][3];
		
		//first Row
		data[0][0]="editPrivilege";
		data[0][1]="Test Privilege Description";
		data[0][2]="Privilege Successfully Modified";
		//Second Row
		data[1][0]="         ";
		data[1][1]="Test Privilege Description";
		data[1][2]="This is not valid Privilege";
		//Third Row
		data[2][0]="";
		data[2][1]="Test Privilege Description";
		data[2][2]="Privilege is required";
		//Fourth row
		data[3][0]="SubAdmin";
		data[3][1]="Test Privilege Description";
		data[3][2]="Duplicate/ Invalid Entry. Please Enter New Privilege Name."; // you need to check this message because currently it is not appearing in application
		//fifth row
		data[4][0]="mn";
		data[4][1]="Test Privilege Description";
		data[4][2]="This is not valid Privilege";
		//Six row
		data[5][0]="SpecialSym#$";
		data[5][1]="Test Privilege Description";
		data[5][2]="This is not valid Privilege";

		return data;
	}
}
