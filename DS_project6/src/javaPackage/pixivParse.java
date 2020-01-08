package javaPackage;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.jsoup.Jsoup;
import org.openqa.selenium.support.ui.Select;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.PushPromiseHandler;
import java.time.Duration;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

import org.apache.http.impl.client.HttpClients;
import org.jsoup.Connection;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.jsoup.Connection.Method;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.cookie.Cookie;
//import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.BasicCookieStore;
import java.lang.Math;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.commons.exec.OS;
import org.apache.http.HttpHost;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.DriverManagerType;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
public class pixivParse {
	
	public pixivParse() {
		
	}
	public String pixivParsing() throws IOException {
		String retVal="";
		
			String pixivLink="https://www.pixiv.net/artworks/76117294";
			System.out.println(pixivLink);
			URL link=new URL(pixivLink);
			URLConnection conn=link.openConnection();
			InputStream in=conn.getInputStream();
			BufferedReader br= new BufferedReader(new InputStreamReader(in,"UTF-8"));
			
			
			String line=null;
			
			while((line=br.readLine()) !=null) {
				retVal=retVal+line;
			}
		
		int IDstart=retVal.indexOf("authorId")+11;
		int IDend=retVal.indexOf("isLocked");
		int authorNameStart=retVal.indexOf("userName")+11;
		int authorNameEnd=retVal.indexOf("\"},{");
		int illustStart=retVal.indexOf("illustTitle")+14;
		int illustEnd=retVal.indexOf("\",\"id\"");
		String authorID=retVal.substring(IDstart,IDend-3);
		String authorName=retVal.substring(authorNameStart,authorNameEnd);
		String illust=retVal.substring(illustStart,illustEnd).replace("\",\"illustComment\":\"", "\n");
		String info=authorID+"\n"+authorName+"\n"+illust;
		
		
		
		return info;
		
	}
	public String findImg() throws IOException{
		String retVal="";
		String pixivLink="https://www.pixiv.net/member_illust.php?id=490219";
		URL link=new URL(pixivLink);
		URLConnection conn=link.openConnection();
		InputStream in=conn.getInputStream();
		BufferedReader br= new BufferedReader(new InputStreamReader(in,"UTF-8"));
		
		
		String line=null;
		
		while((line=br.readLine()) !=null) {
			retVal=retVal+line+"\n";
		}
	
		
		return retVal;
		
	}
	
	
	//find the (top) tweet from author's comment on that tweet
	public String twitterParsing(String siteLink) throws IOException {
		String retVal="";
		ArrayList<String> links=new ArrayList<String>();
		if(siteLink.contains("twitter")!=false) {
				Document document=Jsoup.connect(siteLink).get();
				Elements dElements=document.select("body").select("a[href]");
				
				
				
				int index=0;
				for(Element each:dElements) {
					index++;
					if(each.attr("href").toString().indexOf("signup")!=-1) {
						break;
					}
				}
//				System.out.println(index);
				String baseString=dElements.get(index).attr("href");
				//System.out.println(baseString);
				for(Element each:dElements) {
					if(each.attr("href").toString().indexOf(baseString+"/status")!=-1) {
						if(("https://twitter.com"+each.attr("href")).equals(siteLink)==false) {
							links.add("https://twitter.com"+each.attr("href"));
//							System.out.println("https://twitter.com"+each.attr("href"));
						}
					}
				}
				
			}
		return links.get(0);
		
		
		
	}
	//get original tweet from random user's retweet
	public String startBrowserTwitter(String linkString,String imgString) {
		System.setProperty("webdriver.chrome.driver","C:\\webdriver79\\chromedriver.exe");
		
		ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
	    ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("--headless");
	    WebDriver driver = new ChromeDriver(chromeOptions);
		driver.navigate().to(linkString);
		
		JavascriptExecutor jsx = (JavascriptExecutor)driver;
		String originalTweet="";
		boolean success=false;
		while (!success) {
			try {
				String ttt=driver.findElement(By.cssSelector("img[src*='"+imgString+"']")).getAttribute("src");
				System.out.println(ttt);
				WebElement element=driver.findElement(By.xpath("//img[contains(@src,'"+imgString+"')]/ancestor::li//div"));
				String twitterID=element.getAttribute("data-screen-name");
				String tweetID=element.getAttribute("data-tweet-id");
				if(driver.findElement(By.xpath("//img[contains(@src,'"+imgString+"')]/ancestor::li//div//div//div//span[2]")).getText().indexOf("Retweeted")==-1) {
					break;
				}
				//System.out.println("https://twitter.com/"+twitterID+"/status/"+tweetID);
				originalTweet="https://twitter.com/"+twitterID+"/status/"+tweetID;
				success=true;
				
			}catch (Exception e) {
				// TODO: handle exception
				
			}
			jsx.executeScript("window.scrollBy(0,200)", "");
			
		}
		//WebDriverWait wait = new WebDriverWait(driver, 10);
		//WebElement elem=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"react-root\"]/div/div/div/main/div/div/div/div[1]/div/div[2]/div/div/div[2]/section/div/div/div/div[1]/div")));
		
		//System.out.println(elem);
		return originalTweet;
		
		
	}
	
