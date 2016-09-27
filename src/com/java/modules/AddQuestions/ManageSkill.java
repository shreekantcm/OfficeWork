package com.java.modules.AddQuestions;

import java.awt.AWTException;
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
import com.java.common.Log;
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadExcelFile;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
import com.java.common.WaitUtil;
/**
 * This class will execute multiple test case like Add skill, Edit skill, Delete skill 
 * @author Nandu
 *
 */
public class ManageSkill {
	
	String button_OpenAddSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenAddSkillForm", "AddQuestion");
	String button_SubmitAddSkillForm = ReadLocatorsFromExcel.readExcel("button_SubmitAddSkillForm", "AddQuestion");
	String button_SkilReset = ReadLocatorsFromExcel.readExcel("button_SkilReset", "AddQuestion");
	String button_CloseAddSkillForm = ReadLocatorsFromExcel.readExcel("button_CloseAddSkillForm", "AddQuestion");
	String button_OpenEDITSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenEDITSkillForm", "AddQuestion");
	String button_OpenAddSubSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenAddSubSkillForm", "AddQuestion");
	String button_deleteSkill = ReadLocatorsFromExcel.readExcel("button_deleteSkill", "AddQuestion");
	String button_deleteSkillOnPopup = ReadLocatorsFromExcel.readExcel("button_deleteSkillOnPopup", "AddQuestion");
	
	String input_skill = ReadLocatorsFromExcel.readExcel("input_skill", "AddQuestion");
	String input_skillDescription = ReadLocatorsFromExcel.readExcel("input_skillDescription", "AddQuestion");
	String links_allSkill = ReadLocatorsFromExcel.readExcel("links_allSkill", "AddQuestion");
	String text_SuccessMessage = ReadLocatorsFromExcel.readExcel("text_SuccessMessage", "AddQuestion");
	String text_ErrorMessageDuplicate = ReadLocatorsFromExcel.readExcel("text_ErrorMessageDuplicate", "AddQuestion");
	String text_ErrorMessage = ReadLocatorsFromExcel.readExcel("text_ErrorMessage", "AddQuestion");
	String text_EditSuccessMessage = ReadLocatorsFromExcel.readExcel("text_EditSuccessMessage", "AddQuestion");
	String text_DeleteSuccessMessage = ReadLocatorsFromExcel.readExcel("text_DeleteSuccessMessage", "AddQuestion");
	String text_deleteConfarmationMsg = ReadLocatorsFromExcel.readExcel("text_deleteConfarmationMsg", "AddQuestion");
	String text_skillAddedSuccesfMsg = ReadLocatorsFromExcel.readExcel("text_skillAddedSuccesfMsg", "AddQuestion");
	
	static int timeOutMinimum =  Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutMinimum"));  //  2 sec
	static int timeOutMidiam =  Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutMidiam"));    //  5 sec
	static int timeOutMaximum = Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutMaximum"));  //  10 sec
	static int timeOutXMaximum = Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutXMaximum")); // 20 sec
	
	public static WebDriver driver;
	@Test(priority=1)
	public void initializedDriver(){
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		driver = DriverUtil.getDriver(driver,drivername);
		
		Log.info("----------------- Test Case : Manage skill-Add/Edit/Delete ------------------- ");
	}
	
	@Test(priority=2)
	public void userLogin(){
		String username = ReadPropertiesFile.getPropValues("username");
		String password = ReadPropertiesFile.getPropValues("password");
		LoginUtil.userLogin(username, password,driver);
	}
	
	@Test(priority=3)
	public void navigateOnPage(){
		NavigationUtil.navigationOnSubMenu("Skill", "Manage",driver);
	}
	
