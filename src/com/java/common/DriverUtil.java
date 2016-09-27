package com.java.common;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
/**
 * Return new firefox driver
 * @author Nandu
 *
 */
public class DriverUtil {
	static String applicationUrl = ReadPropertiesFile.getPropValues("applicationUrl");
	static String chromeDriverPath = System.getProperty("user.dir")+"/resource/allDrivers/chromedriver.exe";
	static String ieDriverPath = System.getProperty("user.dir")+"/resource/allDrivers/IEDriverServer.exe";
	
	public static WebDriver getDriver(WebDriver driver,String driverName) {
		DOMConfigurator.configure("log4j.xml");
		if(driverName.equalsIgnoreCase("firefox")){
			driver =  new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		}
		if(driverName.equals("ie32")){
			System.setProperty("webdriver.ie.driver", ieDriverPath);  
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();  
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(ieCapabilities);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
		if(driverName.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			driver =  new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		}
		driver.manage().window().maximize();
		driver.get(applicationUrl);
		return driver;
	}		
}	
