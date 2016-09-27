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

public class ManageTopic {
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
	static String button_deleteTopic = ReadLocatorsFromExcel.readExcel("button_deleteTopic", "AddQuestion");
	static String button_deleteTopicConform = ReadLocatorsFromExcel.readExcel("button_deleteTopicConform", "AddQuestion");
	static String button_OpenAddTopicForm = ReadLocatorsFromExcel.readExcel("button_OpenAddTopicForm", "AddQuestion");
	static String button_SubmitTopicForm = ReadLocatorsFromExcel.readExcel("button_SubmitTopicForm", "AddQuestion");
	static String button_openEditTopicForm = ReadLocatorsFromExcel.readExcel("button_openEditTopicForm", "AddQuestion");
	
	static String input_skill = ReadLocatorsFromExcel.readExcel("input_skill","AddQuestion");
	static String input_skillDescription = ReadLocatorsFromExcel.readExcel("input_skillDescription","AddQuestion");
	static String input_topicName = ReadLocatorsFromExcel.readExcel("input_topicName","AddQuestion");
	static String input_topicDescription = ReadLocatorsFromExcel.readExcel("input_topicDescription","AddQuestion");
	
	static String links_allSkill = ReadLocatorsFromExcel.readExcel("links_allSkill","AddQuestion");
	static String links_allSubSkill = ReadLocatorsFromExcel.readExcel("links_allSubSkill", "AddQuestion");
	static String link_topic = ReadLocatorsFromExcel.readExcel("link_topic", "AddQuestion");
	
	static String text_SuccessMessage = ReadLocatorsFromExcel.readExcel("text_SuccessMessage","AddQuestion");
	static String text_ErrorMessageDuplicate = ReadLocatorsFromExcel.readExcel("text_ErrorMessageDuplicate","AddQuestion");
	static String text_ErrorMessage = ReadLocatorsFromExcel.readExcel("text_ErrorMessage","AddQuestion");
	static String text_deleteConfarmationMsg = ReadLocatorsFromExcel.readExcel("text_deleteConfarmationMsg", "AddQuestion");
	static String text_DeleteSuccessMessage = ReadLocatorsFromExcel.readExcel("text_DeleteSuccessMessage", "AddQuestion");
	static String text_SuccessTopicAddedMsg = ReadLocatorsFromExcel.readExcel("text_SuccessTopicAddedMsg", "AddQuestion");
	static String text_successTopicModifyMsg = ReadLocatorsFromExcel.readExcel("text_successTopicModifyMsg", "AddQuestion");
	