	@Test(priority=4)
	public void deleteSkill() throws AWTException, InterruptedException{
		Log.info("$$ START FUNCTION : deleteSkill() ");
		
		String deleteSkill = "SuperSkill";
		List<WebElement> allSkil= driver.findElements(By.xpath(links_allSkill));
		boolean foundSkill = checkSkillPresent(allSkil,deleteSkill);
		
		// if foundSkill is false mean Skill not found then add new skill
		if(!foundSkill){
			addSkill(deleteSkill, "skill Description","Skill is successfully added.");
		}
		
		List<WebElement> allSkilAfterSkillAdded= driver.findElements(By.xpath(links_allSkill));
		boolean foundSkillAfterSkillAdded = checkSkillPresent(allSkilAfterSkillAdded,deleteSkill);
		Assert.assertTrue(" 'deleteSkill' is not added succesfully", foundSkillAfterSkillAdded);
		Thread.sleep(2000); // Important
		WebElement deleteSkillButton= WaitUtil.getElement(driver,By.xpath(button_deleteSkill), 10);
		deleteSkillButton.click();
		Log.info("clicked on delete skill button");
		
		WaitUtil.getElement(driver,By.xpath(button_deleteSkillOnPopup),5).click();
		Log.info("clicked on delete skill confarmation button");

		/*String deleteConfamrmationMsg = WaitUtil.getElement(driver,By.xpath(text_deleteConfarmationMsg), 10).getText();
		int indexOfDelete = deleteConfamrmationMsg.lastIndexOf("delete");
		int lastIndex = deleteConfamrmationMsg.indexOf("?");
		String skillName = deleteConfamrmationMsg.substring(indexOfDelete,lastIndex);
		if(skillName.trim().equalsIgnoreCase(deleteSkill)){
			Log.info("Before  clicked on delete skill confarmation button");
			WaitUtil.getElement(driver,By.xpath(button_deleteSkillOnPopup),5).click();
			Log.info("clicked on delete skill confarmation button");
		}*/
		Thread.sleep(4000);
		String deleteSuccessMessage = WaitUtil.getElement(driver,By.xpath(text_DeleteSuccessMessage), 10).getText();
		Assert.assertEquals("Skill has not been deleted successfully - message does not matching",
				"Skill Successfully Deleted.", deleteSuccessMessage);
		
		Log.info("Skill deleted succesfully");
	}
	
	@Test(priority=5,dataProvider="getData",dependsOnMethods="deleteSkill")
	public void addSkill(String skill, String description, String message) throws InterruptedException{
		//System.out.println("message=======>"+message);
		Thread.sleep(1000);
		driver.findElement(By.xpath(button_OpenAddSkillForm)).click();
		
		WaitUtil.waitForElementLocated(By.xpath(input_skill),driver);
		driver.findElement(By.xpath(input_skill)).clear();
		driver.findElement(By.xpath(input_skill)).sendKeys(skill);
		driver.findElement(By.xpath(input_skillDescription)).clear();
		driver.findElement(By.xpath(input_skillDescription)).sendKeys(description);
		Thread.sleep(2000);
		WebElement submitButton = driver.findElement(By.xpath(button_SubmitAddSkillForm));
		if(submitButton.isEnabled()){
			Thread.sleep(1000);
			submitButton.click();
			String SuccessMsg="Skill is successfully added.";
			if(message.equals(SuccessMsg)){
				WaitUtil.waitForElementLocated(By.xpath(text_skillAddedSuccesfMsg),driver);
				String actualSuccessMsg = driver.findElement(By.xpath(text_skillAddedSuccesfMsg)).getText();
				Assert.assertTrue("Skill not added in the database May be because of duplicate Skill",actualSuccessMsg.equals("Skill is successfully added."));
			}
			String duplicateMsg="Skill already exists, add new skill.";
			if(message.equals(duplicateMsg)){
				Thread.sleep(1000);
				WaitUtil.waitForElementLocated(By.xpath(text_ErrorMessageDuplicate),driver);
				Thread.sleep(1000);
				String duplicateUserErrorMsg = driver.findElement(By.xpath(text_ErrorMessageDuplicate)).getText();
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				// If error message diplaying fine then the no issue otherwise assertion fails
				Assert.assertTrue("Duplicate Skill Name - Error message not matching",message.equals(duplicateUserErrorMsg));
			}
			String invalidSkilMsg="This is not valid Skill name";
			if(message.equals(invalidSkilMsg)){
				WaitUtil.waitForElementLocated(By.xpath(text_ErrorMessage),driver);
				String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
				Thread.sleep(1000);
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				// If error message diplaying fine then the no issue otherwise assertion fails
				Assert.assertTrue("Invalid Skill Name - Error message not matching",message.equals(actualErrorMsg));
			}
		}else{
			WaitUtil.waitForElementLocated(By.xpath(text_ErrorMessage),driver);
			Thread.sleep(1000);
			String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
			Thread.sleep(1000);
			driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
			Assert.assertTrue("Error message does not matched",actualErrorMsg.equals(message));
		}
	}
	
