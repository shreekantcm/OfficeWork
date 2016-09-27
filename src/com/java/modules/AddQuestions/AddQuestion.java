package com.java.modules.AddQuestions;

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

//all methods of this class are belong to "deploy" group.
@Test(groups="AddQuestion")
public class AddQuestion {
	static String links_allSkill = ReadLocatorsFromExcel.readExcel("links_allSkill", "AddQuestion");
	static String links_allSubSkill = ReadLocatorsFromExcel.readExcel("links_allSubSkill", "AddQuestion");
	static String link_topic = ReadLocatorsFromExcel.readExcel("link_topic", "AddQuestion");
	
	static String button_OpenAddSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenAddSkillForm", "AddQuestion");
	static String button_OpenAddSubSkillForm = ReadLocatorsFromExcel.readExcel("button_OpenAddSubSkillForm", "AddQuestion");
	static String button_OpenAddTopicForm = ReadLocatorsFromExcel.readExcel("button_OpenAddTopicForm", "AddQuestion");
	static String button_SubmitTopicForm = ReadLocatorsFromExcel.readExcel("button_SubmitTopicForm", "AddQuestion");
	static String button_SubmitAddSkillForm = ReadLocatorsFromExcel.readExcel("button_SubmitAddSkillForm", "AddQuestion");
	static String button_addOpenAddQueForm = ReadLocatorsFromExcel.readExcel("button_addOpenAddQueForm", "AddQuestion");
	static String button_saveAnswer = ReadLocatorsFromExcel.readExcel("button_saveAnswer", "AddQuestion");
	static String button_PublishQuestion = ReadLocatorsFromExcel.readExcel("button_PublishQuestion", "AddQuestion");
	static String button_PublishQuestionConfarm = ReadLocatorsFromExcel.readExcel("button_PublishQuestionConfarm", "AddQuestion");
	static String button_submitColumnABvalue = ReadLocatorsFromExcel.readExcel("button_submitColumnABvalue", "AddQuestion");
	static String button_addOption = ReadLocatorsFromExcel.readExcel("button_addOption", "AddQuestion");
	static String button_closeAddQuestionForm = ReadLocatorsFromExcel.readExcel("button_closeAddQuestionForm", "AddQuestion");
	static String button_questionPreview = ReadLocatorsFromExcel.readExcel("button_questionPreview", "AddQuestion");
	static String button_nextQuestionButton = ReadLocatorsFromExcel.readExcel("button_nextQuestionButton", "AddQuestion");
	static String button_editQuestion = ReadLocatorsFromExcel.readExcel("button_editQuestion", "AddQuestion");
	static String button_saveEditedQuestion = ReadLocatorsFromExcel.readExcel("button_saveEditedQuestion", "AddQuestion");
	static String button_saveChanges = ReadLocatorsFromExcel.readExcel("button_saveChanges", "AddQuestion");
	static String button_deleteTopic = ReadLocatorsFromExcel.readExcel("button_deleteTopic", "AddQuestion");
	
	static String input_columnA = ReadLocatorsFromExcel.readExcel("input_columnA", "AddQuestion");
	static String input_columnB = ReadLocatorsFromExcel.readExcel("input_columnB", "AddQuestion");
	static String input_questionIframe = ReadLocatorsFromExcel.readExcel("input_questionIframe", "AddQuestion");
	
	static String select_questionTypeDropDown  =ReadLocatorsFromExcel.readExcel("select_questionTypeDropDown", "AddQuestion");
	static String select_difficultiLevelDropDown  =ReadLocatorsFromExcel.readExcel("select_difficultiLevelDropDown", "AddQuestion");
	
	static String tab_addAnswer  =ReadLocatorsFromExcel.readExcel("tab_addAnswer", "AddQuestion");
	static String checkBox_correctAnsSingle = ReadLocatorsFromExcel.readExcel("checkBox_correctAnsSingle", "AddQuestion");

	static String radioButton_true = ReadLocatorsFromExcel.readExcel("radioButton_true", "AddQuestion");
	static String radioButton_false = ReadLocatorsFromExcel.readExcel("radioButton_false", "AddQuestion");
	
	static String text_selectDDErrorMessage = ReadLocatorsFromExcel.readExcel("text_selectDDErrorMessage", "AddQuestion"); 
	static String text_differentErrorMsgOnAddQueForm = ReadLocatorsFromExcel.readExcel("text_differentErrorMsgOnAddQueForm", 
			"AddQuestion");
	static String textErrorMessageAnswerOption = ReadLocatorsFromExcel.readExcel("textErrorMessageAnswerOption", "AddQuestion");
	static String text_questionType = ReadLocatorsFromExcel.readExcel("text_questionType", "AddQuestion");
	static String text_questionOnQuestionPreviewForm = ReadLocatorsFromExcel.readExcel("text_questionOnQuestionPreviewForm", 
			"AddQuestion");
	static String text_showingRecords = ReadLocatorsFromExcel.readExcel("text_showingRecords", "AddQuestion");
	static String text_queLevelOnPreviewQue = ReadLocatorsFromExcel.readExcel("text_queLevelOnPreviewQue", "AddQuestion");
	static String text_SuccMsgOfSaveModification = ReadLocatorsFromExcel.readExcel("text_SuccMsgOfSaveModification", "AddQuestion");
	static String text_totalNoOfQuestion = ReadLocatorsFromExcel.readExcel("text_totalNoOfQuestion", "AddQuestion");
	
