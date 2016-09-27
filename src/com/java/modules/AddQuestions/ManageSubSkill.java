package com.java.modules.AddQuestions;

import java.awt.AWTException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

public class ManageSubSkill {
	//static int timeOutMinimum =  Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutMinimum"));  //  2 sec
	//static int timeOutMidiam =  Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutMidiam"));    //  5 sec
	//static int timeOutMaximum = Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutMaximum"));  //  10 sec
	//static int timeOutXMaximum = Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutXMaximum")); // 20 sec
	
	static String button_OpenAddSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenAddSkillForm","AddQuestion");
	static String button_SubmitAddSkillForm  = ReadLocatorsFromExcel.readExcel("button_SubmitAddSkillForm","AddQuestion");
	static String button_SkilReset = ReadLocatorsFromExcel.readExcel("button_SkilReset","AddQuestion");
	static String button_CloseAddSkillForm = ReadLocatorsFromExcel.readExcel("button_CloseAddSkillForm","AddQuestion");
	static String button_OpenEDITSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenEDITSkillForm","AddQuestion");
	static String button_OpenAddSubSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenAddSubSkillForm","AddQuestion");
	static String button_deleteSkillOnPopup = ReadLocatorsFromExcel.readExcel("button_deleteSkillOnPopup", "AddQuestion");
	static String button_deleteSubSkill = ReadLocatorsFromExcel.readExcel("button_deleteSubSkill", "AddQuestion");
	
	static String input_skill = ReadLocatorsFromExcel.readExcel("input_skill","AddQuestion");
	static String input_skillDescription = ReadLocatorsFromExcel.readExcel("input_skillDescription","AddQuestion");
	
	static String links_allSkill = ReadLocatorsFromExcel.readExcel("links_allSkill","AddQuestion");
	static String links_allSubSkill = ReadLocatorsFromExcel.readExcel("links_allSubSkill", "AddQuestion");
	
	static String text_SuccessMessage = ReadLocatorsFromExcel.readExcel("text_SuccessMessage","AddQuestion");
	static String text_ErrorMessageDuplicate = ReadLocatorsFromExcel.readExcel("text_ErrorMessageDuplicate","AddQuestion");
	static String text_ErrorMessage = ReadLocatorsFromExcel.readExcel("text_ErrorMessage","AddQuestion");
	static String text_deleteConfarmationMsg = ReadLocatorsFromExcel.readExcel("text_deleteConfarmationMsg", "AddQuestion");
	static String text_DeleteSuccessMessage = ReadLocatorsFromExcel.readExcel("text_DeleteSuccessMessage", "AddQuestion");
	
	public static WebDriver driver;
	AddQuestion addQuestion;
	
	@Test(priority=1)
	public void initializedDriver(){
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		driver = DriverUtil.getDriver(driver,drivername);
		Log.info("----------------- Test Case : Manage Sub skill ------------------- ");
		addQuestion = new AddQuestion();
	}
	
	@Test(priority=2)
	public void userLogin(){
		String username = ReadPropertiesFile.getPropValues("username");
		String password = ReadPropertiesFile.getPropValues("password");
		LoginUtil.userLogin(username, password,driver);
	}
	
	@Test(priority=3)
	public void navigateOnPage(){
		NavigationUtil.navigationOnSubMenu("Skill", "Manage Skills",driver);
	}
	
	@Test(priority=4)
	public void deleteSubSkill() throws AWTException, InterruptedException{
		Log.info("$$ START FUNCTION : ManageSubSkill.deleteSubSkill() ");
		
		// First find super skill of sub skill which want to delete
		List<WebElement> allSkil= driver.findElements(By.xpath(links_allSkill));
		boolean foundSkill = false; 
		foundSkill = addQuestion.checkIfElementPresentInList(allSkil,"SuperSkill");
		
		Assert.assertTrue("A sub skill which want to delete its Superskill 'SuperSkill' not present. Therefore we can not select and delete sub skill", foundSkill);
		
		String deleteSkill = "SubSkill";
		List<WebElement> allSubSkil= driver.findElements(By.xpath(links_allSubSkill));
		boolean foundSubSkill = false;
		foundSubSkill = addQuestion.checkIfElementPresentInList(allSubSkil,deleteSkill);
		// if foundSubSkill is false mean sub Skill not found then add new sub skill
		if(!foundSubSkill){
			addSubSkill(deleteSkill, "Sub Skill Description","SubSkill is successfully added.");
			
			List<WebElement> allSubSkilAfterSkillAdded= driver.findElements(By.xpath(links_allSubSkill));
			boolean foundSubSkillAfterSubSkillAdded = addQuestion.checkIfElementPresentInList(allSubSkilAfterSkillAdded,deleteSkill);
			Assert.assertTrue(" Sub skill can not be deleted because it is not in DB", foundSubSkillAfterSubSkillAdded);
		}
		
		Thread.sleep(2000); // Important
		WebElement deleteSubSkillButton= WaitUtil.getElement(driver,By.xpath(button_deleteSubSkill), 8);
		Thread.sleep(2000);
		deleteSubSkillButton.click();
		Log.info("clicked on delete Sub skill button");
		Thread.sleep(2000);
		WaitUtil.getElement(driver,By.xpath(button_deleteSkillOnPopup),5).click();
		Thread.sleep(2000);
		String deleteSuccessMessage = WaitUtil.getElement(driver,By.xpath(text_DeleteSuccessMessage), 10).getText();
		Assert.assertEquals("Skill has not been deleted successfully - message does not matching",
				"SubSkill Successfully Deleted.", deleteSuccessMessage);
		Log.info("Skill deleted succesfully");
		Log.info("$$ END FUNCTION : ManageSubSkill.deleteSubSkill() ");
	}
	