	public static WebDriver driver;
	AddQuestion addQuestion;
	ManageSubSkill manageSubSkill;
	ManageSkill manageSkill;
	CommanFunctions commanFunctions;
	@Test(priority=1)
	public void initializedDriver(){
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		driver = DriverUtil.getDriver(driver,drivername);
		Log.info("----------------- Test Case : Manage Topic ------------------- ");
		addQuestion = new AddQuestion();
		//manageSubSkill = new ManageSubSkill();
		//manageSkill = new ManageSkill();
		commanFunctions = new CommanFunctions();
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
	public void deleteTopic() throws AWTException, InterruptedException{
		Log.info("$$ START FUNCTION : ManageSubSkill.deleteTopic() ");
		
		// First find super skill of topic which want to delete
		List<WebElement> allSkil= driver.findElements(By.xpath(links_allSkill));
		boolean foundSkill = false; 
		foundSkill = addQuestion.checkIfElementPresentInList(allSkil,"SuperSkill");
		if(!foundSkill){
			Log.info("Skill not found");
			commanFunctions.addSkillOrSubSkillOrTopic("SuperSkill", "skill Description","Skill is successfully added.",
					By.xpath(button_OpenAddSkillForm),By.xpath(button_SubmitAddSkillForm),driver);
			
			Log.info("Skill added");
			Thread.sleep(3000);
			boolean foundSkillAfterSkillAdded = addQuestion.checkIfElementPresentInList(allSkil,"SuperSkill");
			Assert.assertTrue("A Topic which want to delete its Superskill is not present. Therefore we can not select and delete Topic", foundSkillAfterSkillAdded);
		}
		
		String deleteSubSkill = "SubSkill";
		List<WebElement> allSubSkil= driver.findElements(By.xpath(links_allSubSkill));
		boolean foundSubSkill = false;
		foundSubSkill = addQuestion.checkIfElementPresentInList(allSubSkil,deleteSubSkill);
		// if foundSubSkill is false mean sub Skill not found then add new sub skill
		if(!foundSubSkill){
			commanFunctions.addSkillOrSubSkillOrTopic(deleteSubSkill, "Sub Skill Description","SubSkill is successfully added.",
					By.xpath(button_OpenAddSubSkillForm),By.xpath(button_SubmitAddSkillForm),driver);
			
			Thread.sleep(2000);
			List<WebElement> allSubSkilAfterSkillAdded= driver.findElements(By.xpath(links_allSubSkill));
			boolean foundSubSkillAfterSubSkillAdded = addQuestion.checkIfElementPresentInList(allSubSkilAfterSkillAdded,deleteSubSkill);
			Assert.assertTrue(" Topic can not be deleted because its Sub skill 'SubSkill' not added in DB", foundSubSkillAfterSubSkillAdded);
		}
		
		String deleteTopic = "topic";
		List<WebElement> allTopic= driver.findElements(By.xpath(link_topic));
		boolean foundTopic = false;
		Log.info("Check if topic ='topic' present in DB");
		foundTopic = addQuestion.checkIfElementPresentInList(allTopic,deleteTopic);
				
		if(!foundTopic){
			Log.info(" topic ='topic' Not present in DB.");
			commanFunctions.addSkillOrSubSkillOrTopic(deleteTopic, "Topic Description","Topic is successfully added.",
					By.xpath(button_OpenAddTopicForm),By.xpath(button_SubmitTopicForm),driver);
			//Thread.sleep(2000);
			List<WebElement> allTopicAfterTopicAdded= driver.findElements(By.xpath(link_topic));
			boolean foundTopicAfterTopicAdded = false;
			foundTopicAfterTopicAdded = addQuestion.checkIfElementPresentInList(allTopicAfterTopicAdded,deleteTopic);
			Assert.assertTrue(" topic is not added succesfully", foundTopicAfterTopicAdded);
			Log.info("Topic added succesfully");
		}
		
		Thread.sleep(2000); // Important
		WebElement deleteTopicButton= WaitUtil.getElement(driver,By.xpath(button_deleteTopic), 10);
		Thread.sleep(2000);
		deleteTopicButton.click();
		Log.info("clicked on delete Topic button");
		
		Thread.sleep(2000);
		WaitUtil.getElement(driver,By.xpath(button_deleteTopicConform),10).click();
		Log.info("Clicked on delete topic confarmation button");
		
		Thread.sleep(2000);
		String deleteTopicSuccessMsg = WaitUtil.getElement(driver,By.xpath(text_DeleteSuccessMessage), 10).getText();
		Log.info("Delete topic succesfully message found");
		
		Assert.assertEquals("Topic has not been deleted successfully - message does not matched",
				"Topic Successfully Deleted.", deleteTopicSuccessMsg);
		Log.info("Topic deleted succesfully");
		Log.info("$$ END FUNCTION : ManageSubSkill.deleteSubSkill() ");
	}
	
	@Test(priority=5,dataProvider="getData",dependsOnMethods="deleteTopic")
	public void addTopic(String topic, String topicDescription, String orgCompetency, String message) throws InterruptedException{

		Log.info("$$ START FUNCTION : ManageTopic.addTopic()");
		
		Thread.sleep(5000);
		
		driver.findElement(By.xpath(button_OpenAddTopicForm)).click();
		
		//WaitUtil.waitForElementXPATH(input_skill);
		
		driver.findElement(By.xpath(input_skill)).clear();
		driver.findElement(By.xpath(input_skill)).sendKeys(topic);
		driver.findElement(By.xpath(input_skillDescription)).clear();
		driver.findElement(By.xpath(input_skillDescription)).sendKeys(topicDescription);
		driver.findElement(By.xpath("//form//div[2]//div[3]//div[2]//span//button")).click();
		driver.findElement(By.xpath(".//*[@id='formAddTopic']//label//span[contains(.,'"+orgCompetency+"')]")).click();
		driver.findElement(By.xpath(input_skill)).click();
		
		Thread.sleep(2000);
		WebElement submitButton = driver.findElement(By.xpath(button_SubmitTopicForm));
		Thread.sleep(1000);	
		if(submitButton.isEnabled()){
			Thread.sleep(1000);
			submitButton.click();
			Log.info("Clicked Submit button on Add topic form");
			String SuccessMsg="Topic is successfully added.";
			// Check for the Success case
			if(message.equals(SuccessMsg)){
				Log.info("In IF condition - Success message matched");
				Thread.sleep(1000);
				List<WebElement> list = driver.findElements(By.xpath(text_ErrorMessageDuplicate));
				// If size greater that 0 then it mean 
				if(list.size()>0){
					Thread.sleep(2000);
					driver.findElement(By.xpath("/html/body/div[4]/div/div/form/div/button[@ng-click='cancel()']")).click();
					Log.info("Closed topic form 1");
					Assert.assertTrue("You are trying to create topic which already present. Please change " +
							"the topic name in Excel sheet.", false);
				}
				//WaitUtil.waitForElementXPATH(text_SuccessMessage);
				String actualSuccessMsg = driver.findElement(By.xpath(text_SuccessTopicAddedMsg)).getText();
				Assert.assertTrue("Topic not added in the database May be because of duplicate topic",actualSuccessMsg.equals(message));
				Log.info("Topic successfully added in database");
			}
			
			String duplicateMsg="Topic already exists, add new topic.";
			// Check for the duplicate 'sub skill' case
			if(message.equals(duplicateMsg)){
				Log.info("In IF condition - duplicate error messasage matched");
				Thread.sleep(1000);
				String duplicateUserErrorMsg = driver.findElement(By.xpath(text_ErrorMessageDuplicate)).getText();
				Thread.sleep(1000);
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				Log.info("Closed Add sub skill form 2");
				Assert.assertTrue("Duplicate Sub Skill Name - Error message not matching",message.equals(duplicateUserErrorMsg));
			}
			
			String invalidTopicMsg="This is not valid Topic name";
			// check for the not valid sub-skill name
			if(message.equals(invalidTopicMsg)){
				//WaitUtil.waitForElementXPATH(text_ErrorMessage);
				String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				//driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				// If error message displaying fine then the no issue otherwise assertion fails
				Assert.assertTrue("Invalid Skill Name - Error message not matching",message.equals(actualErrorMsg));
			}
		}else{
			//WaitUtil.waitForElementXPATH(text_ErrorMessage);
			Thread.sleep(2000);
			String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
			driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
					Assert.assertTrue("Error message does not matched",actualErrorMsg.equals(message));
		}
	}
	
	@Test(priority=6,dataProvider="getDataForEdit",dependsOnMethods="addTopic")
	public void EditTopic(String topic, String topicDescription,String orgCompetency, String message) throws InterruptedException{
		//NavigationUtil.navigationOnSubMenu("Skill", "Manage",driver);
		if(ReadPropertiesFile.getPropValues("drivername").equalsIgnoreCase("firefox")){
			Assert.assertTrue("This TC ll'nt supported to Mozilla Because edit fields only enter first character of input value", false);
		}
		
		// To complete duplicate topic scenario we must have to put one topic first 
		if(message.equalsIgnoreCase("Topic already exists, add new topic.")){
			
			String duplicateTopic = "duplicateTopicForEdit";
			List<WebElement> allTopic= driver.findElements(By.xpath(link_topic));
			boolean foundTopic = false;
			Log.info("Check if topic ='duplicateTopicForEdit' present in DB");
			foundTopic = addQuestion.checkIfElementPresentInList(allTopic,duplicateTopic);
					
			if(!foundTopic){
				Log.info(" topic ='duplicateTopic' Not present in DB.");
				commanFunctions.addSkillOrSubSkillOrTopic(duplicateTopic, "Topic Description","Topic is successfully added.",
						By.xpath(button_OpenAddTopicForm),By.xpath(button_SubmitTopicForm),driver);
				//Thread.sleep(2000);
				List<WebElement> allTopicAfterDuplicateTopicAdded= driver.findElements(By.xpath(link_topic));
				boolean foundTopicAfterTopicAdded = false;
				foundTopicAfterTopicAdded = addQuestion.checkIfElementPresentInList(allTopicAfterDuplicateTopicAdded,duplicateTopic);
				Assert.assertTrue(" topic is not added succesfully", foundTopicAfterTopicAdded);
				Log.info("Duplicate Topic added succesfully");
			}
		}
		Thread.sleep(2000);
		
		String topicEdit = "editedTopic"; // It should be present on page
		Thread.sleep(2000);
		List<WebElement> allTopic= driver.findElements(By.xpath(link_topic));
		boolean foundTopic = false;
		Log.info("Check if topic ='editedTopic' present in DB");
		foundTopic = addQuestion.checkIfElementPresentInList(allTopic,topicEdit);
		Thread.sleep(1000);		
		if(!foundTopic){
			Log.info(" topic ='editedTopic' Not present in DB.");
			commanFunctions.addSkillOrSubSkillOrTopic(topicEdit, "Edit Topic Description","Topic is successfully added.",
					By.xpath(button_OpenAddTopicForm),By.xpath(button_SubmitTopicForm),driver);
			Thread.sleep(2000);
			List<WebElement> allTopicAfterTopicAdded= driver.findElements(By.xpath(link_topic));
			boolean foundTopicAfterTopicAdded = false;
			foundTopicAfterTopicAdded = addQuestion.checkIfElementPresentInList(allTopicAfterTopicAdded,topicEdit);
			Assert.assertTrue(" topic is not added succesfully", foundTopicAfterTopicAdded);
			Log.info("Topic added succesfully");
		}
		
		Actions actions = new Actions(driver);
		
		Log.info("Before clicked on open edit topic form");
		//driver.findElement(By.xpath(".//*[@id='widgetPanelBottomTopic']/span[2]/a/img")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("/html/body/div/div[2]/div[4]/div/div/div[3]/div/div[3]/span[2]/a/img[@src='images/skill_edit.png']")).click();
		Log.info("Aftr clicked on open edit topic form");
		//actions.moveToElement(driver.findElement(By.xpath(button_openEditTopicForm))).click().build().perform();
		Thread.sleep(1000);
		
		driver.findElement(By.xpath(input_topicName)).clear();
		driver.findElement(By.xpath(input_topicName)).sendKeys(topic);
		Thread.sleep(1000);
		driver.findElement(By.xpath(".//*[@id='formModifyTopic']//button[@ng-bind-html='varButtonLabel']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//form//div[2]//div[3]//div//label//span[contains(text(),'"+orgCompetency+"')]")).click();
		driver.findElement(By.xpath(input_topicDescription)).clear();
		Thread.sleep(1000);
		driver.findElement(By.xpath(input_topicDescription)).sendKeys(topicDescription);
		Thread.sleep(1000);
		WebElement submitButton = driver.findElement(By.xpath(button_SubmitTopicForm));
		if(submitButton.isEnabled()){
			Thread.sleep(1000);
			submitButton.click();
			Log.info("Clicked Submit button on Add topic form");
			String SuccessMsg="Topic Successfully Modified.";
			if(message.equals(SuccessMsg)){
				Thread.sleep(2000);
				List<WebElement> list = driver.findElements(By.xpath(text_ErrorMessageDuplicate));
				// If size greater that 0 then it mean 
				if(list.size()>0){
					Thread.sleep(3000);
					driver.findElement(By.xpath("/html/body/div[4]/div/div/form/div/button[@ng-click='cancel()']")).click();
					Log.info("Closed topic form 1");
					Assert.assertTrue("You are trying to create topic which already present. Please change " +
							"the topic name in Excel sheet.", false);
				}
				//WaitUtil.waitForElementXPATH(text_SuccessMessage);
				String actualSuccessMsg = driver.findElement(By.xpath(text_successTopicModifyMsg)).getText();
				Assert.assertTrue("Topic not added in the database May be because of duplicate topic",actualSuccessMsg.equals(message));
				Log.info("Topic successfully added in database");
			}
			
			String duplicateMsg="Topic already exists, add new topic.";
			// Check for the duplicate 'sub skill' case
			if(message.equals(duplicateMsg)){
				Log.info("In IF condition - duplicate error messasage matched");
				Thread.sleep(1000);
				String duplicateUserErrorMsg = driver.findElement(By.xpath(text_ErrorMessageDuplicate)).getText();
				Thread.sleep(2000);
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				Log.info("Closed Add topic form 2");
				Assert.assertTrue("Duplicate topic Name - Error message not matching",message.equals(duplicateUserErrorMsg));
			}
			String invalidTopicMsg="This is not valid Topic name";
			// check for the not valid sub-skill name
			if(message.equals(invalidTopicMsg)){
				Thread.sleep(2000);
				String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
				// If error message displaying fine then the no issue otherwise assertion fails
				Assert.assertTrue("Invalid Skill Name - Error message not matching",message.equals(actualErrorMsg));
			}
		}else{
			//WaitUtil.waitForElementXPATH(text_ErrorMessage);
			Thread.sleep(2000);
			//String actualErrorMsg = driver.findElement(By.xpath(text_ErrorMessage)).getText();
			Thread.sleep(2000);
			driver.findElement(By.xpath(button_CloseAddSkillForm)).click();
			//actions.moveByOffset(200, 200).click();
			Thread.sleep(2000);
			//Assert.assertTrue("Error message does not matched",actualErrorMsg.equals(message));
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
		String sheetName = "AddTopic";
		Object data[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return data;
	}
	
	@DataProvider
	public Object[][] getDataForEdit() throws IOException{
		ReadExcelFile readExcelFile = new ReadExcelFile(); 
		String filePath = "/resource/testData";
		String fileName = "ManageSkill.xlsx";
		String sheetName = "EditTopic";
		Object data[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return data;
	}
	
}