	static String input_optionAswerField = ReadLocatorsFromExcel.readExcel("input_optionAswerField", "AddQuestion");
	static String input_correctAnwer = ReadLocatorsFromExcel.readExcel("input_correctAnwer", "AddQuestion");
	
	static String singleQuestion = ReadPropertiesFile.getPropValues("singleQuestion");
	
	//static int 2000 =  Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutMinimum"));  //  2 sec
	//static int timeOutMidiam =  Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutMidiam"));    //  5 sec
	//static int timeOutMaximum = Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutMaximum"));  //  10 sec
	//static int timeOutXMaximum = Integer.parseInt(ReadPropertiesFile.getPropValues("timeOutXMaximum")); // 20 sec
	
	Actions action = null;
	static int count =0;
	static int counter=0;
	static int count$sign=0;
	static int previewQuesCounter=0;
	
	public static WebDriver driver;
	
	@Test(priority=1)
	public void initializedDriver(){
		String drivername = ReadPropertiesFile.getPropValues("drivername");
		if(drivername.equalsIgnoreCase("ie32") || drivername.equalsIgnoreCase("ie64")){
			Assert.assertTrue("This whole test case not working for IE browser becase TC contain " +
					"some action like Switch on the iframe which is not working on IE", false);
		}
		driver = DriverUtil.getDriver(driver,drivername);
		Log.info("----------------- Test Case : AddQuestion ------------------- ");
		action = new Actions(driver);
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
	// This function will check and create skill, sub skill, topic if not present
	// We are creating here Hard coded skill, sub skill and topic 
	@Test(priority=5)
	public void checkAndCreate_Skill_SubSkill_Topic() throws InterruptedException{
		CommanFunctions commanFunctions = new CommanFunctions();
		Log.info("$$ START FUNCTION : AddQuestion.checkAndCreate_Skill_SubSkill_Topic()");
		//Check if "AddQuestion_Skill" Skill is all ready present if not then add new skill
		String skill = "AddQuestion_Skill";
		List<WebElement> allSkil= driver.findElements(By.xpath(links_allSkill));
		boolean foundSkill = false; 
		foundSkill = checkIfElementPresentInList(allSkil,skill);
		Log.info("Checkde if skill = "+skill+" is present in DB or not");
		if(!foundSkill){
			Log.info("Skill ="+skill+" Not found in DB");
			commanFunctions.addSkillOrSubSkillOrTopic(skill, "skill Description","Skill is successfully added.",
					By.xpath(button_OpenAddSkillForm),By.xpath(button_SubmitAddSkillForm),driver);
			Log.info("Skill ="+skill+" Added in DB");
			List<WebElement> allSkilAfterSkillAdded= driver.findElements(By.xpath(links_allSkill));
			boolean foundSkillAfterSkillAdded=false;
			Log.info("Checking if Skill ="+skill+" is succesfully added in DB?");
			foundSkillAfterSkillAdded = checkIfElementPresentInList(allSkilAfterSkillAdded,skill);
			Assert.assertTrue(" [AddQuestion_Skill] is not added succesfully", foundSkillAfterSkillAdded);
			Log.info("Skill ="+skill+" is succesfully added in DB?");
		}
		//Thread.sleep(2000);
		//Check if "AddQuestion_Sub_Skill" Skill is all ready present if not then add new skill
		String subSkill = "AddQuestion_Sub_Skill";
		List<WebElement> allSubSkil= driver.findElements(By.xpath(links_allSubSkill));
		boolean foundSubSkill = false;
		Log.info("Check if subskill = AddQuestion_Sub_Skill is present in DB?");
		foundSubSkill = checkIfElementPresentInList(allSubSkil,subSkill);
		
		if(!foundSubSkill){
			Log.info("subskill = AddQuestion_Sub_Skill is not present in DB");
			commanFunctions.addSkillOrSubSkillOrTopic(subSkill, "Sub skill Description","SubSkill is successfully " +
					"added.",By.xpath(button_OpenAddSubSkillForm),By.xpath(button_SubmitAddSkillForm),driver);
			
			List<WebElement> allSubSkilAfterSkillAdded= driver.findElements(By.xpath(links_allSubSkill));
			boolean foundSubSkillAfterSubSkillAdded = false;
			foundSubSkillAfterSubSkillAdded = checkIfElementPresentInList(allSubSkilAfterSkillAdded,subSkill);
			Assert.assertTrue(" [AddQuestion_Sub_Skill] is not added succesfully", foundSubSkillAfterSubSkillAdded);
			Log.info("subskill = AddQuestion_Sub_Skill is succesfully added in DB");
		}
		//Thread.sleep(2000);
		//Check if "AddQuestion_Topic" Skill is all ready present if not then add new skill
		String topic = "AddQuestion_Topic";
		List<WebElement> allTopic= driver.findElements(By.xpath(link_topic));
		boolean foundTopic = false;
		Log.info("Check if topic ='AddQuestion_Topic' present in DB");
		foundTopic = checkIfElementPresentInList(allTopic,topic);
				
		if(!foundTopic){
			Log.info(" topic ='AddQuestion_Topic' Not present in DB.");
			commanFunctions.addSkillOrSubSkillOrTopic(topic, "Topic Description","Topic is successfully added.",
					By.xpath(button_OpenAddTopicForm),By.xpath(button_SubmitTopicForm),driver);
			//Thread.sleep(2000);
			List<WebElement> allTopicAfterTopicAdded= driver.findElements(By.xpath(link_topic));
			boolean foundTopicAfterTopicAdded = false;
			foundTopicAfterTopicAdded = checkIfElementPresentInList(allTopicAfterTopicAdded,topic);
			Assert.assertTrue(" [AddQuestion_Topic] is not added succesfully", foundTopicAfterTopicAdded);
		}else{
			Log.info(" topic ='AddQuestion_Topic' is present in DB.");
			
			Thread.sleep(2000);
			WebElement deleteTopicButton = WaitUtil.getElement(driver, By.xpath(button_deleteTopic), 2000);
			action.moveToElement(deleteTopicButton).click().build().perform();
			
			Log.info(" topic ='AddQuestion_Topic' is deleted succesfully");
			
			Thread.sleep(3000);
			WaitUtil.getElement(driver, By.xpath("/html/body/div[4]/div/div/div/div[3]/button[2]"), 2000).click();
			Thread.sleep(3000);
			commanFunctions.addSkillOrSubSkillOrTopic(topic, "Topic Description","Topic is successfully added.",
					By.xpath(button_OpenAddTopicForm),By.xpath(button_SubmitTopicForm),driver);
			//Thread.sleep(2000);
			List<WebElement> allTopicAfterTopicAdded= driver.findElements(By.xpath(link_topic));
			boolean foundTopicAfterTopicAdded = false;
			foundTopicAfterTopicAdded = checkIfElementPresentInList(allTopicAfterTopicAdded,topic);
			Assert.assertTrue(" [AddQuestion_Topic] is not added succesfully", foundTopicAfterTopicAdded);
			
			Log.info(" topic ='AddQuestion_Topic' is added succesfully in DB");
		}
		Log.info("$$ END FUNCTION : AddQuestion.checkAndCreate_Skill_SubSkill_Topic()");
	}
	@Test(priority=6,dataProvider="getData")
	public void addQuestion(String QuestionType, String DifficultyLevel) throws InterruptedException{
		
		Log.info("$$ START FUNCTION : AddQuestion.addQuestion()");
		Log.info("QuestionType="+QuestionType+", DifficultyLevel="+DifficultyLevel);
		
		String QuesType = QuestionType;
        switch (QuesType) {
            case "Single":
            		singleQestionType(QuesType, DifficultyLevel);
                    break;
           case "Multiple":
            		multipleQestionType(QuesType, DifficultyLevel);
                    break;
           case "Subjective":
            	subjectiveQestionType(QuesType, DifficultyLevel);
                break;
           case "Fill in the blank":
            		fillBlankQestionType(QuesType, DifficultyLevel);
                    break;
            case "True or False":
            		trueFalseQestionType(QuesType, DifficultyLevel);
            		break;
            case "Match the following":
            		matchTheFollowing(QuesType, DifficultyLevel);
            		break;
            case "Shuffle":
            		shuffleQestionType(QuesType, DifficultyLevel);
            		break;
            case "Micro text":
        		microTextQestionType(QuesType, DifficultyLevel);
        	break;
            case "Drag and Drop":
        		dragDropQestionType(QuesType, DifficultyLevel);
        		break;
           default: 
                    Assert.assertTrue("Question type = ["+QuestionType+"] did not match in switch case", false);
            	break;  
        }
        Log.info("$$ END FUNCTION : AddQuestion.addQuestion()");
	}
	
	// This function will close Add Question form and Open question preview 
	@Test(priority=7,dependsOnMethods="addQuestion")
	public void closeAddQuePopNOpenPreviePopUP() throws InterruptedException{
		action.moveByOffset(100,100).click().build().perform();
		//WaitUtil.getElement(driver, By.xpath("//button[@id='closeQuestion']"), timeOutMidiam);
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.xpath(button_questionPreview), 5).click();
		Thread.sleep(2000);
		Log.info("$$ END FUNCTION : AddQuestion.closeAddQuePopNOpenPreviePopUP()");
	}
	
