package com.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.job.SendEmail;
import com.model.Customer;

@Component
public class AutoRegister {

	public AutoRegister() {
		super();
	}

	@Autowired
	SendEmail emailSender;

	Customer thisCustomer;

	static long startTime;
	static int hour, min;
	static List<String> appliedHouses;
	StringBuilder logFileName;

	public void startJob(Customer customer) {
		thisCustomer = customer;
		try {
			appliedHouses = new ArrayList<String>();
			final Logger logger = Logger.getLogger("MyLog");
			FileHandler fh;
			try {
				logFileName = new StringBuilder("BopLogger-" + thisCustomer.getName() + "-"
						+ Calendar.getInstance().get(Calendar.YEAR) + (Calendar.getInstance().get(Calendar.MONTH) + 1)
						+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-"
						+ Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + Calendar.getInstance().get(Calendar.MINUTE)
						+ ".txt");
				fh = new FileHandler("./" + logFileName.toString());
				logger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Timer timer = new Timer();
			TimerTask tt = new TimerTask() {
				public void run() {
					Calendar cal = Calendar.getInstance();
					hour = cal.get(Calendar.HOUR_OF_DAY);// get the hour number
															// of the day, from
															// 0 to 23
					min = cal.get(Calendar.MINUTE);
					if ((hour == 23 && min > 50) || (hour >= 0 && hour < 2) || ((min > 0))) {
						initGtb(logger);
					} else {
						System.out.println("Out of Apply hours :: " + hour + ":" + min);
					}
				}
			};
			timer.schedule(tt, 1000, 1000 * 30);// delay the task 1 second, and
												// then run task every 30
												// seconds
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void initGtb(Logger logger) {
		startTime = System.currentTimeMillis();
		System.setProperty("webdriver.chrome.driver", "./src/main/webapp/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		executeGtb(driver, logger);
		driver.quit();
	}

	private void executeGtb(WebDriver driver, Logger logger) {
		if (login(driver, logger)) {
			if (filterSearchGtb(driver, logger)) {
				int housesApplied = apply(driver, logger);
				emailSender.sendEmails(housesApplied, thisCustomer.getEmail(), logFileName.toString());

			} else {
				login(driver, logger);
				filterSearchGtb(driver, logger);
			}

			try {
				WebElement logout = driver.findElement(By.xpath("//*[@id='pagehead']/div/div[1]/p[2]/a"));
				logout.click();
			} catch (ElementNotVisibleException env) {
				// logger.info("ERROR :: logout button NOT VISIBLE");
			} catch (NoSuchElementException e) {
				// logger.info("ERROR :: logout button element");
			}
		}

	}

	private int apply(WebDriver driver, Logger logger) {
		int result = 0;
		try {
			List<WebElement> itemList = new ArrayList<WebElement>();
			List<String> links = new ArrayList<String>();
			for (int i = 1; i < 30; i++) {
				try {
					WebElement item = driver
							.findElement(By.xpath("//*[@id='search-result-items']/tr[" + i + "]/td/a/div[2]/div[6]"));
					if (null != item.getText() && item.getText().trim().equals("terms")) {
						WebElement link = driver
								.findElement(By.xpath("//*[@id='search-result-items']/tr[" + i + "]/td/a"));
						if (null != link) {
							itemList.add(link);
							links.add(link.getAttribute("href"));
						}
					}
				} catch (ElementNotVisibleException env) {
					logger.info("ERROR :: search-result-items NOT VISIBLE");
				} catch (NoSuchElementException e) {
					logger.info("ERROR :: search-result-items no such element");
				}
			}
			if (itemList.size() > 0) {
				logger.info("New Houses to apply :: ");
			}
			for (int i = 0; i < itemList.size(); i++) {
				logger.info((i + 1) + " -----> " + itemList.get(i).getText());
			}
			int count = 0;
			for (String link : links) {
				driver.get(link);
				WebElement registerButton = driver.findElement(By.name("apply"));
				registerButton.click();
				count++;
			}
			if (count == links.size()) {
				result = count;
			}
			logger.info("Total applied houses  ************* " + count);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("EXCEPTION :: Exception in apply");
		}
		return result;
	}

	private static boolean filterSearchGtb(WebDriver driver, Logger logger) {
		boolean result = false;
		try {
			if (driver.getPageSource().contains("THAMEEM")) {
				try {
					WebElement citySel = driver.findElement(By.xpath("//*[@id='city']"));
					Select city = new Select(citySel);
					city.selectByVisibleText("Göteborg");
				} catch (ElementNotVisibleException env) {
					logger.info("ERROR :: city NOT VISIBLE");
				}
				WebElement termMatch = driver.findElement(By.id("filterrequirements"));
				termMatch.sendKeys(Keys.SPACE);
				WebElement rent = driver.findElement(By.name("rent"));
				rent.sendKeys(Keys.chord(Keys.CONTROL, "a"), "7500");
				WebElement searchButton = driver
						.findElement(By.xpath("//*[@id='objectsearchform']/div/table[2]/tbody/tr[3]/td[2]/button"));
				searchButton.click();
				try {
					WebElement showMoreButton = driver.findElement(By.id("show-more-button"));
					showMoreButton.click();
				} catch (ElementNotVisibleException env) {
					// logger.info("show-more-button not visible. Can be
					// ignored");
				}
				result = true;
			} else {
				driver.navigate().refresh();
				// logger.info("ERROR :: User not logged In");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info("EXCEPTION :: Exception in filterSearch");
		}
		return result;

	}

	private static boolean login(WebDriver driver, Logger logger) {
		boolean result = false;
		try {
			driver.get("https://nya.boplats.se/login/");
			driver.manage().window().maximize();
			driver.switchTo().frame(driver.findElement(By.id("login-frame")));
			WebElement userName = driver.findElement(By.name("username"));
			WebElement passWord = driver.findElement(By.name("password"));
			WebElement loginButton = driver.findElement(By.name("login_button"));
			userName.sendKeys("199106059299");
			passWord.sendKeys("th@meem98567");
			loginButton.click();
			result = true;
		} catch (WebDriverException wex) {
			driver.quit();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
