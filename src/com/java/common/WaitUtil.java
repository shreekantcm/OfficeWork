package com.java.common;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
/**
 * 
 * @author Nandu
 *
 */
public class WaitUtil {
	//*************** Function with WebDriver as argument
	public static void waitForElementLocated(By locator, WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver,30);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public static void waitForElementXPATH(String locator, WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver,30);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
	}

	public static void waitForTextToBePresentInElementXPATH(String locator,String text, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver,30);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(locator), text));
	}
	
	public static void waitForElementID(String locator,WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver,30);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator)));
	}
	
	public static void waitForElementClicableID(String locator,WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver,30);
	    wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
	}
	
	public static void waitForElementinvisibilityOfElementWithText(By locator,String text,WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver,30);
	    wait.until(ExpectedConditions.invisibilityOfElementWithText(locator,text));
	}
	
	public static void waitForElementToBeClickable(WebDriver driver,By locator,int timeoutSeconds) {
	    WebDriverWait wait = new WebDriverWait(driver,timeoutSeconds);
	    wait.until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	public static WebElement waitForElementPLT(String linkText,WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver,30);
	    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(linkText)));
	    return element;
	}
	
	public static WebElement getElement(final WebDriver driver, final By locator, final int timeoutSeconds) {
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(timeoutSeconds, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class);
	    return wait.until(new Function<WebDriver, WebElement>() {
	        public WebElement apply(WebDriver webDriver) {
	            return driver.findElement(locator);
	        }
	    });
	}
}