	// This function will open the preview question form and one by one verify the Question and its respective   
	// answer options are the same or not 
	@Test(priority=8,dataProvider="getData",dependsOnMethods="closeAddQuePopNOpenPreviePopUP")
	public void verifyAndEditQuestions(String QuestionType, String DifficultyLevel) throws InterruptedException{
		Thread.sleep(5000);
		Log.info("$$ START FUNCTION : AddQuestion.verifyAndEditQuestions(QuestionType="+QuestionType+", DifficultyLevel="+DifficultyLevel+")");
		String totalNoOfQuestion = WaitUtil.getElement(driver, By.xpath(text_totalNoOfQuestion), 2000).getText();
		if(previewQuesCounter==0){
			Log.info("=======================totalNoOfQuestion="+totalNoOfQuestion);
			Assert.assertEquals("errorcode1025 - Total no of question lable shoul not be match", "Total Questions:9", 
					totalNoOfQuestion.trim());
		}
		
		previewQuesCounter++;
		//String showingRecordsInfo = WaitUtil.getElement(driver, By.xpath(text_showingRecords), 5).getText(); 	
		//System.out.println("Showing "+previewQuesCounter+" of 9");
		//System.out.println("showingRecordsInfo"+showingRecordsInfo);
		//Assert.assertEquals("errorcode1021-Record does not matched, standard record shoul be ='Showing "+previewQuesCounter+" of 9'", "Showing "+previewQuesCounter+" of 9", showingRecordsInfo);
		
		String QuesType = QuestionType;
        switch (QuesType) {
            case "Single":
            		String question = singleQuestion;
            		String[] answer = ReadPropertiesFile.getPropValues("singleAnswer").split(",");
            		verifyPreviewQues(QuestionType,DifficultyLevel,question);
            		
                    break;
           case "Multiple":
        	   		verifyPreviewQues(QuestionType,DifficultyLevel,ReadPropertiesFile.getPropValues("multipleQuestion"));
        	   		
                    break;
           case "Subjective":
        	   		verifyPreviewQues(QuestionType,DifficultyLevel,ReadPropertiesFile.getPropValues("subjectiveQuestion"));
            		//subjectiveQestionType(QuesType, DifficultyLevel);
                break;
           case "Fill in the blank":
        	   		verifyPreviewQues(QuestionType,DifficultyLevel,ReadPropertiesFile.getPropValues("fillInTheBlankQuestion"));
            		//fillBlankQestionType(QuesType, DifficultyLevel);
                    break;
            case "True or False":
            		verifyPreviewQues(QuestionType,DifficultyLevel,ReadPropertiesFile.getPropValues("trueOrFalseQuestion"));
            		//trueFalseQestionType(QuesType, DifficultyLevel);
            		break;
            case "Match the following":
            		verifyPreviewQues(QuestionType,DifficultyLevel,ReadPropertiesFile.getPropValues("matchTheFollowingQuestion"));
            		//matchTheFollowing(QuesType, DifficultyLevel);
            		break;
            case "Shuffle":
            		verifyPreviewQues(QuestionType,DifficultyLevel,ReadPropertiesFile.getPropValues("shuffleQestion"));
            		//shuffleQestionType(QuesType, DifficultyLevel);
            		break;
            case "Micro text":
            		verifyPreviewQues(QuestionType,DifficultyLevel,ReadPropertiesFile.getPropValues("microTexQuestion"));
            		//microTextQestionType(QuesType, DifficultyLevel);
        	break;
            case "Drag and Drop":
            		verifyPreviewQues(QuestionType,DifficultyLevel,ReadPropertiesFile.getPropValues("dragAndDropQuestion"));
        			//dragDropQestionType(QuesType, DifficultyLevel);
        		break;
           default: 
                    Assert.assertTrue("Question type = ["+QuestionType+"] did not match in Preview Question switch case", false);
            	break;  
        }
        Log.info("$$ END FUNCTION : AddQuestion.verifyAndEditQuestions()");
	}
	
