package com.java.common;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AddPrvilegeUtil {
	
	String widget_add = ReadLocatorsFromExcel.readExcel("widget_add", "MangPrivilegesAndModl");
	
	String input_privilege = ReadLocatorsFromExcel.readExcel("input_privilege", "MangPrivilegesAndModl");
	String input_privDesciprtion = ReadLocatorsFromExcel.readExcel("input_privDesciprtion", "MangPrivilegesAndModl");
	
	String modelFooter = ReadLocatorsFromExcel.readExcel("modelFooter", "MangPrivilegesAndModl");
	
	String button_submitAddPriv = ReadLocatorsFromExcel.readExcel("button_submitAddPriv", "MangPrivilegesAndModl");
	String button_closeAddPrivForm = ReadLocatorsFromExcel.readExcel("button_closeAddPrivForm", "MangPrivilegesAndModl");
	
	String text_successMsgAddPriv = ReadLocatorsFromExcel.readExcel("text_successMsgAddPriv", "MangPrivilegesAndModl");
	String text_errorMsg = ReadLocatorsFromExcel.readExcel("text_errorMsg", "MangPrivilegesAndModl");
	String text_duplicateErrorMsg = ReadLocatorsFromExcel.readExcel("text_duplicateErrorMsg", "MangPrivilegesAndModl");
	
	public void addPrivilege(String privilege,String privilegeDescription,String message,WebDriver driver) throws InterruptedException{
		//System.out.println(privilege+"  "+privilegeDescription+"  "+message);
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
				try{
				String actualSuccessMsg = driver.findElement(By.xpath(text_successMsgAddPriv)).getText();
				Assert.assertTrue("Privilege not added in the database May be because of duplicate privilege",actualSuccessMsg.equals(expectedSuccessMsg));
				}finally{
					driver.findElement(By.xpath(button_closeAddPrivForm)).click();
				}
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
			//System.out.println("actualErrorMsg = "+actualErrorMsg);
			//System.out.println("message = "+message);
			try{
					Assert.assertTrue("Error message does not matched",actualErrorMsg.equals(message));
				}
			finally{
				driver.findElement(By.xpath(button_closeAddPrivForm)).click();
			}
		}
	}
}
