package com.java.common;

import static org.testng.AssertJUnit.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class AddUserUtil {
	static String button_Go = ReadLocatorsFromExcel.readExcel("button_Go", "ManageUser");
	static String widget_add = ReadLocatorsFromExcel.readExcel("widget_add","ManageUser");
	public static void addUser(String userId,String firstName,String lastName,String emailID,String password,
			String confPassword,String mobileNo,String altrEmaiID,String status,String privilege,String 
			role,String msg,WebDriver driver) throws InterruptedException{
		String role1 = role;
		Actions action = new Actions(driver);
		//NavigationUtil.navigationOnSubMenu("User", "Manage",driver);
		Thread.sleep(2000);
		action.moveToElement(driver.findElement(By.id(button_Go))).click().build().perform();
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
		// Password
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
		
		/*if(driver.findElement(By.id("submitUser")).isEnabled()){*/
			Thread.sleep(1000);
			WaitUtil.getElement(driver, By.id("submitUser"), 20).click();
			String expectedMsg = "User successfully added.";
			Thread.sleep(5000);
			String successMsg = WaitUtil.getElement(driver, By.xpath("//span[contains(text(),'User successfully added.')]"), 10).getText();
			if(msg.equals(expectedMsg)){
				if(!successMsg.equals(expectedMsg)){
					Actions actions=new Actions(driver);
					actions.moveToElement(driver.findElement(By.xpath("//div[@class='modal-header']//button[@class='close']"))).click().build().perform();
					assertTrue("User not added in the database May be because of duplicate user id or email id",successMsg.equals(expectedMsg));
				}
			}else{
				Thread.sleep(5000);
				WaitUtil.waitForElementXPATH("//div[contains(.,'Duplicate Login Name') and @ng-show='duplicateLogin.errorMsg']",driver);
				String duplicateUserErrorMsg = driver.findElement(By.xpath("//div[contains(.,'Duplicate Login Name') and @ng-show='duplicateLogin.errorMsg']")).getText();
				Actions actions=new Actions(driver);
				actions.moveToElement(driver.findElement(By.xpath("//div[@class='modal-header']//button[@class='close']"))).click().build().perform();
				assertTrue("Duplicate Login Name - Error message not matching",msg.equals(duplicateUserErrorMsg));
			}
			/*}else{
			Thread.sleep(5000);
			List<WebElement> errorMsg = null;
			if(msg.equals("This is not valid Email")){
				WaitUtil.waitForElementXPATH("//div[@style='color: red']",driver);
				errorMsg = driver.findElements(By.xpath("//div[@style='color: red']"));
				System.out.println(" This is not valid Email= "+errorMsg);
			}else{
				Thread.sleep(2000);
				WaitUtil.waitForElementXPATH("//span//p",driver);
				errorMsg = driver.findElements(By.xpath("//span//p"));
			}
			
			try{
				Thread.sleep(5000);
				for(WebElement errorMessage : errorMsg){
					if(errorMsg.size()>1){
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
				Thread.sleep(5000);
				driver.findElement(By.xpath("//div[@class='modal-header']//button[@class='close']")).click();
			}
		}*/
	}
}