	@Test(priority=5,dataProvider="getData",dependsOnMethods="deleteSubSkill")
	public void addSubSkill(String subSkill, String SubSkilldescription, String message) throws InterruptedException{
		String skill = "SuperSkill";
		String description = "Super skilll description";
		String successMessage = "Skill is successfully added.";
		// Search if skill is already present
		CommanFunctions commanFunctions = new CommanFunctions();
		
		Log.info("$$ START FUNCTION : ManageSubSkill.checkAndCreate_Skill_SubSkill_Topic()");
		//Check if "SuperSkill" Skill is all ready present if not then add new skill
		
		List<WebElement> allSkil= driver.findElements(By.xpath(links_allSkill));
		boolean foundSkill = false; 
		foundSkill = addQuestion.checkIfElementPresentInList(allSkil,skill);
		if(!foundSkill){
			Log.info("Skill ="+skill+" Not found in DB");
			commanFunctions.addSkillOrSubSkillOrTopic(skill, description,successMessage,
					By.xpath(button_OpenAddSkillForm),By.xpath(button_SubmitAddSkillForm),driver);
			Log.info("Skill ="+skill+" Added in DB");
			List<WebElement> allSkilAfterSkillAdded= driver.findElements(By.xpath(links_allSkill));
			boolean foundSkillAfterSkillAdded=false;
			Log.info("Checking if Skill ="+skill+" is succesfully added in DB?");
			foundSkillAfterSkillAdded = addQuestion.checkIfElementPresentInList(allSkilAfterSkillAdded,skill);
			Assert.assertTrue(" [AddQuestion_Skill] is not added succesfully", foundSkillAfterSkillAdded);
			Log.info("Skill ="+skill+" is succesfully added in DB?");
		}
		Thread.sleep(2000);
		driver.findElement(By.xpath(button_OpenAddSubSkillForm)).click();
		
		//WaitUtil.waitForElementXPATH(input_skill);
		
		driver.findElement(By.xpath(input_skill)).clear();
		driver.findElement(By.xpath(input_skill)).sendKeys(subSkill);
		driver.findElement(By.xpath(input_skillDescription)).clear();
		driver.findElement(By.xpath(input_skillDescription)).sendKeys(SubSkilldescription);
		Thread.sleep(2000);
		WebElement submitButton = driver.findElement(By.xpath(button_SubmitAddSkillForm));
		Thread.sleep(1000);	
		if(submitButton.isEnabled()){
			submitButton.click();
			Log.info("Clicked Submit button on Add sub skill form");
			String SuccessMsg="SubSkill is successfully added.";
			// Check for the Success case
			if(message.equals(SuccessMsg)){
				Log.info("In IF condition - Success message matched");
				List<WebElement> list = driver.findElements(By.xpath(text_ErrorMessageDuplicate));
				// If size greater that 0 then it mean 
				Log.info("list.size()="+list.size());
				if(list.size()>0){
					driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
					Log.info("Closed Add sub skill form 1");
					Assert.assertTrue("You are trying to create sub skill which already present. Please change " +
							"the Sub skill name in Excel sheet.", false);
				}
				//WaitUtil.waitForElementXPATH(text_SuccessMessage);
				String actualSuccessMsg = driver.findElement(By.xpath(text_SuccessMessage)).getText();
				Assert.assertTrue("Sub Skill not added in the database May be because of duplicate sub Skill",actualSuccessMsg.equals(message));
				Log.info("Sub skill successfully added in database");
			}
			
			String duplicateMsg="SubSkill already exists, add new SubSkill.";
			// Check for the duplicate 'sub skill' case
			if(message.equals(duplicateMsg)){
				Log.info("In IF condition - duplicate error messasage matched");
				Log.info("1");
				Thread.sleep(1000);
				String duplicateUserErrorMsg = driver.findElement(By.xpath(text_ErrorMessageDuplicate)).getText();
				System.out.println("duplicateUserErrorMsg="+duplicateUserErrorMsg);
				Thread.sleep(1000);
				Log.info("2");
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				Log.info("Closed Add sub skill form 2");
				Assert.assertTrue("Duplicate Sub Skill Name - Error message not matching",message.equals(duplicateUserErrorMsg));
			}
			
			String invalidTopicErrMsg="This is not valid Topic name";
			// check for the not valid sub-skill name
			if(message.equals(invalidTopicErrMsg)){
				//WaitUtil.waitForElementXPATH(text_ErrorMessage);
				String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				// If error message displaying fine then the no issue otherwise assertion fails
				Assert.assertTrue("Invalid Skill Name - Error message not matching",message.equals(actualErrorMsg));
			}
		}else{
			//WaitUtil.waitForElementXPATH(text_ErrorMessage);
			Thread.sleep(2000);
			String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
			try{
					Assert.assertTrue("Error message does not matched",actualErrorMsg.equals(message));
				}
			finally{
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
			}
		}
	}
	
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
	
	@DataProvider
	public Object[][] getData() throws IOException{
		ReadExcelFile readExcelFile = new ReadExcelFile(); 
		String filePath = "/resource/testData";
		String fileName = "ManageSkill.xlsx";
		String sheetName = "AddSubSkill";
		Object data[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return data;
	}
}
