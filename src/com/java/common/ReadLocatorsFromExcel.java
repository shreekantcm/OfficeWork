package com.java.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class will read the excel sheet and return locator value of key which is pass as argument 
 * in 'readExcel' function 
 * @author Nandu
 *
 */
public class ReadLocatorsFromExcel {
	// This function will search and return the value of key which we are passing as an arguments
	// Search will take place in the sheet which we are passing as an argument 
	public static String readExcel(String key,String sheetName){  
		File file = new File(System.getProperty("user.dir")+"/resource/locators/Locators.xlsx");       
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}       
		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}       
		     
		Sheet sheet = workbook.getSheet(sheetName);       
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();   
		//System.out.println("Total No of row = "+rowCount);
		String value = null;
		
		for (int i = 1; i <=rowCount; i++) {           
			Row row = sheet.getRow(i);  
			final DataFormatter df = new DataFormatter();
			//System.out.println("cell value is null = "+df.formatCellValue(row.getCell(0)));
			if(key.equals(df.formatCellValue(row.getCell(0)))){
				value = df.formatCellValue(row.getCell(1));
			}
		}
		if(value == null){
			System.out.println("ERROR - Class - ReadLocatorsFromExcel - locator not found in excel");
		}
		return value;      
	}  
}
