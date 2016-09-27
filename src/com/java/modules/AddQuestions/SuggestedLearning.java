package com.java.modules.AddQuestions;

import java.awt.AWTException;
import java.io.IOException;
import java.util.List;

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

public class SuggestedLearning {
	
	static String button_OpenAddSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenAddSkillForm","AddQuestion");
	static String button_SubmitAddSkillForm  = ReadLocatorsFromExcel.readExcel("button_SubmitAddSkillForm","AddQuestion");
	static String button_OpenAddSubSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenAddSubSkillForm","AddQuestion");
	static String button_OpenAddTopicForm = ReadLocatorsFromExcel.readExcel("button_OpenAddTopicForm", "AddQuestion");
	static String button_SubmitTopicForm = ReadLocatorsFromExcel.readExcel("button_SubmitTopicForm", "AddQuestion");
	static String button_openEditTopicForm = ReadLocatorsFromExcel.readExcel("button_openEditTopicForm", "AddQuestion");
	static String button_openSuugestedLearnForm = ReadLocatorsFromExcel.readExcel("button_openSuugestedLearnForm", "AddQuestion");
	static String button_SubmitSuggestedLearningForm = ReadLocatorsFromExcel.readExcel("button_SubmitSuggestedLearningForm", "AddQuestion");
	static String button_deleteSuggestedLearning = ReadLocatorsFromExcel.readExcel("button_deleteSuggestedLearning", "AddQuestion");
	static String button_deleteSuggestedLearnConformation = ReadLocatorsFromExcel.readExcel("button_deleteSuggestedLearnConformation", 
			"AddQuestion");
	static String button_openAddSuugestedLearnForm = ReadLocatorsFromExcel.readExcel("button_openAddSuugestedLearnForm", 
			"AddQuestion");
	static String button_closeSuggestedLearnForm = ReadLocatorsFromExcel.readExcel("button_closeSuggestedLearnForm", "AddQuestion");
	
	static String links_allSkill = ReadLocatorsFromExcel.readExcel("links_allSkill","AddQuestion");
	static String links_allSubSkill = ReadLocatorsFromExcel.readExcel("links_allSubSkill", "AddQuestion");
	static String link_topic = ReadLocatorsFromExcel.readExcel("link_topic", "AddQuestion");
	static String link_suggestedLearning = ReadLocatorsFromExcel.readExcel("link_suggestedLearning", "AddQuestion");
	
	static String text_DeleteSuccessMessage = ReadLocatorsFromExcel.readExcel("text_DeleteSuccessMessage", "AddQuestion");
	static String textSuccessDeleteSuggestedLearning = ReadLocatorsFromExcel.readExcel("textSuccessDeleteSuggestedLearning", "AddQuestion");
	static String text_DuplicateSuggestedLearnMsg = ReadLocatorsFromExcel.readExcel("text_DuplicateSuggestedLearnMsg", "AddQuestion");
	static String text_successSuggeLearnMsg = ReadLocatorsFromExcel.readExcel("text_successSuggeLearnMsg", "AddQuestion");
	static String text_errorMsgSuggestedLearn = ReadLocatorsFromExcel.readExcel("text_errorMsgSuggestedLearn", "AddQuestion");
	
	static String input_learningReference = ReadLocatorsFromExcel.readExcel("input_learningReference", "AddQuestion");
	static String input_learningRefDescrip = ReadLocatorsFromExcel.readExcel("input_learningRefDescrip", "AddQuestion");
	static String input_learningRefUrl = ReadLocatorsFromExcel.readExcel("input_learningRefUrl","AddQuestion");
	
	static String dd_referenceType = ReadLocatorsFromExcel.readExcel("dd_referenceType", "AddQuestion");
	
	AddQuestion addQuestion;
	CommanFunctions commanFunctions;
	public static WebDriver driver;
	
