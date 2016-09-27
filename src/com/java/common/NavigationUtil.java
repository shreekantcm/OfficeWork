package com.java.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class NavigationUtil {

	public static void navigationOnSubMenu(String menu, String subMenu,WebDriver driver){
		String driverName = ReadPropertiesFile.getPropValues("drivername");
		
		Actions actions = new Actions(driver);
		
		WaitUtil.waitForElementToBeClickable(driver,By.partialLinkText(menu),30);
		WebElement topMenu = driver.findElement(By.partialLinkText(menu));
		
		if(driverName.equalsIgnoreCase("chrome")){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			actions.moveToElement(topMenu).click().build().perform();
		}
		else{
			topMenu.click();
		}
		
		WaitUtil.waitForElementToBeClickable(driver,By.partialLinkText(subMenu),30);
		WebElement subMenuOption = driver.findElement(By.partialLinkText(subMenu));
		
		if(driverName.equalsIgnoreCase("chrome")){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			actions.moveToElement(subMenuOption).click().build().perform();
		}
		else{
			subMenuOption.click();
		}
		Log.info("Navigation succesfull");
	}
}