	// This function will verify the arguments questionType/difficultyLevel/question are on Preview Question form
	// are matching or not
	private void verifyPreviewQues(String questionType, String difficultyLevel,
			String question) throws InterruptedException {
		Log.info("$$ START FUNCTION : AddQuestion.verifyPreviewQues(questionType="+questionType+", difficultyLevel" +
				"="+difficultyLevel+", question="+question+")");
		
		if(!questionType.equalsIgnoreCase("Single")){
			WaitUtil.getElement(driver, By.xpath(button_nextQuestionButton), 2000).click();
		}
        
		// Verify Question type
		String actualQuesType = WaitUtil.getElement(driver, By.xpath(text_questionType), 2000).getText();
		Assert.assertTrue("errorcode1022-Question Type = {"+questionType+"} is not match on Question Preview form/pop up", actualQuesType.equals(questionType));
		
		// Verify Question Level
		String actualQuesLevel = WaitUtil.getElement(driver, By.xpath(text_queLevelOnPreviewQue), 2000).getText();
		Assert.assertTrue("errorcode1023-Difficulty Level = {"+difficultyLevel+"} is not match on Question Preview form/pop up", actualQuesLevel.equals(difficultyLevel));
		
		// Verify Question
		String actualQuestion = WaitUtil.getElement(driver, By.xpath(text_questionOnQuestionPreviewForm), 2000).getText();
		Assert.assertTrue("errorcode1024-Question = {"+actualQuestion+"} is not match on Question Preview form/pop up", actualQuestion.equals(question));
		
		// Modify and Verify modified Question
		WaitUtil.getElement(driver, By.xpath(button_editQuestion), 2000).click();
		Thread.sleep(2000);
		
		driver.switchTo().frame(0);
		WaitUtil.getElement(driver, By.xpath(input_questionIframe), 2000).sendKeys("EDITEDQUESTION");
		Thread.sleep(3000);
		driver.switchTo().defaultContent();
		WaitUtil.getElement(driver, By.xpath(button_saveEditedQuestion), 2000).click();
		
		Assert.assertTrue("errorcode1025-Edited question does not match", WaitUtil.getElement(driver, By.xpath(text_questionOnQuestionPreviewForm), 
				2000).getText().contains("EDITEDQUESTION"));
		
		WaitUtil.getElement(driver, By.xpath(button_saveChanges), 2000).click();
		Thread.sleep(2000);
		String actualSuccessMessage = WaitUtil.getElement(driver, By.xpath(text_SuccMsgOfSaveModification), 3000).getText();
		String expectedSuccessMessage = "Question modified Successfully.";
		
		Assert.assertTrue("errorcode1026- 'Question modified Successfully' does not match", 
				actualSuccessMessage.equals(expectedSuccessMessage));
	}