	@Test(priority=1)
	public void initializedDriver(){
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		
		driver = DriverUtil.getDriver(driver,drivername);
		addQuestion = new AddQuestion();
		commanFunctions = new CommanFunctions();
		
		Log.info("----------------- Test Case : Suggested Learning ------------------- ");
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
	
	@Test(priority=4,dependsOnMethods="navigateOnPage")
	public void deleteSuggestedLearning() throws AWTException, InterruptedException{
		Log.info("$$ START FUNCTION : SuggestedLearning.deleteSuggestedLearning() ");
		
		// First find super skill of SuggestedLearning which want to delete
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
		
		// Second find sub skill of SuggestedLearning which want to delete
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
		
		// Third find topic of SuggestedLearning which want to delete
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
		
		// Fourth find Suugested leatnign and then delete it
		String deleteSuggestedLearning = "SuggestedLearning";
		List<WebElement> allSuggestedLearning= driver.findElements(By.xpath(link_suggestedLearning));
		boolean SuggestedLearning = false;
		Log.info("Check if SuggestedLearning ='SuggestedLearning' present in DB");
		SuggestedLearning = addQuestion.checkIfElementPresentInList(allSuggestedLearning,deleteSuggestedLearning);
				
		if(!SuggestedLearning){
			Log.info(" SuggestedLearning ='SuggestedLearning' Not present in DB.");
			commanFunctions.addSkillOrSubSkillOrTopic(deleteSuggestedLearning, "Description","Suggested Learning is successfully added.",
					By.xpath(button_openSuugestedLearnForm),By.xpath(button_SubmitSuggestedLearningForm),driver);
			Thread.sleep(1000);
			List<WebElement> allSuggestedLearningAfterAdded = driver.findElements(By.xpath(link_suggestedLearning));
			boolean suggestedLeaningAfterItIsAdded = false;
			suggestedLeaningAfterItIsAdded = addQuestion.checkIfElementPresentInList(allSuggestedLearningAfterAdded,deleteSuggestedLearning);
			Assert.assertTrue("SuggestedLearning is not added succesfully", suggestedLeaningAfterItIsAdded);
			Log.info("SuggestedLearning added succesfully");
		}
		
		Thread.sleep(2000); // Important
		WebElement deleteSuggestedLearningButton= WaitUtil.getElement(driver,By.xpath(button_deleteSuggestedLearning), 10);
		Thread.sleep(2000);
		deleteSuggestedLearningButton.click();
		Log.info("clicked on delete suggesting leaning button");
		
		Thread.sleep(2000);
		WaitUtil.getElement(driver,By.xpath(button_deleteSuggestedLearnConformation),10).click();
		Log.info("Clicked on delete suggested learning button confarmation popup");
		
		Thread.sleep(2000);
		String deleteSuggestedLearnSuccessMsg = WaitUtil.getElement(driver,By.xpath(textSuccessDeleteSuggestedLearning), 10).getText();
		Log.info("Delete Suggested learning succesfully message found");
		
		Assert.assertEquals("Suugested learning has not been deleted successfully - message does not matched",
				"Suggested Learning Successfully Deleted.", deleteSuggestedLearnSuccessMsg);
		Log.info("Suggested Learning deleted succesfully");
		Log.info("$$ START FUNCTION : SuggestedLearning.deleteSuggestedLearning()");
	}
	
	@Test(priority=5,dataProvider="getData",dependsOnMethods="deleteSuggestedLearning")
	public void addSuggestedLearning(String learningRef,String description,String ddOption, String url,String message) 
			throws AWTException, InterruptedException{
		Thread.sleep(1000);
		WaitUtil.getElement(driver, By.xpath(button_openAddSuugestedLearnForm), 10).click();
		Thread.sleep(2000);
		WaitUtil.getElement(driver, By.id(input_learningReference), 5).sendKeys(learningRef);
		WaitUtil.getElement(driver, By.id(input_learningRefDescrip), 5).sendKeys(description);
		WaitUtil.getElement(driver, By.xpath(dd_referenceType), 5).click();
		Thread.sleep(1000);
		WaitUtil.getElement(driver, By.xpath("//li//a[contains(.,'"+ddOption+"')]"), 5).click();
		if(ddOption.equals("Url")){
			WaitUtil.getElement(driver, By.id(input_learningRefUrl), 5).sendKeys(url);
		}
		Thread.sleep(1000);
		WebElement submitSuggestedLearningForm = 
				WaitUtil.getElement(driver, By.xpath(button_SubmitSuggestedLearningForm), 5); 
		Thread.sleep(1000);
		if(submitSuggestedLearningForm.isEnabled()){
			submitSuggestedLearningForm.click();
			Thread.sleep(1000);
			List<WebElement> e = driver.findElements(By.xpath(text_DuplicateSuggestedLearnMsg));
			if(message.equals("Suggested Learning is successfully added.")){
				if(e.isEmpty()){
					String actualText = WaitUtil.getElement(driver, By.xpath(text_successSuggeLearnMsg), 10).getText();
					String expectedText = "Suggested Learning is successfully added.";
					Assert.assertEquals("Success Message not displayed/Suggested Learning not added succesfully", 
							expectedText, actualText);
				}else{
					for(WebElement ee:e){
						String expectedText = ee.getText();
						Thread.sleep(1000);
						WaitUtil.getElement(driver, By.xpath(button_closeSuggestedLearnForm), 5).click();
						Assert.assertEquals("You are trying to add duplicate sugested learning",expectedText , 
								"Suggested Learning already exists, add new one.");
					}
				}
			}else{
				for(WebElement ee:e){
					String expectedText = ee.getText();
					Thread.sleep(1000);
					WaitUtil.getElement(driver, By.xpath(button_closeSuggestedLearnForm), 5).click();
					Assert.assertEquals("Error message duplicate sugested learning does not match",expectedText , 
							"Suggested Learning already exists, add new one.");
				}
			}
		}else{
			Thread.sleep(1000);
			String actualText = WaitUtil.getElement(driver, By.xpath(text_errorMsgSuggestedLearn), 5).getText();
			Thread.sleep(1000);
			WaitUtil.getElement(driver, By.xpath(button_closeSuggestedLearnForm), 5).click();
			Assert.assertTrue("Error message does not match on Suggested Learning form", actualText.equals(message));
		}
	}
	
	@AfterTest
	public void closeDriver(){
		driver.quit();
	}
	
	@DataProvider
	public Object[][] getData() throws IOException{
		ReadExcelFile readExcelFile = new ReadExcelFile(); 
		String filePath = "/resource/testData";
		String fileName = "ManageSkill.xlsx";
		String sheetName = "SuggestedLearning";
		Object[][] data = readExcelFile.readExcel(filePath,fileName,sheetName);
		return data;	
	}
}