	public ArrayList<ArrayList<Object>> startBrowserPixiv(String linkString) {
		System.setProperty("webdriver.chrome.driver","C:\\webdriver79\\chromedriver.exe");
		
		ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
	    ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("--headless");
	    chromeOptions.addArguments("--disable-gpu");
	    chromeOptions.addArguments("disable-infobars");
	    chromeOptions.addArguments("--window-size=1920x3000");
//	    chromeOptions.addArguments("--hide-scrollbars");
	    chromeOptions.addArguments("--disable-javascript");
//	    chromeOptions.addArguments("--disable-plugins");

//	    chromeOptions.addArguments("--remote-debugging-port=4444");
	    chromeOptions.addArguments("--no-sandbox");
//	    chromeOptions.addArguments("--no-proxy-server");
	    chromeOptions.addArguments("--proxy-server='direct://'");
	    chromeOptions.addArguments("--proxy-bypass-list=*");
	    
	    //chromeOptions.addArguments("--start-maximized");
//	    chromeOptions.addArguments("user-data-dir=C:\\Users\\Nick\\ChromeProfiles\\User Data");
	  
//	    chromeOptions.addArguments("--profile-directory=Profile 5");
	   
	    //C:\\Users\\Nick\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 2
	    
	    WebDriver driver = new ChromeDriver(chromeOptions);
//	    driver.manage().window().setSize(new Dimension(2000, 4000));
	    if(linkString.indexOf("member_illust")==-1) {
	    	linkString=linkString.replace("member", "member_illust");
	    }
//	    Set<Cookie> allCookies=driver.manage().getCookies();
//	    for(Cookie eachCookie: allCookies) {
//	    	System.out.println(eachCookie);
//	    	driver.manage().addCookie(eachCookie);
//	    }
	   
		driver.navigate().to(linkString);
		
		ArrayList<ArrayList<Object>> artWorkList=new ArrayList<ArrayList<Object>>();
		
//		JavascriptExecutor jsx = (JavascriptExecutor)driver;
//		if(imgString.indexOf("pixiv.navirank")!=-1) {
//			int start=imgString.indexOf("img/");
//			int end=imgString.indexOf(".jpg");
//			String artWorkId=imgString.substring(start+8,end);
//		}
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
		List<WebElement> webElements = driver.findElement(By.cssSelector("ul[class='_2WwRD0o _2WyzEUZ']"))
				.findElements(By.tagName("li"));
		for(WebElement each: webElements) {
//			if(each.findElement(By.xpath("div/div/div/a/div[1]")).findElements(By.tagName("div")).size()!=3 &&
//					each.findElement(By.xpath("div/div/div/a/div[1]")).findElements(By.tagName("div")).size()!=4) { 
				int numberOfArts=0;
				String pximgUrlString=each.findElement(By.tagName("a")).getAttribute("href");
				
				String imgurlString=each.findElement(By.tagName("img")).getAttribute("src");
				System.out.println(imgurlString);
//				System.out.println(pximgUrlString);

//				int startIndex=pximgUrlString.indexOf("artworks/");
//				pximgUrlString=pximgUrlString.substring(startIndex+9);
				//System.out.println(pximgUrlString);
				//*[@id="root"]/div[1]/div/div[2]/div[1]/section/ul/li[8]/div/div/div/a/div[1]/div[2]/span
				if(each.findElement(By.xpath("div/div/div/a/div[1]")).findElements(By.tagName("div")).size()==2) {
					numberOfArts=Integer.valueOf(each.findElement(By.xpath("div/div/div/a/div[1]/div[2]/span")).getText());
				}
				ArrayList<Object> eachInfo=new ArrayList<Object>();
				eachInfo.add(pximgUrlString);
				eachInfo.add(imgurlString);
				eachInfo.add(numberOfArts);
				eachInfo.add(0);//set base similarity
				artWorkList.add(eachInfo);
			//}
		}
		

		boolean success=true;
		while(success) {
			try {
				WebElement nextpageElement=null;
				try{
					nextpageElement=driver.findElement(By.xpath("//*[@id=\"root\"]/div[3]/div/div[2]/div[2]/a[last()]"));
				}catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
					try{
						nextpageElement=driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div/div[2]/div[2]/a[last()]"));
					}catch (Exception ex) {
						// TODO: handle exception
						System.out.println(ex);
					}
				}
				
				
				if(nextpageElement.getAttribute("class").equals("_2m8qrc7")) {
					nextpageElement.click();
//					actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
					List<WebElement> webNextPageElements = driver.findElement(By.cssSelector("ul[class='_2WwRD0o _2WyzEUZ']"))
							.findElements(By.tagName("li"));
					for(WebElement each:webNextPageElements) {
//						if(each.findElement(By.xpath("div/div/div/a/div[1]")).findElements(By.tagName("div")).size()!=3 &&
//								each.findElement(By.xpath("div/div/div/a/div[1]")).findElements(By.tagName("div")).size()!=4) {
							int numberOfArts=0;
							String pximgUrlString=each.findElement(By.tagName("a")).getAttribute("href");
							String imgurlString=each.findElement(By.tagName("img")).getAttribute("src");
							System.out.println(imgurlString);
//							System.out.println(pximgUrlString);

//							int startIndex=pximgUrlString.indexOf("artworks/");
//							pximgUrlString=pximgUrlString.substring(startIndex+9);
							//System.out.println(pximgUrlString);
							//*[@id="root"]/div[1]/div/div[2]/div[1]/section/ul/li[8]/div/div/div/a/div[1]/div[2]/span
							if(each.findElement(By.xpath("div/div/div/a/div[1]")).findElements(By.tagName("div")).size()==2 ) {
								numberOfArts=Integer.valueOf(each.findElement(By.xpath("div/div/div/a/div[1]/div[2]/span")).getText());
							}
							ArrayList<Object> eachInfo=new ArrayList<Object>();
							eachInfo.add(pximgUrlString);
							eachInfo.add(imgurlString);
							eachInfo.add(numberOfArts);
							eachInfo.add(0);//set base similarity
							artWorkList.add(eachInfo);
						}
					//}
				}else {
					break;
				}
			}catch (Exception e) {
				System.out.println(e);
				success=false;
				// TODO: handle exception
			}
			
		}
		
		//*[@id="root"]/div[1]/div/div[2]/div[2]/a[4]
			
		driver.quit();
		return artWorkList;
		
	}
	
	
	

	public void parse() throws IOException{
		
		 Connection.Response res = 
				 Jsoup.connect("https://accounts.pixiv.net/login?php")

				    .data("pixiv_id", "nick0828a@gmail.com", "password", "nicklee870828")
				    .method(Method.POST)
				    .execute();
				
		
		Document doc = res.parse();
		System.out.println(doc);
		//String sessionId = res.cookie("SESSIONID");
		
		//Document doc2 = Jsoup.connect("https://www.pixiv.net/member.php?id=490219")
			    //.cookie("SESSIONID", sessionId)
			   // .get();
	}
	/*
	<script>
	 
	'use strict';var dataLayer = [{login: 'yes',gender: 
		"male",user_id: "16506863",lang: "zh_tw",illustup_flg:
		'not_uploaded',premium: 'no',}];
	</script>
	*/
	String password= "nicklee870828";
	String pixiv_id= "nick0828a@gmail.com";
	String post_keyString="e02f16e290f64e6f4b3b5212226fe039";
}