	private void singleQestionType(String quesType, String DifficultyLevel) throws InterruptedException {
		Log.info("$$ START FUNCTION : AddQuestion.singleQestionType(quesType="+quesType+"DifficultyLevel="+DifficultyLevel+")");
		Thread.sleep(3000);
		WaitUtil.waitForElementToBeClickable(driver, By.xpath(button_addOpenAddQueForm), 10);
		WaitUtil.getElement(driver, By.xpath(button_addOpenAddQueForm), 5).click();
		
		Log.info("Add Question form opened");
		
		String question = singleQuestion;
		selectAllDropDownAndAddQuestion(quesType, DifficultyLevel, question);
		
		// Error message validation - Aswer Tab
		Log.info("1");
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(2000);
		Log.info("2");
		String actualErrMsg = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm), 5).getText();
		Log.info("3");
		String expcErrMsg = "For this question type, at least two question options are mandatory.";
		Log.info("4");
		Assert.assertTrue("errorcode1012 - error message "+expcErrMsg+" does not matched", actualErrMsg.equals(expcErrMsg));
		Log.info("5");
		String[] answer = ReadPropertiesFile.getPropValues("singleAnswer").split(",");
		for(int i=0;i<answer.length;i++){
			if(answer[i].contains("$")){
				count$sign++;
				WaitUtil.getElement(driver, By.xpath(checkBox_correctAnsSingle), 5).click();
				Log.info("6");
			}
			driver.switchTo().frame(1);
			Log.info("7");
			WaitUtil.getElement(driver, By.xpath(input_questionIframe), 10).sendKeys(answer[i].replace("$", ""));
			Log.info("8");
			driver.switchTo().defaultContent();
			Log.info("9");
			WebElement saveButton =WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5);
			Log.info("10");
			saveButton.click();
			Log.info("11");
		}
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Log.info("12");
		Thread.sleep(3000);
		if(count$sign>=2){
			Log.info("13");
			String actualErrMsg1 = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm), 5).getText();
			String expcErrMsg1 = "Only one Correct Option allowed for Single Type Question.";
			Assert.assertTrue("errorcode1013 - error message "+expcErrMsg1+" does not matched", actualErrMsg1.equals(expcErrMsg1));
			Assert.assertTrue("errorcode1014 - In Single question type only one correct anwer allowed." +
					"In properties file 'singleAnswer' should not contain more than one $ sign.Will fail all the test cases.", actualErrMsg1.equals(expcErrMsg1));
			Log.info("14");
		}
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestionConfarm), 5).click();
		Log.info("15");
		Log.info("$$ START FUNCTION : AddQuestion.singleQestionType()");
	}
	
	private void multipleQestionType(String quesType, String DifficultyLevel) throws InterruptedException {
		Thread.sleep(3000); // Important
		String question = ReadPropertiesFile.getPropValues("multipleQuestion");
		selectAllDropDownAndAddQuestion(quesType, DifficultyLevel, question);
		
		// Error message validation - Answer Tab
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(2000);
		String actualErrMsg = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm), 5).getText();
		String expcErrMsg = "For this question type, at least two question options are mandatory.";
		Assert.assertTrue("errorcode1016 - error message = ["+expcErrMsg+"] does not matched", actualErrMsg.equals(expcErrMsg));
		
		String[] answer = ReadPropertiesFile.getPropValues("multipleAnswer").split(",");
		
		for(int i=0;i<answer.length;i++){
			if(answer[i].contains("$")){
				WaitUtil.getElement(driver, By.xpath(checkBox_correctAnsSingle), 5).click();
			}
			driver.switchTo().frame(1);
			WaitUtil.getElement(driver, By.xpath(input_questionIframe), 10).sendKeys(answer[i].replace("$", ""));
			
			driver.switchTo().defaultContent();
			WebElement saveButton =WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5);
			saveButton.click();
		}
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestionConfarm), 5).click();
	}
	
	private void subjectiveQestionType(String quesType, String difficultyLevel) throws InterruptedException {
		Thread.sleep(3000); // Important
		String question = ReadPropertiesFile.getPropValues("subjectiveQuestion");
		selectAllDropDownAndAddQuestion(quesType, difficultyLevel, question);
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestionConfarm), 5).click();
	}
	
	private void fillBlankQestionType(String quesType, String DifficultyLevel) throws InterruptedException {
		Thread.sleep(3000); // Important
		String question = ReadPropertiesFile.getPropValues("fillInTheBlankQuestion");
		
		selectAllDropDownAndAddQuestion(quesType, DifficultyLevel,question);
		
		// Error message validation - Answer Tab
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(2000);
		String actualErrMsg = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm), 5).getText();
		String expcErrMsg = "Please Enter Correct answer";
		Assert.assertTrue("errorcode1015 - error message = ["+expcErrMsg+"] does not matched", actualErrMsg.equals(expcErrMsg));
				
		String[] answer = ReadPropertiesFile.getPropValues("fillInTheBlankAnswer").split(",");
		Thread.sleep(5000);
		WaitUtil.getElement(driver, By.xpath("//div[@class='row row-gap ng-scope']//input"), 20);
		List<WebElement> list = driver.findElements(By.xpath("//div[@class='row row-gap ng-scope']//input"));
		for(int i=0;i<answer.length;i++){
			list.get(i).sendKeys(answer[i]);
		}
		WebElement saveButton =WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5);
		saveButton.click();
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestionConfarm),5).click();
	}
	
	private void trueFalseQestionType(String quesType, String DifficultyLevel) throws InterruptedException {
		Thread.sleep(3000); // Important
		String question = ReadPropertiesFile.getPropValues("trueOrFalseQuestion");
		
		selectAllDropDownAndAddQuestion(quesType, DifficultyLevel, question);
		
		// Error message validation - Answer Tab
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(2000);
		String actualErrMsg = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm), 5).getText();
		String expcErrMsg = "Please Enter Correct answer";
		Assert.assertTrue("errorcode1017 - error message = ["+expcErrMsg+"] does not matched", actualErrMsg.equals(expcErrMsg));
		
		// Code which will add answers option
		String answer = ReadPropertiesFile.getPropValues("trueOrFalseAnswer");
		if(answer.equalsIgnoreCase("true")){
			WaitUtil.getElement(driver, By.xpath(radioButton_true), 5).click();
		}else{
			WaitUtil.getElement(driver, By.xpath(radioButton_false), 5).click();
		}
		WebElement saveButton =WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5);
		saveButton.click();
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestionConfarm),5).click();
	}
	
	public void matchTheFollowing(String quesType,String DifficultyLevel) throws InterruptedException{
		Thread.sleep(3000); // Important
		String question = ReadPropertiesFile.getPropValues("matchTheFollowingQuestion");
		selectAllDropDownAndAddQuestion(quesType, DifficultyLevel, question);
		
		// clicked on save button to validate error message of the mandatory question option 
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer),10).click();
		Thread.sleep(2000);
		String actualErrMsg = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm),5).getText();
		String expectedErrMsg = "For this question type, at least two question options are mandatory.";
		Assert.assertTrue("errorcode1001 - 'mandatory question option' error message does not matched.", 
				actualErrMsg.equalsIgnoreCase(expectedErrMsg));
		
		//click on Column B input box and check error message displayed for blank field
		driver.findElement(By.xpath(input_columnB)).click();
		driver.findElement(By.xpath(input_columnA)).click();
		Thread.sleep(2000);
		String actualErrMsgAnsRequire = WaitUtil.getElement(driver, By.xpath(textErrorMessageAnswerOption),10).getText();
		String expectedErrMsgAnsRequire = "Answer is required";
		Assert.assertTrue("errorcode1002 - error message 'Answer is required' does not match", 
				actualErrMsgAnsRequire.equals(expectedErrMsgAnsRequire));
		//click on column A inut and check error message displayed for blank field
		driver.findElement(By.xpath(input_columnB)).click();
		driver.findElement(By.xpath(input_columnB)).sendKeys("B");
		driver.findElement(By.xpath(input_columnA)).click();
		
		Thread.sleep(2000);
		String actualErrMsgOptionRequire = WaitUtil.getElement(driver, By.xpath(textErrorMessageAnswerOption),10).getText();
		String expectedErrMsgOptionRequire = "Option is required";
		Assert.assertTrue("errorcode1007 - error message 'Option is required' does not match", 
				actualErrMsgOptionRequire.equals(expectedErrMsgOptionRequire));
		
		driver.findElement(By.xpath(input_columnA)).sendKeys("A");
		WaitUtil.getElement(driver, By.xpath(button_submitColumnABvalue),10).click();
		driver.findElement(By.xpath(input_columnA)).sendKeys("AA");
		driver.findElement(By.xpath(input_columnB)).sendKeys("BB");
		WaitUtil.getElement(driver, By.xpath(button_submitColumnABvalue),10).click();
		
		WaitUtil.getElement(driver, By.xpath("//div[3]//div//div//div[2]//div//input"),10).clear();
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5).click();
		Thread.sleep(2000);
		String expErrMsgOptionNotBlank = "For this question type, options cannot be blank.";
		String actualErrMsgOptionNotBlank = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm),
				5).getText();
		Assert.assertTrue("errorcode1006 - error message 'For this question type, options cannot be blank.' " +
				"does not match", 
				expErrMsgOptionNotBlank.equals(actualErrMsgOptionNotBlank));
		Thread.sleep(2000);
		WaitUtil.getElement(driver, By.xpath("//button[@ng-click='removeShuffleOption($index)']"),50).click();
		Thread.sleep(2000);
		WaitUtil.getElement(driver, By.xpath("//button[@ng-click='removeShuffleOption($index)']"),50).click();
		// Check if user enter text in column B and keep blank column A then by default text "NA" -
		// -should print in column A
		/*WaitUtil.getElement(driver,By.xpath(input_columnB),5).sendKeys("B");
		WaitUtil.getElement(driver,By.xpath(button_submitColumnABvalue),5).click();
		Thread.sleep(2000);
		WaitUtil.getElement(driver,By.xpath("//div[3]//div//div//div[2]//div//" +
				"input[@placeholder='add option']"),5).click();
		String actualTextNA = WaitUtil.getElement(driver,By.xpath("//div[3]//div//div//div[2]//div//" +
				"input[@placeholder='add option']"),5).getText();
		
		Assert.assertTrue("errorcode1003 - text 'NA' does not match", actualTextNA.equals("NA"));*/
		
		String[] columnA = ReadPropertiesFile.getPropValues("matchTheFollowingColA").split(",");
		String[] columnB = ReadPropertiesFile.getPropValues("matchTheFollowingColB").split(",");
		
		if(columnA.length==columnB.length){
			for(int i=0;i<columnA.length;i++){
				driver.findElement(By.xpath(input_columnA)).sendKeys(columnA[i]);
				driver.findElement(By.xpath(input_columnB)).sendKeys(columnB[i]);
				WaitUtil.getElement(driver, By.xpath(button_submitColumnABvalue),10).click();
			}
		}
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5).click();
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestionConfarm), 5).click();
	}
	
	public void shuffleQestionType(String quesType,String DifficultyLevel) throws InterruptedException{
		Thread.sleep(3000); // Important
		String question = ReadPropertiesFile.getPropValues("shuffleQestion");
		selectAllDropDownAndAddQuestion(quesType, DifficultyLevel, question);
		
		// clicked on save button to validate error message of the mandatory question option 
		// clicked on option field to validate the blank field error message
		WaitUtil.getElement(driver, By.xpath(input_optionAswerField), 5).click();
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer),5).click();
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer),5).click();
		Thread.sleep(2000);
		String actualErrMsg = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm),7).getText();
		String expectedErrMsg = "For this question type, at least two question options are mandatory.";
		Assert.assertTrue("errorcode1004 - 'mandatory question option' error message does not matched.", 
		actualErrMsg.equalsIgnoreCase(expectedErrMsg));
		
		String actualErrMsgOptionReq = WaitUtil.getElement(driver, By.xpath(textErrorMessageAnswerOption),7).getText();
		String expectedErrMsgOptionReq = "Option is required";
		Assert.assertTrue("errorcode1005 - 'mandatory question option' error message does not matched.", 
				actualErrMsgOptionReq.equalsIgnoreCase(expectedErrMsgOptionReq));
		
		// Adding answers 
		String answer = ReadPropertiesFile.getPropValues("shuffleAnswer");
		String[] ans = answer.split(",");
		
		for(int i=0;i<ans.length;i++){
			WaitUtil.getElement(driver, By.xpath(input_optionAswerField), 5).sendKeys(ans[i]);
			WaitUtil.getElement(driver, By.xpath(button_addOption), 5).click();
		}
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5).click();
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestionConfarm), 5).click();
	}

	private void microTextQestionType(String quesType, String difficultyLevel) throws InterruptedException {
		Thread.sleep(3000); // Important
		String question = ReadPropertiesFile.getPropValues("microTexQuestion");
		selectAllDropDownAndAddQuestion(quesType, difficultyLevel, question);
		
		WaitUtil.getElement(driver, By.xpath(input_correctAnwer), 5).click();
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5).click();
		WaitUtil.getElement(driver, By.xpath(input_correctAnwer), 5).click();
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5).click();
		
		Thread.sleep(3000);
		String actualErrMsgAddAnswer = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm), 
				5).getText();
		String expErrMsgAddAnswer = "Please add correct answer";
		Assert.assertTrue("errorcode1008 - Erroe message 'Please add correct answer' does not matched", 
				expErrMsgAddAnswer.equals(actualErrMsgAddAnswer));
		
		String actualErrMsgAnswerRequire = WaitUtil.getElement(driver, By.xpath(textErrorMessageAnswerOption), 
				5).getText();
		String expErrMsgAnswerRequire = "Answer is required";
		Assert.assertTrue("errorcode1009 - Erroe message 'Answer is required' does not matched", 
				actualErrMsgAnswerRequire.equals(expErrMsgAnswerRequire));
		
		String answer = ReadPropertiesFile.getPropValues("microTexAnswer");
		WaitUtil.getElement(driver, By.xpath(input_correctAnwer), 5).sendKeys(answer);
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5).click();
		Thread.sleep(1000);
		WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5).click();
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestionConfarm), 5).click();
	}
	
	private void dragDropQestionType(String quesType, String difficultyLevel) throws InterruptedException {
		Thread.sleep(3000); // Important
		String question = ReadPropertiesFile.getPropValues("dragAndDropQuestion");
		selectAllDropDownAndAddQuestion(quesType, difficultyLevel, question);
		
		String[] separateQuestion = question.split(",");
		for(int i=0;i<separateQuestion.length;i++){
			if(separateQuestion[i].contains("xxxx") || separateQuestion[i].contains("XXXX")){
				counter++; // Count how many no of time xxxx or XXXX comes in question
			}
		}
		Thread.sleep(2000);	
		String[] answer = ReadPropertiesFile.getPropValues("dragAndDropAnswer").split(",");
		Assert.assertTrue("errorcode1010 - Total number of answers input fields in answer tab are not equals to the times of xxxx present " +
				"in questions", answer.length==counter);
		
		WaitUtil.getElement(driver, By.xpath("//thead[@id='thead']"), 2).click();
		Thread.sleep(3000);
		List<WebElement> inputFields = driver.findElements(By.xpath("//div[@ng-if='!blank.isBluffOption']//input"));
		
		Assert.assertTrue("errorcode1011 - Total number of answerS are not equals to anserS input field generated " +
				"in answers tab", (answer.length) == (inputFields.size()));
		
		for(int i=0;i<answer.length;i++){
			inputFields.get(i).sendKeys(answer[i]);
		}
		WebElement saveButton =WaitUtil.getElement(driver, By.xpath(button_saveAnswer), 5);
		saveButton.click();
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestionConfarm), 5).click();
	}
	
	public boolean checkIfElementPresentInList(List<WebElement> list,String skillEdit) throws InterruptedException{
		Log.info("$$ START FUNCTION: AddQuestion.checkIfElementPresentInList()");
		boolean findElement=false;
		for(WebElement listElements:list){
			if(listElements.getText().equals(skillEdit)){
				Thread.sleep(1000);
				listElements.click();
				Log.info("Element {"+skillEdit+"} found in list and now it is clicked/Selected");
				findElement=true;
				break;
			}
		}
		Log.info("$$ END FUNCTION: AddQuestion.checkIfElementPresentInList()");
		return findElement;
	}
	
	// This Function called for the all type of question. Function will select question type and difficulty
	// level drop down also enter the question in question input box.This function will validate and check 
	// the error messages of select question and difficulty level drop down and blank question filed
	public void selectAllDropDownAndAddQuestion(String quesType,String difficultyLevel,String question) 
			throws InterruptedException{
		Log.info("$$ START FUNCTION : AddQuestion.selectAllDropDownAndAddQuestion(quesType="+quesType+",difficultyLevel=" +
				""+difficultyLevel+",question="+question+")");
		
		WebElement queTypeDD1 = WaitUtil.getElement(driver, By.xpath(select_questionTypeDropDown), 8000);
		WaitUtil.waitForElementToBeClickable(driver, By.xpath(select_questionTypeDropDown), 5000);
		action.moveToElement(queTypeDD1).click().build().perform();
		WebElement queTypelDD1 = WaitUtil.getElement(driver, By.xpath("//li[text()='--Select--']"), 5000);
		action.moveToElement(queTypelDD1).click().build().perform();
		
		WaitUtil.getElement(driver, By.xpath(select_difficultiLevelDropDown), 1).click();
		//Thread.sleep(2000);
		WaitUtil.getElement(driver, By.xpath("//ul[@id='ui-id-5']//li[contains(.,'Select')]"),5000).click();
		
		// Error message validation of Select question type drop down
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion),3000).click();
		Thread.sleep(2000);
		String actualErrMsgQuestionTypeDD = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm),7).getText();
		String expectedErrMsgQuestionTypeDD = "Please select Option Type for the Report";
		Assert.assertTrue("errorcode1000 - Select Question type drop down error message does not matched", 
				actualErrMsgQuestionTypeDD.equalsIgnoreCase(expectedErrMsgQuestionTypeDD));
		
		// Select Question type drop down
		WebElement queTypeDD2 = WaitUtil.getElement(driver, By.xpath(select_questionTypeDropDown), 20);
		WaitUtil.waitForElementToBeClickable(driver, By.xpath(select_questionTypeDropDown), 10);
		action.moveToElement(queTypeDD2).click().build().perform();
		WebElement queTypelDD2 = WaitUtil.getElement(driver, By.xpath("//li[text()='"+quesType+"']"), 10);
		action.moveToElement(queTypelDD2).click().build().perform();
		
		// Error message validation of select Difficulty Level drop down
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 10).click();
		Thread.sleep(2000);
		String actualErrMsgSelectDifficultyLevelDD = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm), 5).getText();
		String expectedErrMsgSelectDifficultyLevelDD = "Please select Level for the Report";
		Assert.assertTrue("Select Question type drop down error message does not matched", 
				actualErrMsgSelectDifficultyLevelDD.equalsIgnoreCase(expectedErrMsgSelectDifficultyLevelDD));		
		
		// Select Difficulty Level drop down
		WaitUtil.getElement(driver, By.xpath(select_difficultiLevelDropDown), 1).click();
		WaitUtil.getElement(driver, By.xpath("//li[text()='"+difficultyLevel+"']"),10).click();
		
		// Error message validation if Question not entered
		WaitUtil.getElement(driver, By.xpath(button_PublishQuestion), 5).click();
		Thread.sleep(2000);
		String actualErrMsgEnterQuestion = WaitUtil.getElement(driver, By.xpath(text_differentErrorMsgOnAddQueForm),
				5).getText();
		String expectedErrMsgEnterQuestion = "Please Enter some Question Text.";
		Assert.assertTrue("Select Question type drop down error message does not matched", 
				actualErrMsgEnterQuestion.equalsIgnoreCase(expectedErrMsgEnterQuestion));	
		
		// Enter Question 
		Thread.sleep(1000);
		driver.switchTo().frame(0);
		WaitUtil.getElement(driver, By.xpath(input_questionIframe), 10).sendKeys(question);
		driver.switchTo().defaultContent();
		if(!quesType.equalsIgnoreCase("Subjective")){
			WaitUtil.getElement(driver, By.xpath(tab_addAnswer), 10).click();
		}
		Log.info("$$ END FUNCTION : AddQuestion.selectAllDropDownAndAddQuestion()");
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
		String sheetName = "AddQuestion";
		Object data[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return data;
	}
}
