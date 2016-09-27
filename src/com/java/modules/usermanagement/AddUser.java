package com.java.modules.usermanagement;

import static org.testng.AssertJUnit.assertTrue;
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
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadExcelFile;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
import com.java.common.WaitUtil;

public class AddUser{
	static String newMsg = "";
	static int count=0;
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
	
	String widget_add = ReadLocatorsFromExcel.readExcel("widget_add","ManageUser");
	String button_Go = ReadLocatorsFromExcel.readExcel("button_Go", "ManageUser");
	
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
	
	@Test(dataProvider = "getUserData",priority=3)
	public void addUser(String userId,String firstName,String lastName,String emailID,String password,
			String confPassword,String mobileNo,String altrEmaiID,String status,String privilege,String 
			role,String msg) throws InterruptedException{
		String role1 = role;
		//count++;
		//System.out.println("Count value = "+count);
		NavigationUtil.navigationOnSubMenu("User", "Manage",driver);
		Thread.sleep(3000);
		WaitUtil.waitForElementXPATH(widget_add,driver);
		driver.findElement(By.xpath(widget_add)).click();
		Thread.sleep(3000);
		WaitUtil.getElement(driver, By.id("firstNameId"), 10);
		try{
		driver.findElement(By.id("firstNameId")).click();	
		driver.findElement(By.id("firstNameId")).sendKeys(firstName);
		
		driver.findElement(By.id("loginNameId")).click();
		driver.findElement(By.id("loginNameId")).sendKeys(userId);
		
		driver.findElement(By.id("lastNameId")).click();
		driver.findElement(By.id("lastNameId")).sendKeys(lastName);
		
		driver.findElement(By.id("inputEmailId")).click();
		driver.findElement(By.id("inputEmailId")).sendKeys(emailID);
		//password
		driver.findElement(By.xpath("/html/body/div[7]/div/div/form/div[2]/div[8]/div/input")).click();
		driver.findElement(By.xpath("/html/body/div[7]/div/div/form/div[2]/div[8]/div/input")).sendKeys(password);
		//conform password
		driver.findElement(By.xpath(".//*[@id='formId']/div[2]/div[8]/div[2]/input")).click();
		driver.findElement(By.xpath(".//*[@id='formId']/div[2]/div[8]/div[2]/input")).sendKeys(confPassword);
		
		driver.findElement(By.id("phoneId")).click();
		driver.findElement(By.id("phoneId")).sendKeys(mobileNo);
		
		driver.findElement(By.xpath("//input[@placeholder='Alternate Email ID']")).clear(); 
		driver.findElement(By.xpath("//input[@placeholder='Alternate Email ID']")).sendKeys(altrEmaiID);
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/div[7]/div/div/form/div[2]/div[12]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Active')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(".//*[@id='formId']/div[2]/div[10]/div[2]/div[1]/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//li[@class='ng-scope']/a[text()='"+privilege+"']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/div[7]/div/div/form/div[2]/div[12]/div[2]/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.partialLinkText(role1)).click();
		}
		// If any element not found then while filling up form then "add user" form was not closing therefore try catch used here
		catch(Exception e){   
			WaitUtil.waitForElementXPATH("//div[@class='modal-header']//button[@class='close']",driver);
			Actions actions=new Actions(driver);
			Thread.sleep(1000);
			actions.moveToElement(driver.findElement(By.xpath("//div[@class='modal-header']//button[@class='close']"))).click().build().perform();
		}
		Thread.sleep(1000);
		if(driver.findElement(By.id("submitUser")).isEnabled()){
			driver.findElement(By.id("submitUser")).click();
			Thread.sleep(5000);
			String expectedMsg = "User successfully added.";
			WaitUtil.getElement(driver, By.xpath("//span[@class='ng-scope ng-binding']"), 10);
			Thread.sleep(5000);
			String successMsg = driver.findElement(By.xpath("//span[@class='ng-scope ng-binding']")).getText();
			if(msg.equals(expectedMsg)){
				if(!successMsg.equals(expectedMsg)){
					Actions actions=new Actions(driver);
					Thread.sleep(1000);
					actions.moveToElement(driver.findElement(By.xpath("//div[@class='modal-header']//button[@class='close']"))).click().build().perform();
					assertTrue("User not added in the database May be because of duplicate user id or email id",successMsg.equals(expectedMsg));
				}
			}else{
				Thread.sleep(5000);
				String duplicateUserErrorMsg = WaitUtil.getElement(driver, By.xpath("//div[contains(text(),'USER ID already exist')]"), 10).getText();
				Actions actions=new Actions(driver);
				Thread.sleep(1000);
				actions.moveToElement(driver.findElement(By.xpath("//div[@class='modal-header']//button[@class='close']"))).click().build().perform();
				assertTrue("Duplicate Login Name - Error message not matching",msg.equals(duplicateUserErrorMsg));
			}
		}else{
			try{
			Thread.sleep(5000);
			List<WebElement> errorMsg = null;
			if(msg.equals("This is not valid Email")){
				WaitUtil.waitForElementXPATH("//div[@style='color: red']",driver);
				Thread.sleep(2000);
					errorMsg = driver.findElements(By.xpath("//div[@style='color: red']"));
				
				System.out.println(" This is not valid Email= "+errorMsg);
			}else if(msg.contains("password") || msg.contains("Password")){
				Thread.sleep(3000);
					errorMsg = driver.findElements(By.xpath("//span[@class='errorMessage ng-binding']"));
			}else{
				Thread.sleep(2000);
					errorMsg = driver.findElements(By.xpath("//span//p"));
			}
				Thread.sleep(5000);
				for(WebElement errorMessage : errorMsg){
					if(errorMsg.size()>1){
						Thread.sleep(1000);
						String expectedMsg = errorMsg.get(0).getText()+"/"+errorMsg.get(1).getText();
						System.out.println("expectedMsg==="+expectedMsg);
						Assert.assertTrue("Error message does not matched",msg.equals(expectedMsg));
					}else{
						System.out.println("actual = "+errorMessage.getText());
						System.out.println("expect = "+msg);
						Assert.assertTrue("Error message does not matched",errorMessage.getText().equals(msg));
					}
				}
			}finally{
				System.out.println("In finaly block "+"count="+count+"   "+userId+"  "+msg);
				Thread.sleep(2000);
				driver.findElement(By.xpath("//div[@class='modal-header']//button[@class='close']")).click();
			}
		}
	}
	
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
	
	@DataProvider
	public Object[][] getUserData() throws IOException{
		ReadExcelFile readExcelFile = new ReadExcelFile();
		
		  String filePath ="/resource/testData";
		  String fileName= "ManageUser.xlsx";
		  String sheetName ="AddUser";
		
		Object userData[][]= readExcelFile.readExcel(filePath, fileName, sheetName);
		return userData;
	}
}
