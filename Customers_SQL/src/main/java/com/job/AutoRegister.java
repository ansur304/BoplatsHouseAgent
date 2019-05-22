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
		// TODO Auto-generated constructor stub
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
			final Logger loggerBlock = Logger.getLogger("MyLogBlock");
			FileHandler fh1;
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

			try {

//                try {
//                    fh1 = new FileHandler("C://Dev_stuff//Workspace//HouseAgent//Logs//BlockLogger.txt");
//                    loggerBlock.addHandler(fh1);
//                    SimpleFormatter formatter1 = new SimpleFormatter();
//                    fh1.setFormatter(formatter1);
//                } catch (SecurityException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Timer timer = new Timer();
			TimerTask tt = new TimerTask() {
				public void run() {
					Calendar cal = Calendar.getInstance();
					hour = cal.get(Calendar.HOUR_OF_DAY);// get the hour number of the day, from 0 to 23
					min = cal.get(Calendar.MINUTE);
					// logger.info("Bop Thread is running now :: Time :: " + hour + ":" + min);
					// initGtb(logger);
					// initMld(logger);
					if ((hour == 23 && min > 50) || (hour >= 0 && hour < 2) || ((min > 0))) {
						initGtb(logger);
					} else {
						System.out.println("Out of Apply hours :: " + hour + ":" + min);
					}
				}
			};
			timer.schedule(tt, 1000, 1000 * 30);// delay the task 1 second, and then run task every 30 seconds

			// if ((hour == 23 && min > 50) || (hour >= 0 && hour <= 2)) {
			// timer.schedule(tt, 1000, 1000 * 30);// delay the task 1 second, and then run
			// task every 30 seconds
			// System.out.println("running in 30 sec freq");
			// } else {
			// timer.schedule(tt, 1000, 1000 * 60 * 4);// delay the task 1 second, and then
			// run task every 4 min
			// System.out.println("running in 4 min freq");
			// }

			// Timer timerBlock = new Timer();
			// TimerTask tb = new TimerTask() {
			// public void run() {
			// Calendar cal = Calendar.getInstance();
			// hour = cal.get(Calendar.HOUR_OF_DAY);// get the hour number of the day, from
			// 0 to 23
			// min = cal.get(Calendar.MINUTE);
			// if (hour >= 6 && hour <= 23) {
			// initBlock(loggerBlock);
			// loggerBlock.info("Block Thread is running now :: Time :: " + hour + ":" +
			// min);
			// } else {
			// loggerBlock.info("Out of Apply hours for Block");
			// }
			// }
			// };
			// timerBlock.schedule(tb, 1000, 1000 * 60 * 5);
			//
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void initGtb(Logger logger) {
		startTime = System.currentTimeMillis();
		System.setProperty("webdriver.chrome.driver", "./src/main/webapp/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		// logger.info("Program Started ------------------------------");
		executeGtb(driver, logger);
		driver.quit();
	}

	public void initMld(Logger logger) {
		System.setProperty("webdriver.chrome.driver", "C:\\Dev_stuff\\Workspace\\HouseAgent\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		executeMld(driver, logger);
		driver.quit();
		logger.info("Program Ended --------------------------------");
		// logger.info("Time take in Sec ::" + (System.currentTimeMillis() - startTime)
		// / 1000);
	}

	private void executeMld(WebDriver driver, Logger logger) {
		if (login(driver, logger)) {
			if (filterSearchMdl(driver, logger)) {
				apply(driver, logger);
			} else {
				login(driver, logger);
				filterSearchMdl(driver, logger);
			}
			try {
				WebElement logout = driver.findElement(By.xpath("//*[@id='pagehead']/div/div[1]/p[2]/a"));
				logout.click();
			} catch (ElementNotVisibleException env) {
				// logger.info("ERROR :: executeMld :: logout button NOT VISIBLE");
			} catch (NoSuchElementException e) {
				// logger.info("ERROR :: executeMld :: logout button element");
			}
		}
	}

	private void executeGtb(WebDriver driver, Logger logger) {
		if (login(driver, logger)) {
			if (filterSearchGtb(driver, logger)) {
				int housesApplied = apply(driver, logger);
				if (housesApplied > 0)
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
					System.out.println("ElementNotVisibleException :: Göteborg city NOT VISIBLE");
				}
				WebElement rooms = driver.findElement(By.xpath("//*[@id='rooms']"));
				Select room = new Select(rooms);
				room.selectByVisibleText("2");
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
					// logger.info("show-more-button not visible. Can be ignored");
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

	private static boolean filterSearchMdl(WebDriver driver, Logger logger) {
		boolean result = false;
		try {
			if (driver.getPageSource().contains("THAMEEM")) {
				try {
					driver.get("https://nya.boplats.se/sok");
					Thread.sleep(2000);
					WebElement citySel = driver.findElement(By.xpath("//*[@id='city']"));
					Select city = new Select(citySel);
					city.deselectAll();
					// city.selectByVisibleText("Mölndal");
					city.selectByIndex(30);
					Thread.sleep(2000);
					System.out.println("Selected City :: " + city.getAllSelectedOptions());
				} catch (ElementNotVisibleException env) {
					// logger.info("filterSearchMdl ERROR :: city NOT VISIBLE");
				}
				WebElement termMatch = driver.findElement(By.id("filterrequirements"));
				termMatch.sendKeys(Keys.SPACE);
				WebElement rent = driver.findElement(By.name("rent"));
				rent.sendKeys(Keys.chord(Keys.CONTROL, "a"), "7000");
				WebElement searchButton = driver
						.findElement(By.xpath("//*[@id='objectsearchform']/div/table[2]/tbody/tr[3]/td[2]/button"));
				searchButton.click();
				try {
					WebElement showMoreButton = driver.findElement(By.id("show-more-button"));
					showMoreButton.click();
				} catch (ElementNotVisibleException env) {
					logger.info("filterSearchMdl :: show-more-button not visible. Can be ignored");
				}
				result = true;
			} else {
				driver.navigate().refresh();
				logger.info(" filterSearchMdlERROR :: User not logged In");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("EXCEPTION :: Exception in filterSearchMdl");
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
			// logger.info("Logged in Successfully ");
			result = true;
		} catch (WebDriverException wex) {
			// logger.info("WebDriverException");
			driver.quit();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info("Exception in login");
		}
		return result;
	}

	public static void initBlock(Logger loggerBlock) {
		startTime = System.currentTimeMillis();
		System.setProperty("webdriver.chrome.driver", "C:\\Dev_stuff\\Workspace\\HouseAgent\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		loggerBlock.info("Block Program Started ------------------------------");
		executeBlock(driver, loggerBlock);
		driver.quit();
		loggerBlock.info("Block Program Ended --------------------------------");
		loggerBlock.info("Time take in Sec ::" + (System.currentTimeMillis() - startTime) / 1000);
	}

	private static void executeBlock(WebDriver driver, Logger loggerBlock) {
		if (loginBlock(driver, loggerBlock)) {
			if (filterSearchBlock(driver, loggerBlock)) {
				applyBlock(driver, loggerBlock);
			} else {
				loginBlock(driver, loggerBlock);
			}
		}
	}

	private static boolean applyBlock(WebDriver driver, Logger loggerBlock) {
		boolean result = false;
		try {
			List<String> newHouseIds = new ArrayList<String>();
			List<String> newHouseLinks = new ArrayList<String>();
			try {
				List<WebElement> allItems = driver.findElements(By.xpath("//*[@id='adwatch']/div/article"));
				if (!appliedHouses.isEmpty()) {
					for (WebElement item : allItems) {
						boolean applied = false;
						if (!item.getText().toUpperCase().contains("WEST")) {
							for (String appliedHouse : appliedHouses) {
								if (appliedHouse.equals(item.getAttribute("id"))) {
									applied = true;
									break;
								}
							}
							if (!applied) {
								newHouseIds.add(item.getAttribute("id"));
								loggerBlock.info("Adding ID ::" + item.getAttribute("id")
										+ "\n -- New add found in Block :: " + item.getText());
								appliedHouses.add(item.getAttribute("id"));
							}
						}
					}
				} else {
					for (WebElement item : allItems) {
						appliedHouses.add(item.getAttribute("id"));
						System.out.println(item.getAttribute("id"));
					}
				}
			} catch (ElementNotVisibleException env) {
				loggerBlock.info("ERROR :: House NOT VISIBLE");
			} catch (NoSuchElementException e) {
				loggerBlock.info("ERROR :: House no such element");
			}
			if (!newHouseIds.isEmpty()) {
				try {
					for (String id : newHouseIds) {
						newHouseLinks.add(driver.findElement(By.xpath("//*[@id='" + id + "']/a")).getAttribute("href"));
					}
				} catch (ElementNotVisibleException env) {
					loggerBlock.info("ERROR :: House Link NOT VISIBLE");
				} catch (NoSuchElementException e) {
					loggerBlock.info("ERROR :: House link no such element");
				}
			}

			// // TEST PURPOSE
			// else {
			// newHouseLinks.add(driver.findElement(By.xpath("//*[@id='item_80648475']/a")).getAttribute("href"));
			// newHouseLinks.add(driver.findElement(By.xpath("//*[@id='item_80647809']/a")).getAttribute("href"));
			// }

			int count = 0;
			if (!newHouseLinks.isEmpty()) {
				String msg = "Hallå, \n\n Vi är en familj på 2 som letar efter en lägenhet för ett långsiktigt. \n\n Om oss - Jag, Thameem 26 år gammal, arbetar som programvarutekniker hos Volvo Cars Corporation, Torslanda med en stabil lön och fast jobb. Min fru Ruhina, 25 år gammal, är ingenjör men bor för närvarande hemma som huskone. Vi har inga barn, inga husdjur och inga rökvanor. \n\n Vi kommer från Indien och har bott i Sverige sedan april 2017. Vi letar efter en lägenhet med minst 1 rum. \n Vi har ingen kredithistoria i Sverige eller Indien och säkerställer en lönsam utbetalning av hyran. Vi är ganska sociala och gillar att resa och se platser och träffa nya människor. \n\n Snälla låt mig få veta vad du tänker. Vi väntar på ditt svar. \n\n Med vänliga hälsningar, \n Thameem \n 0734918476";
				try {
					for (String link : newHouseLinks) {
						loggerBlock.info("Applying for house :: " + link);
						driver.get(link);
						Thread.sleep(2000);
						WebElement applyButton = driver.findElement(By.xpath("//*[@id='js-contact-btn-private']"));
						applyButton.click();
						Thread.sleep(2000);
						WebElement textArea = driver.findElement(By.xpath("//*[@id='contact-body']"));
						textArea.sendKeys(Keys.chord(Keys.CONTROL, "a"), msg);
						WebElement sendButton = driver
								.findElement(By.xpath("//*[@id='ad-reply-form']/div[4]/div[2]/button"));
						sendButton.click();
						Thread.sleep(2000);
						WebElement closeButton = driver.findElement(By.xpath("//*[@id='close-modal']"));
						closeButton.click();
						count++;
					}
				} catch (ElementNotVisibleException env) {
					env.printStackTrace();
					loggerBlock.info("ERROR :: message link NOT VISIBLE");
				} catch (NoSuchElementException e) {
					loggerBlock.info("ERROR :: message link no such element");
				}
			}
			loggerBlock.info("Total applied houses in Block ************* " + count);
			if (count == newHouseLinks.size()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			loggerBlock.info("EXCEPTION :: Exception in apply");
		}
		return result;
	}

	private static boolean filterSearchBlock(WebDriver driver, Logger loggerBlock) {
		boolean result = false;
		try {
			if (driver.getPageSource().contains("THAMEEM")) {
				try {
					WebElement agent = driver
							.findElement(By.xpath("//*[@id='blocket_content']/div/div[2]/div/ul[1]/li[3]/a"));
					agent.click();
				} catch (ElementNotVisibleException env) {
					loggerBlock.info("ERROR :: Agentlink NOT VISIBLE");
				}
				result = true;
			} else {
				driver.navigate().refresh();
				loggerBlock.info("ERROR :: User not logged In");
			}
		} catch (Exception e) {
			e.printStackTrace();
			loggerBlock.info("EXCEPTION :: Exception in filterSearch");
		}
		return result;

	}

	private static boolean loginBlock(WebDriver driver, Logger loggerBlock) {
		boolean result = false;
		try {
			driver.get("https://www.blocket.se/");
			driver.manage().window().maximize();
			driver.switchTo().frame(driver.findElement(By.id("ufti-iframe")));
			WebElement popupOk = driver
					.findElement(By.xpath("//*[@id='root']/div/div/div[2]/div/span[2]/div/div/button"));
			popupOk.click();
			WebElement loginLink = driver.findElement(By.xpath("//*[@id='top_nav_mypages']"));
			loginLink.click();
			Thread.sleep(2000);
			WebElement userName = driver.findElement(By.xpath("//*[@id='CredIdentifier']"));
			WebElement passWord = driver.findElement(By.xpath("//*[@id='CredPassword']"));
			WebElement loginButton = driver.findElement(By.xpath("//*[@id='login-form']/div[4]/button"));
			userName.sendKeys("ansur304@gmail.com");
			passWord.sendKeys("@nsari985");
			loginButton.click();
			loggerBlock.info("Logged in Successfully ");
			result = true;
		} catch (WebDriverException wex) {
			loggerBlock.info("WebDriverException in Block");
			driver.quit();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			loggerBlock.info("Exception in login");
		}
		return result;
	}
}
