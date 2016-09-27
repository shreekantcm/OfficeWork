package com.java.modules.usermanagement;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.java.common.DriverUtil;
import com.java.common.LoginUtil;
import com.java.common.NavigationUtil;
import com.java.common.ReadLocatorsFromExcel;
import com.java.common.ReadPropertiesFile;
import com.java.common.WaitUtil;

public class SearchOption {
	
	String button_previlegeDropDown =  ReadLocatorsFromExcel.readExcel("button_previlegeDropDown","ManageUser");
	String button_Go = ReadLocatorsFromExcel.readExcel("button_Go", "ManageUser");
	String ddLink_associate = ReadLocatorsFromExcel.readExcel("ddLink_associate","ManageUser");
	
	String text_NoMatchingRecord = ReadLocatorsFromExcel.readExcel("text_NoMatchingRecord","ManageUser");
	
	String input_search =  ReadLocatorsFromExcel.readExcel("input_search","ManageUser");
	
	static int count;
	static int counttd;
	static int trCount;
	
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
	
	// We are fetching here email id of first record which is on 4th <td> then 
	// Then searching with same email id by sending email id in search input field
	// And then compare the result 
	@Test(priority =4)
	public void verifySearch() throws InterruptedException{
		WaitUtil.waitForElementToBeClickable(driver,By.className(button_previlegeDropDown),20);
		Actions actions = new Actions(driver);
		Thread.sleep(1000);
		actions.moveToElement(driver.findElement(By.className(button_previlegeDropDown))).click().build().perform();
		Thread.sleep(1000);
		driver.findElement(By.xpath(ddLink_associate)).click();
		driver.findElement(By.id(button_Go)).click();
		
		WaitUtil.waitForElementinvisibilityOfElementWithText(By.xpath("//table//tr//td"), "No Records Found. Please " +
				"Try Some Other Combination.", driver);
		// allTR have data in all TD on first page
		List<WebElement> allTD = driver.findElements(By.xpath("//table//tr//td"));
		for(WebElement e: allTD){
			counttd++;
			if(counttd==4){
				driver.findElement(By.id(input_search)).sendKeys(e.getText());
				String expectedText = driver.findElement(By.xpath("//table//tr[1]//td["+counttd+"]")).getText();
				Assert.assertEquals(expectedText, e.getText());
			}
		}
	}
	
	//This function will check the searched key matched with the result in every row or not
	// Mean whatever record displayed as result, each record should contain search key
	@Test(priority =5)
	public void verifySearchResultContainSearchKey() throws InterruptedException{
		driver.findElement(By.id(input_search)).clear();
		driver.findElement(By.id(input_search)).sendKeys(" ");
		Thread.sleep(2000);
		/*driver.findElement(By.className(button_previlegeDropDown)).click();
		driver.findElement(By.xpath("//ul[@id='ui-id-1']//li[text()='Associate']")).click();
		driver.findElement(By.id("goBtn")).click();*/

		// allTR have data in all TD on first page
		List<WebElement> allTD = driver.findElements(By.xpath("//table//tr//td"));
		for(WebElement e: allTD){
			count++;
			if(count==4){
				//e.getText() = tr[1]//td[4]
				driver.findElement(By.id(input_search)).sendKeys(e.getText());
				Thread.sleep(1000);
				String expectedText = driver.findElement(By.xpath("//table//tr[1]//td["+count+"]")).getText();
				Assert.assertEquals(expectedText, e.getText());
				
				List<WebElement> allTR = driver.findElements(By.xpath("//table//tr"));
				System.out.println("size of row = "+allTR.size());
				if(allTR.size()>1){
					// Loop of all resulted tr
					for(WebElement alltr : allTR){
						trCount++;
						if(trCount == allTR.size()){break;}
						//System.out.println("trCount"+trCount);
						//System.out.println("alltr"+alltr);
						
						// All the <td> of row(alltr)
						List<WebElement> td = driver.findElements(By.xpath("//table//tr["+trCount+"]//td"));
						boolean flag = false;
						for(WebElement td1 :td){
							System.out.println("td1 = "+td1.getText());
							if(td1.getText().equals(e.getText())){
								flag = true;
							}
						} 
						if(flag == false){ 	// If the data does not match with any <td> of tr(alltr)
							Assert.assertTrue("No of row populated in result are not matching with the searchKey", 1>2);
						}
					}
				}
			}
			if(count == 4){
			break; //  to stop the repeatation of for loop after the count ==4
			}
		}
	}
	
	// Verify if particular text(Bench admin) present only in privilege(Bench admin) 
	// then it should not displayed in another privilege (Associate)
	@Test(priority =6)
	public void verifyPrivilegeSpecificText() throws InterruptedException{
		driver.findElement(By.id(input_search)).clear();
		driver.findElement(By.id(input_search)).sendKeys("Bench admin");
		Thread.sleep(2000);
		String expectedMsg = "No matching records found";
		String actualMsg = driver.findElement(By.xpath(text_NoMatchingRecord)).getText();
		Assert.assertEquals(expectedMsg, actualMsg);
	}
	@AfterTest
	public void quitDriver(){
		driver.quit();
	}
}
