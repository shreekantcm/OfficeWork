package com.java.common;

import java.io.File;   
import java.io.FileInputStream;   
import java.io.IOException;   
import org.apache.poi.hssf.usermodel.HSSFWorkbook;   
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;   
import org.apache.poi.ss.usermodel.Sheet;   
import org.apache.poi.ss.usermodel.Workbook;   
import org.apache.poi.xssf.usermodel.XSSFWorkbook;   
import org.testng.annotations.Test;
/**
 * 
 * @author Nandu
 *
 */
public class ReadExcelFile {    
	
	
	public Object[][] readExcel(String filePath,String fileName,String sheetName) throws IOException{  
		Log.info("$$ START FUNCTION : readExcel(filePath="+filePath+",fileName="+fileName+"," +
				"sheetName=+sheetName+)");
		Object userData[][]= null;
		
		String projectPath = System.getProperty("user.dir");
		
		String usableProjectPath = projectPath.replaceAll("\\\\", "/");
		File file =    new File(usableProjectPath+filePath+"/"+fileName);     
		
		FileInputStream inputStream = new FileInputStream(file);       
		Workbook workbook = null;       
		String fileExtensionName = fileName.substring(fileName.indexOf("."));       
		if(fileExtensionName.equals(".xlsx")){       
			workbook = new XSSFWorkbook(inputStream);       
		}       
		else if(fileExtensionName.equals(".xls")){           
			workbook = new HSSFWorkbook(inputStream);       
		}       
		Sheet sheet = workbook.getSheet(sheetName);       
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();   
		
		System.out.println("Total No of row = "+rowCount);
		
		if(sheetName.equalsIgnoreCase("AddUser")){
			userData = new Object[rowCount][12];
		}
		
		if(sheetName.equalsIgnoreCase("AddPrivilege")){
			userData = new Object[rowCount][3];
		}
		
		if(sheetName.equalsIgnoreCase("assignModules")){
			userData = new Object[rowCount][4];
		}
		
		if(sheetName.equalsIgnoreCase("AddSkill")){
			userData = new Object[rowCount][3];
		}
		if(sheetName.equalsIgnoreCase("EditSkill")){
			userData = new Object[rowCount][3];
		}
		if(sheetName.equalsIgnoreCase("AddSubSkill")){
			userData = new Object[rowCount][3];
		}
		if(sheetName.equalsIgnoreCase("logincredential")){
			userData = new Object[rowCount][3];
		}
		if(sheetName.equalsIgnoreCase("AddQuestion")){
			userData = new Object[rowCount][2];
		}
		if(sheetName.equalsIgnoreCase("AddTopic")){
			userData = new Object[rowCount][4];
		}
		if(sheetName.equalsIgnoreCase("EditTopic")){
			userData = new Object[rowCount][4];
		}
		if(sheetName.equalsIgnoreCase("SuggestedLearning")){
			userData = new Object[rowCount][5];
		}
		// Putting value i=0 will not read first row
		//Here rowCount less "<" because we are startign from the 0th row also we don't want use last row so "rowCount-1"
		for (int i = 0; i <rowCount; i++) {           
			Row row = sheet.getRow(i);  
			for (int j = 0; j < row.getLastCellNum()-1; j++) {   
				final DataFormatter df = new DataFormatter();
				System.out.print(df.formatCellValue(row.getCell(j))+"||");	
				userData[i][j]=df.formatCellValue(row.getCell(j));
			}           
		}
		Log.info("$$ END FUNCTION : readExcel()");
		return userData;      
	}               
}