	@Test(priority=6,dataProvider="getDataForEdit")
	public void EditSkill(String skill, String description, String message) throws InterruptedException{
		//NavigationUtil.navigationOnSubMenu("Skill", "Manage",driver);
		if(ReadPropertiesFile.getPropValues("drivername").equalsIgnoreCase("firefox")){
			Assert.assertTrue("This TC ll'nt supported to Mozilla Because edit fields only enter first character of input value", false);
		}
		String skillEdit = "editedSkill"; // It should be present on page
		boolean foundSkill = false;
		
		WaitUtil.waitForElementLocated(By.xpath(links_allSkill),driver);
		List<WebElement> allSkill= driver.findElements(By.xpath(links_allSkill));
		for(WebElement allskill:allSkill){
			if(allskill.getText().equals(skillEdit)){
				Thread.sleep(1000);
				allskill.click();
				foundSkill=true;
				break;
			}
		}
		if(!foundSkill){
			System.out.println("In add skill page");
			addSkill(skillEdit,"Skill Description", "Skill is successfully added.");
			List<WebElement> allSkil= driver.findElements(By.xpath(links_allSkill));
			for(WebElement allskill:allSkil){
				if(allskill.getText().equals(skillEdit)){
					Thread.sleep(1000);
					allskill.click();
					foundSkill=true;
					break;
				}
			}
		}
		Assert.assertTrue("Skill ["+skillEdit+"] which you want to EDIT is not present.First add it.", foundSkill);
		Actions actions = new Actions(driver);
		Thread.sleep(2000);
		actions.moveToElement(driver.findElement(By.xpath(button_OpenEDITSkillForm))).click().build().perform();
		
		//*************************************************************************
		WaitUtil.waitForElementLocated(By.xpath(input_skill),driver);
		driver.findElement(By.xpath(input_skill)).clear();
		driver.findElement(By.xpath(input_skill)).sendKeys(skill);
		driver.findElement(By.xpath(input_skillDescription)).clear();
		driver.findElement(By.xpath(input_skillDescription)).sendKeys(description);
		driver.findElement(By.xpath(input_skill)).click();
		driver.findElement(By.xpath(input_skillDescription)).click();
		
		WebElement submitButton = driver.findElement(By.xpath(button_SubmitAddSkillForm));
		if(submitButton.isEnabled()){
			submitButton.click();
			String SuccessMsg="Skill Successfully Modified.";
			if(message.equals(SuccessMsg)){
				WaitUtil.waitForElementLocated(By.xpath(text_EditSuccessMessage),driver);
				String actualSuccessMsg = driver.findElement(By.xpath(text_EditSuccessMessage)).getText();
				//System.out.println("((((((actualSuccessMsg======>"+actualSuccessMsg);
				Assert.assertTrue("Skill not added in the database May be because of duplicate Skill",actualSuccessMsg.equals("Skill Successfully Modified."));
			}
			String duplicateMsg="Skill already exists, add new skill.";
			if(message.equals(duplicateMsg)){
				Thread.sleep(1000);
				String duplicateUserErrorMsg = driver.findElement(By.xpath(text_ErrorMessageDuplicate)).getText();
				System.out.println("duplicateUserErrorMsg=====>"+duplicateUserErrorMsg);
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				// If error message diplaying fine then the no issue otherwise assertion fails
				Assert.assertTrue("Duplicate Skill Name - Error message not matching",message.equals(duplicateUserErrorMsg));
			}
			String invalidSkilMsg="This is not valid Skill name";
			if(message.equals(invalidSkilMsg)){
				WaitUtil.waitForElementLocated(By.xpath(text_ErrorMessage),driver);
				String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
				System.out.println("((((((actualErrorMsg======>"+actualErrorMsg);
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				// If error message diplaying fine then the no issue otherwise assertion fails
				Assert.assertTrue("Invalid Skill Name - Error message not matching",message.equals(actualErrorMsg));
			}
		}else{
			WaitUtil.waitForElementLocated(By.xpath(text_ErrorMessage),driver);
			String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
			System.out.println("actualErrorMsg = "+actualErrorMsg);
			System.out.println("message = "+message);
			try{
					Assert.assertTrue("Error message does not matched",actualErrorMsg.equals(message));
				}
			finally{
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
			}
		}
	}
	
	private boolean checkSkillPresent(List<WebElement> skillList,String skillEdit) throws InterruptedException{
		boolean foundSkill=false;
		for(WebElement allskill:skillList){
			if(allskill.getText().equals(skillEdit)){
				Thread.sleep(1000);
				allskill.click();
				foundSkill=true;
				break;
			}
		}
		return foundSkill;
	}
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
	/*----------------------------- Data Provider ---------------------------------------------------*/
	@DataProvider
	public Object[][] getData() throws IOException{
		ReadExcelFile readExcelFile = new ReadExcelFile(); 
		String filePath = "/resource/testData";
		String fileName = "ManageSkill.xlsx";
		String sheetName = "AddSkill";
		Object data[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return data;
	}
	
	@DataProvider
	public Object[][] getDataForEdit() throws IOException{
		ReadExcelFile readExcelFile = new ReadExcelFile(); 
		String filePath = "/resource/testData";
		String fileName = "ManageSkill.xlsx";
		String sheetName = "EditSkill";
		Object data[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return data;
	}
}
