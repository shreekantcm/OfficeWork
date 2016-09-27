package com.java.modules.AddQuestions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.java.common.Log;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.WaitUtil;

public class CommanFunctions {
	static String button_OpenAddSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenAddSkillForm", "AddQuestion");
	static String button_CloseAddSkillForm = ReadLocatorsFromExcel.readExcel("button_CloseAddSkillForm", "AddQuestion");
	
	static String input_learningRefUrl = ReadLocatorsFromExcel.readExcel("input_learningRefUrl","AddQuestion");
	static String input_skill = ReadLocatorsFromExcel.readExcel("input_skill", "AddQuestion");
	static String input_skillDescription = ReadLocatorsFromExcel.readExcel("input_skillDescription", "AddQuestion");
	
	static String links_allSkill = ReadLocatorsFromExcel.readExcel("links_allSkill", "AddQuestion");
	static String text_ErrorMessageDuplicate = ReadLocatorsFromExcel.readExcel("text_ErrorMessageDuplicate", "AddQuestion");
	static String text_ErrorMessage = ReadLocatorsFromExcel.readExcel("text_ErrorMessage", "AddQuestion");
	static String text_skillAddedSuccesfMsg = ReadLocatorsFromExcel.readExcel("text_skillAddedSuccesfMsg", "AddQuestion");
	
	// This function commonly used to add skill or subskil or topic 
	public void addSkillOrSubSkillOrTopic(String element, String elementDescription, String message,By locatorOpenForm,By locatorSubmitForm,WebDriver driver) throws InterruptedException{
		// Open form 
		Log.info("START FUNCTION : CommanFunctions.addSkillOrSubSkillOrTopic() will add element ="+element+"in DB"); 
		driver.findElement(locatorOpenForm).click();
		Log.info("Form opened to add element ={"+element+"} in DB");
		WaitUtil.waitForElementLocated(By.xpath(input_skill),driver);
		driver.findElement(By.xpath(input_skill)).sendKeys(element);
		driver.findElement(By.xpath(input_skillDescription)).sendKeys(elementDescription);
		driver.findElement(By.xpath(input_skill)).click();
		driver.findElement(By.xpath(input_skillDescription)).click();
		if(message.equals("Suggested Learning is successfully added.")){
			WaitUtil.getElement(driver, By.id(input_learningRefUrl), 5).sendKeys("https://www.google.co.in");
		}
		Thread.sleep(1000);		
		WebElement submitButton = driver.findElement(locatorSubmitForm);
		Thread.sleep(1000);	
		if(submitButton.isEnabled()){
				submitButton.click();
				Thread.sleep(3000);
				WaitUtil.waitForElementLocated(By.xpath(text_skillAddedSuccesfMsg),driver);
				String actualSuccessMsg = driver.findElement(By.xpath(text_skillAddedSuccesfMsg)).getText();
				Assert.assertTrue("Skill or sub skill or topic is not added in the database "
						,actualSuccessMsg.equals(message));
				Log.info("Form Submitted succesfully");
		}else{
			Assert.assertTrue("Submit button is not enabled",false);
		}
		Log.info("END FUNCTION : addSkillOrSubSkillOrTopic()"); 
	}
}
