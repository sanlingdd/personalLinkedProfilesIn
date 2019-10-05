package com.linkedin.automation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jadira.usertype.spi.utils.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewCompanyEmployee {

	public static void HandleAPage(PageOperation obj, WebDriver driver, HuntingCompany firm) {
		int skip = 0;// skip email check
		double current = 0.0;
		obj.scrollThePageWithPercent(driver, Double.valueOf(0.75));
		while (true) {
			// scroll to get all the candidate in the page
			// obj.scrollThePageWithPercent(driver, Double.valueOf(iter / elements.size()));

			List<WebElement> elements = driver.findElements(By.xpath(".//button[text()='加为好友']"));
			if (elements.size() <= skip) {
				break;
			}
			try {
				WebElement element = elements.get(0 + skip);
				obj.scrollThePage(driver, element);
				element.sendKeys(Keys.ENTER);
				;
				obj.sleep(5000);

				List<WebElement> emails = driver.findElements(By.xpath(".//input[@id='email']"));
				if (!emails.isEmpty()) {

					List<WebElement> sendbuttons = driver.findElements(By.xpath(".//button[@name='cancel']"));
					if (!sendbuttons.isEmpty()) {
						sendbuttons.get(0).sendKeys(Keys.ENTER);
					}
					skip++;
					continue;
				}

				List<WebElement> sendbuttons = driver.findElements(By.xpath(".//span[text()='添加消息']/.."));
				if (!sendbuttons.isEmpty()) {
					sendbuttons.get(0).sendKeys(Keys.ENTER);

					String hintMessage = "";
					if (!firm.isCustomer()) {
						hintMessage = "Dear" + "" + "\r\n" + "谢谢接受我的邀请 \r\n"
								+ "我是R2R猎头顾问William，专注猎头行业，服务各行业内外资Top的一些猎头公司的职位招聘。\r\n"
								+ "希望和您建立联系，持续与您迅速分享并探讨或许会对您有所提升的职业机会和最新的市场信息。暂不看机会，可先认识并保持联络。\r\n"
								+ "为方便联系，烦请互换手机号码or微信？（18601793121,微信R2RWilliam,欢迎添加）\r\n" + "谢谢您的时间，等待回复。\r\n"
								+ "William\r\n";
					} else {
						hintMessage = "我是William，希望可以与您建立联系！";
					}

					WebElement messageElement = driver.findElements(By.xpath(".//textarea[@id='custom-message']"))
							.get(0);
					messageElement.sendKeys(hintMessage);
					obj.sleep(3000);

					List<WebElement> sendinvitationElements = driver.findElements(By.xpath(".//span[text()='发邀请']/.."));
					if (!sendinvitationElements.isEmpty()) {
						sendinvitationElements.get(0).sendKeys(Keys.ENTER);
						obj.sleep(3000);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

				obj.sleep(1000 * 60 * 20 * 1);

				String currentURL = driver.getCurrentUrl();
				WebGet(driver, currentURL);

				HandleAPage(obj, driver, firm);
			}
			obj.sleep(10000);
		}
	}

	public static void WebGet(WebDriver driver, String url) {
		try {
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.navigate().to(url);
		} catch (TimeoutException e) {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.stop()");
		}
	}

	public static WebDriver getNewDriver()
			throws InterruptedException, JsonParseException, JsonMappingException, IOException {
		WebDriver driver;
		PageOperation obj = new PageOperation();
		ObjectMapper mapper = new ObjectMapper();
		JavaType firmType = mapper.getTypeFactory().constructParametricType(List.class, HuntingCompany.class);
		// chrome
		System.setProperty("webdriver.chrome.driver", "/temp/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();

		driver.manage().timeouts().pageLoadTimeout(200, TimeUnit.SECONDS);

		WebGet(driver, "https://www.linkedin.com");

		driver.manage().deleteAllCookies();
		Thread.sleep(3000);

		File cookieFile = new File("C://temp/cookie.txt");
		JavaType linkedinCookieType = mapper.getTypeFactory().constructParametricType(List.class, LinkedInCookie.class);
		List<LinkedInCookie> cookieSet = (List<LinkedInCookie>) mapper.readValue(cookieFile, linkedinCookieType);
		obj.sleep(1000);
		for (LinkedInCookie cook : cookieSet) {
			Cookie coo = new Cookie(cook.getName(), cook.getValue(), cook.getDomain(), cook.getPath(), cook.getExpiry(),
					cook.isSecure(), cook.isHttpOnly());
			driver.manage().addCookie(coo);
		}

		return driver;
	}

	public static void main(String[] args)
			throws InterruptedException, JsonParseException, JsonMappingException, IOException {

		File huntingFirmFile = new File("C://temp/huntingfirmsPureCode.txt");
		ObjectMapper mapper = new ObjectMapper();
		JavaType firmType = mapper.getTypeFactory().constructParametricType(List.class, HuntingCompany.class);
		// new TypeReference<List<Cookie>>() {}
		List<HuntingCompany> firmsSet = (List<HuntingCompany>) mapper.readValue(huntingFirmFile, firmType);
		// for (HuntingCompany firm : firmsSet) {
		// firm.setHasFinished(false);
		// }

		PageOperation obj = new PageOperation();
		for (HuntingCompany firm : firmsSet) {
			// if (StringUtils.isEmpty(firm.getName())) {
			firm.setHasFinished(false);
			// }
		}

		for (HuntingCompany firm : firmsSet) {
			try {
				if (firm.isHasFinished()) {
					continue;
				}
				WebDriver driver = getNewDriver();
				String company = "";
				if (firm.isLink()) {
					company = firm.getUrl();
				} else {
					StringBuilder argsStr = new StringBuilder("");
					argsStr.append("\"").append(firm.getCode()).append("\"");
					// BeiJing
					// company =
					// "http://www.linkedin.com/search/results/people/?facetCurrentCompany=%5B" +
					// argsStr.toString()
					// +
					// "%5D&facetGeoRegion=%5B\"cn%3A8911\"%2C\"cn%3A8905\"%5D&facetNetwork=%5B\"S\"%5D&origin=FACETED_SEARCH&page=1";
					// ShangHai
					company = "http://www.linkedin.com/search/results/people/?facetCurrentCompany=%5B"
							+ argsStr.toString()
							+ "%5D&facetGeoRegion=%5B\"cn%3A8909\"%2C\"cn%3A8883\"%5D&facetNetwork=%5B\"S\"%5D&origin=FACETED_SEARCH&page=1";

				}

				obj.sleep(1000);
				WebGet(driver, company);
				obj.sleep(3000);// get company name
				if (StringUtils.isEmpty(firm.getName())) {
					List<WebElement> firmNameElments = driver.findElements(By.xpath(
							".//button[@class='search-s-facet__button artdeco-button artdeco-button--icon-right artdeco-button--2 artdeco-button--primary ember-view']//span[@class='artdeco-button__text']"));
					if (firmNameElments.size() != 0) {
						WebElement firmNameElement = firmNameElments.get(0);
						String firmName = firmNameElement.getText();
						if (firm.getName() == null && (!StringUtils.isEmpty(firmName))) {
							firm.setName(firmName);
						}
					}
				}

				while (true) {
					try {

						HandleAPage(obj, driver, firm);
						// elements.forEach((element) -> {
						// element.sendKeys(Keys.ENTER);
						// obj.sleep(100);
						// });
						List<WebElement> nextPageElements = driver.findElements(By.xpath(
								".//button[@class='artdeco-pagination__button artdeco-pagination__button--next artdeco-button artdeco-button--muted artdeco-button--icon-right artdeco-button--1 artdeco-button--tertiary ember-view']"));
						if (nextPageElements.isEmpty()) {
							firm.setHasFinished(true);
							mapper.writeValue(huntingFirmFile, firmsSet);
							break;
						} else {
							nextPageElements.get(0).sendKeys(Keys.ENTER);
							obj.sleep(10000);
						}
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
				driver.close();
				driver.quit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Batch Finished!");

		// finished
		// driver.close();

	}

}
