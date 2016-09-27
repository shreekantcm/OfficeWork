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
import com.java.common.ReadExcelFile;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;

public class ModuleAssignmentToPrivilege {
	
	String list_allPrivilege = ReadLocatorsFromExcel.readExcel("list_allPrivilege", "MangPrivilegesAndModl");
	String list_allAvailModule = ReadLocatorsFromExcel.readExcel("list_allAvailModule", "MangPrivilegesAndModl");
	
	String list_assignedModules = ReadLocatorsFromExcel.readExcel("list_assignedModules", "MangPrivilegesAndModl");
	String div_assignModules = ReadLocatorsFromExcel.readExcel("div_assignModules", "MangPrivilegesAndModl");
	String button_apply = ReadLocatorsFromExcel.readExcel("button_apply", "MangPrivilegesAndModl");
	String text_msg_successInAssMod = ReadLocatorsFromExcel.readExcel("text_msg_successInAssMod", "MangPrivilegesAndModl");
	
	public static WebDriver driver;
	String drivername = ReadPropertiesFile.getPropValues("drivername");
	@Test(priority=1)
	public void initializedDriver(){
		if(drivername.equalsIgnoreCase("chrome") || drivername.equalsIgnoreCase("ie32")){
			Assert.assertTrue(false, "This test case only support to Mozila because drag " +
					"and drop not works for Chrome and IE");
		}
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		driver = DriverUtil.getDriver(driver,drivername);
	}
	
	@Test(priority=2,dependsOnMethods="initializedDriver")
	public void userLogin(){
		String username = ReadPropertiesFile.getPropValues("username");
		String password = ReadPropertiesFile.getPropValues("password");
		LoginUtil.userLogin(username, password,driver);
	}
	
	@Test(priority=3,dependsOnMethods="userLogin")
	public void navigateOnPage(){
		NavigationUtil.navigationOnSubMenu("User","Privilege",driver);
	}
	
	@Test(dataProvider="getData",priority=4,dependsOnMethods="navigateOnPage")
	public void assignPrivilege(String newPrivilege,String availModule1,String availModule2,String availModule3) throws InterruptedException{
		
		NavigationUtil.navigationOnSubMenu("User","Privilege",driver);
		//code will search newly added privilege by comparing it with all privilege
		boolean foundPrivilege = false;
		List<WebElement> allPrivilege= driver.findElements(By.xpath(list_allPrivilege));
		for(WebElement allPrvg:allPrivilege){
			if(allPrvg.getText().equals(newPrivilege)){
				//Thread.sleep(3000);
				allPrvg.click();
				foundPrivilege=true;
				break;
			}
		}
		if(!foundPrivilege){
			AddPrvilegeUtil addPrivilege = new AddPrvilegeUtil();
			addPrivilege.addPrivilege(newPrivilege,"PrivilegeDescription","Privilege Successfully Added.",driver);
			
			List<WebElement> afterAddedNewPriv = driver.findElements(By.xpath(list_allPrivilege));
			for(WebElement addedNewPriv:afterAddedNewPriv){
				if(addedNewPriv.getText().equals(newPrivilege)){
					addedNewPriv.click();
					foundPrivilege=true;
				}
			}
		}
		
		Assert.assertTrue(foundPrivilege, "Privilege ["+newPrivilege+"] which you want to assign module is not present.First add in privileg...");
		
		List<WebElement> allModuleName = driver.findElements(By.xpath(list_allAvailModule));
		for(WebElement moduleName : allModuleName){
			
			if(moduleName.getText().equals(availModule1) || moduleName.getText().equals(availModule2) ||
					moduleName.getText().equals(availModule3)){
			Actions action = new Actions(driver);
			Thread.sleep(3000);//don't remove this 
			action.dragAndDrop(moduleName, driver.findElement(By.xpath(div_assignModules))).build().perform();
			}
		}
		driver.findElement(By.xpath(button_apply)).click();
		Thread.sleep(2000);//don't remove this 
		String actualSuccessMsg = driver.findElement(By.xpath(text_msg_successInAssMod)).getText();
		String expecteSuccessMsg = "Privileges has been changed successfully.";
		Assert.assertEquals(actualSuccessMsg, expecteSuccessMsg, "Success Message is not matching");
	}
	
	//Method will verify modules are assign or not 
	@Test(dataProvider="getData",priority=5,dependsOnMethods="assignPrivilege")
	public void verifyPrivilegeAssignProperly(String newPrivilege,String availModule1,String availModule2,String availModule3) throws InterruptedException{
		NavigationUtil.navigationOnSubMenu("User","Privilege",driver);
		boolean foundPrivilege=false;
		List<WebElement> allPrivilege= driver.findElements(By.xpath(list_allPrivilege));
		for(WebElement allPrvg:allPrivilege){
			if(allPrvg.getText().equals(newPrivilege)){
				//Thread.sleep(3000);
				allPrvg.click();
				foundPrivilege=true;
				break;
			}
		}
		Assert.assertTrue(foundPrivilege, "Privilege ["+newPrivilege+"] is not present...");
		Thread.sleep(2000);
		List<WebElement> allAssignModule = driver.findElements(By.xpath(list_assignedModules));
		boolean resultOfModule1 = false;
		boolean resultOfModule2 = false;
		boolean resultOfModule3=false;
		for(WebElement assignModule:allAssignModule){
			if(assignModule.getText().equals(availModule1)){
				resultOfModule1 = true;
			}
			if(assignModule.getText().equals(availModule2)){
				resultOfModule2 = true;
			}
			if(assignModule.getText().equals(availModule3)){
				resultOfModule3 = true;
			}
		}
		if(resultOfModule1==false || resultOfModule2==false || resultOfModule3==false){
			Assert.assertTrue(false, "Some Modules are not assign to the privilege...");
		}
	}
	@AfterTest
	public void closeDriver(){
		driver.quit();
	}
	@DataProvider
	public Object[][] getData() throws IOException{
		ReadExcelFile readExcelFile = new ReadExcelFile();
			String filePath ="/resource/testData";
			String fileName= "ManagePrivilegesAndModules.xlsx";
			String sheetName ="assignModules";
		Object userData[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return userData;
	}
}
