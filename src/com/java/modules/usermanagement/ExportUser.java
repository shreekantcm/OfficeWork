package com.java.modules.usermanagement;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.java.common.DriverUtil;
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
import com.java.common.WaitUtil;

public class ExportUser {
	String button_previlegeDropDown =  ReadLocatorsFromExcel.readExcel("button_previlegeDropDown","ManageUser");
	String button_Go = ReadLocatorsFromExcel.readExcel("button_Go", "ManageUser");
	String widget_export = ReadLocatorsFromExcel.readExcel("widget_export", "ManageUser");
	
	String browserName = ReadPropertiesFile.getPropValues("drivername");
	
	String projectPath = System.getProperty("user.dir");
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
	public void clickEdit() throws InterruptedException, IOException{
		WaitUtil.waitForElementToBeClickable(driver,By.className(button_previlegeDropDown),30);
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.className(button_previlegeDropDown))).click().build().perform();
		driver.findElement(By.xpath("//ul[@id='ui-id-1']//li[text()='SubAdmin']")).click();
		actions.moveToElement(driver.findElement(By.id(button_Go))).click().build().perform();
		
		Thread.sleep(3000);
		actions.moveToElement(driver.findElement(By.xpath(widget_export))).click().build().perform();
		Thread.sleep(8000);
		if(browserName.equalsIgnoreCase("firefox")){
		Runtime.getRuntime().exec(projectPath+"/resource/autoIT/expotfileRecordingFireFox.exe");
		Thread.sleep(9000);
		Runtime.getRuntime().exec(projectPath+"/resource/autoIT/CloseExcelsheet.exe");
		}
		else if(browserName.equalsIgnoreCase("ie32")){
			Runtime.getRuntime().exec(projectPath+"/resource/autoIT/expotfileRecordingIE.exe");
		}
	}
	@AfterTest
	public void closeDriver(){
		driver.quit();
	}
}
