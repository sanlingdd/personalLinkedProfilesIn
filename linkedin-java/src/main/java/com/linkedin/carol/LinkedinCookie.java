package com.linkedin.carol;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.automation.CommonSetting;
import com.linkedin.automation.LinkedinOperation;
import com.linkedin.automation.PageOperation;

public class LinkedinCookie {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		PageOperation obj = new PageOperation();
		WebDriver driver;
		// chrome
		System.setProperty("webdriver.chrome.driver", CommonSetting.chromeDrivePath);
		//System.setProperty("webdriver.chrome.driver", "D:\\360Chrome\\chromedriver.exe");		
		//driver = new ChromeDriver();

		
		ChromeOptions options = new ChromeOptions();
		
		//options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");//.binary_location = "/usr/bin/chromium-browser"
		//options.setBinary("C:\\Users\\Michael\\AppData\\Local\\360Chrome\\Chrome\\Application\\360chrome.exe");
		
		//options.addArguments("start-maximized"); // open Browser in maximized mode
//		options.addArguments("disable-infobars"); // disabling infobars
//		options.addArguments("--disable-extensions"); // disabling extensions
//		options.addArguments("--disable-gpu"); // applicable to windows os only
//		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
//		options.addArguments("--no-sandbox"); // Bypass OS security model
		//options.addArguments("--enable-logging"); // Bypass OS security model
		
		driver = new ChromeDriver(options);
		

		LinkedinOperation lop = new LinkedinOperation();
		driver.manage().window().maximize();

		//Daisy Dong
		//lop.login(driver,"1574580422@qq.com","woaini123123");

		//Yaqi
		//lop.login(driver,"yaqichenhappy@163.com","AaBCD525");
		
		//Carol Xu
		//lop.login(driver,"478267107@qq.com","Xu234567");
		
		//Lily Rao
		//lop.login(driver,"15000729310","rt135790");
		
		//Kayla
		//lop.login(driver,"18321408306","dwf45750");
		
		//William Huang
		//lop.login(driver,"williamhuangsuper@163.com","Initial0");
		
		//Ada Zhi
		//lop.login(driver,"zljwenlue@126.com","zhilijuan148628");
		
		//Yulia===Email Yulia170814
		lop.login(driver,"13774278832@163.com","Dd202134");
		
		//https://www.linkedin.com/search/results/people/?facetGeoRegion=%5B%22cn%3A8911%22%5D&facetIndustry=%5B%226%22%5D&keywords=HRBP&origin=FACETED_SEARCH
		
		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
		//File cookieFile = new File("D:/git/linkedin/projectone/linkedin-java/YaqiCookie.txt");
		//File cookieFile = new File(CommonSetting.cookieFilePrefix+"KaylaCookie.txt");
		//File cookieFile = new File(CommonSetting.cookieFilePrefix+"WilliamCookie.txt");
		File cookieFile = new File(CommonSetting.cookieFilePrefix+"YuliaCookie.txt");
		//File cookieFile = new File(CommonSetting.cookieFilePrefix+"AdaCookie.txt");
		ObjectMapper mapper = new ObjectMapper(); 
		mapper.writeValue(cookieFile, cookies);
	}

}
