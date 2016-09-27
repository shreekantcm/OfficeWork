package report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
/**
 *
 * @author NanduShende
 *
 */
public class ExcelReportGenerator {

	public void generateExcelReport(String destinationFileName) throws ParserConfigurationException, SAXException, IOException{
		
		// Return current directory
		String path = ExcelReportGenerator.class.getClassLoader().getResource("./").getPath();
		path = path.replaceAll("bin", "src");
		System.out.println(path);
		
		/*File xmlFile = new File(System.getProperty("user.dir")+"/test-output/testng-results.xml");*/
		File xmlFile = new File(System.getProperty("user.dir")+"/Test-Report/TestNGreport/testng-results.xml");
		System.out.println(xmlFile.isFile());
		System.out.println(xmlFile.getPath());
		
		// Use to parse the xml file
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder build = fact.newDocumentBuilder();
		Document doc = build.parse(xmlFile);
		doc.getDocumentElement().normalize();
		
		//
		XSSFWorkbook book = new XSSFWorkbook();
		
		
		XSSFCellStyle fail = book.createCellStyle();
		XSSFCellStyle pass = book.createCellStyle();
		XSSFCellStyle skip = book.createCellStyle();
		
		// Read the xml file by its tag name and return the list called nodelist
		// doc.getElementsByTagName("test");
		NodeList test_list = doc.getElementsByTagName("test");
		//test name
		for(int i=0; i< test_list.getLength();i++){
			int r = 0;  // row
			Node test_node = test_list.item(i);
			String test_name = ((Element)test_node).getAttribute("name");
			XSSFSheet sheet = book.createSheet(test_name);
			NodeList class_list = ((Element)test_node).getElementsByTagName("class");// In same test tag we are fatching the attribute class
			//class name
			for(int j=0; j< class_list.getLength();j++){
				Node class_node = class_list.item(j);
				String class_name = ((Element)class_node).getAttribute("name");
				NodeList test_method_list = ((Element)class_node).getElementsByTagName("test-method");
				//method name
				for(int k=0;k<test_method_list.getLength();k++){
					Node test_method_node = test_method_list.item(k);
					String test_method_name = ((Element)test_method_node).getAttribute("name");
					String test_method_status = ((Element)test_method_node).getAttribute("status");
					
					fail.setFillForegroundColor(HSSFColor.RED.index);
					pass.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
					skip.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
					
					fail.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
					pass.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
					skip.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
					
					XSSFRow row = sheet.createRow(r++);
					XSSFCell cel_name = row.createCell(0);
					cel_name.setCellValue(class_name + "." +test_method_name); 
					
					XSSFCell cel_status = row.createCell(1);
					//cel_status.setCellValue(class_name + "." +test_method_status);
					cel_status.setCellValue(test_method_status);
					
					if("fail".equalsIgnoreCase(test_method_status))
						cel_status.setCellStyle(fail);
					if("pass".equalsIgnoreCase(test_method_status))
						cel_status.setCellStyle(pass);
					if("skip".equalsIgnoreCase(test_method_status))
						cel_status.setCellStyle(skip);
					
					// Code - to display the exception
					XSSFCell cel_exp;
					String exp_msg;
					
					if("fail".equalsIgnoreCase(test_method_status)){
						NodeList exp_list = ((Element)test_method_node).getElementsByTagName("exception");
						Node exp_node = exp_list.item(0);
						exp_msg = ((Element)exp_node).getAttribute("class"); 
						cel_exp = row.createCell(2);
						cel_exp.setCellValue(exp_msg);
					}
				}
			}
			
		}
		
		FileOutputStream fout = new FileOutputStream(path + "report/"+ destinationFileName);
		book.write(fout);
		fout.close();
		System.out.println("report generated");
		
	}
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		new ExcelReportGenerator().generateExcelReport("report.xlsx");
	}
}
