package javaPackage;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.management.loading.PrivateClassLoader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.TransferHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import javax.swing.JFrame;

/**
 * Servlet implementation class runTest
 */
@WebServlet("/WebrunTest")
public class WebrunTest extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private String[] sitekeywords = {"pixiv","twitter.com","instagram"};
	private String[] imgkeywords = {"twimg","pximg"};
	private String imagePath="";
	private String imageName="";
	private LinkedList<ResultSites> resultSitesList=new LinkedList<>(); 
	private String searchedUrl="";
	private File eachImage;
	private ArrayList<String> siteTitleList;
	private static ArrayList<ImageSite> imageSites;
	public static final String GOOGLE_SEARCH_URL = "http://google.com/searchbyimage?image_url=";
	private HashMap<String, String> query=new HashMap<String, String>();
	private Map<Integer, List<String>> finalResultMap=new TreeMap<Integer, List<String>>();
	private List<Score> scoreList=new ArrayList<Score>();
	private String firstPageUrl="";
	
	public void sortScoreList() {
		scoreList.sort(new Comparator<Score>() {
		@Override
			public int compare(Score o1,Score o2) {
				if(o1.score<o2.score) {
					return 1;
				}else if(o1.score==o2.score){
					return 0;
				}else {
					return -1;
				}
			}
			
		});
	}
	
	public List<Score> getScoreList(){
		return scoreList;
	}
	
	public HashMap<String, String> getQueryHashMap(){
		return query;
	}
	
	
	
	
	
	public static boolean stringContainsItemFromList(String inpputStr, String[] items) {
		return Arrays.stream(items).parallel().anyMatch(inpputStr::contains);
	}
	
	public LinkedList<ResultSites> getResultSitesList(){
		sortResultSiteList();
		return resultSitesList;
	}
	
	public void sortResultSiteList() {
		resultSitesList.sort(new Comparator<ResultSites>() {
		@Override
			public int compare(ResultSites o1,ResultSites o2) {
				if(o1.getSimilarity()<o2.getSimilarity()) {
					return 1;
				}else if(o1.getSimilarity()==o2.getSimilarity()){
					return 0;
				}else {
					return -1;
				}
				
			}
			
		});
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebrunTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		imagePath=request.getParameter("file");
		System.out.println(imagePath);
		if(request.getParameter("file")== null) {
			String requestUri = request.getRequestURI();
			request.setAttribute("requestUri", requestUri);
			request.getRequestDispatcher("Index.jsp").forward(request, response);
			return;
		}
		
		imagePath=request.getParameter("file");
		System.out.println(imagePath);
		File file=new File(imagePath);
		imageName=file.getName();
		System.out.println(imageName);
		String base=imagePath.replace(imageName, "");
		//String base="C:/Users/Nick/Pictures/";
		String imageA=imageName;
		//String imageB="photomania-f35b9c6eb4fc17a95d1e44c973a94a39.jpg";
		String imageB="";	
		String searchUrl="https://www.google.co.jp/searchbyimage/upload";
		resultSitesList.clear();
		GoogleSearchJava searchImage=new GoogleSearchJava();
		urlToFile urlToFile=new urlToFile("", base);
		HashMap<String,String> topWebsitesLinksList=new HashMap<String, String>();
		finalResultMap=new TreeMap<Integer, List<String>>();
		
		
		double i=0;
		
			try {
				searchImage.imageSearch(base+imageA);
				
			System.out.println(searchImage.getRelatedKeyword());
			firstPageUrl=searchImage.getSearchedUrl();
			pixivParse pixivParse=new pixivParse();
			compareImage compareImage=new compareImage(imageA, "");
			File fileA=new File(base+imageA);
			BufferedImage bufferedImage=ImageIO.read(fileA);
			int width=bufferedImage.getWidth();
			int height=bufferedImage.getHeight();
			compareImage.resize(fileA, base+"compressedA.jpg", width, height);
			
			//System.out.println(progessTotal);
			for(ImageSite each:searchImage.getAllSites()) {
				//see if each site's image and site's link contains target keywords
				if(stringContainsItemFromList(each.getUrl(), imgkeywords) && stringContainsItemFromList(each.getSiteLink(), sitekeywords)) {
					String title=each.getTitle();
					String imgUrl=each.getUrl();
					String siteLink=each.getSiteLink();
					System.out.println(title);
					System.out.println(imgUrl);
					System.out.println(siteLink);
					float similarity=0;
					
				try {
					imageB=each.getUrl();
					urlToFile=new urlToFile(imageB, base);
					String imageBPath=urlToFile.getImgPath();
					//System.out.println(imageBPath);
					compareImage=new compareImage(base+imageA, imageBPath);
					compareImage.resize(urlToFile.getFile(), base+"compressedB.jpg",width,height );
					//System.out.println(width+"x"+height);
					
					similarity=compareImage.compare();
					}catch (Exception e) {
					// TODO: handle exception
					}
				
					System.out.println(similarity);
					System.out.println( );
					
					resultSitesList.add(new ResultSites(imgUrl, siteLink,similarity));
					
					}
			}//end of search loop
				
			
			resultSitesList=getResultSitesList();
			String originalTweetTop="";
			String originalTweetTextString="";
			String postTextString="";
			String originalTweet="";
			for(ResultSites each:resultSitesList) {
				if(each.getImgUrl().indexOf("twimg")!=-1 && each.getSiteLink().indexOf("twitter")!=-1) {
					if(each.getSiteLink().indexOf("status")!=-1 && each.getSimilarity()>50) {
						int favCount=0;
						try {
							Document tweetDoc=Jsoup.connect(each.getSiteLink()).get();
							favCount=Integer.valueOf(tweetDoc.select("span.ProfileTweet-action--favorite > span.ProfileTweet-actionCount")
				                    .attr("data-tweet-stat-count"));
							originalTweetTextString=tweetDoc.select("p.TweetTextSize.TweetTextSize--jumbo.js-tweet-text.tweet-text").text();
						}catch (Exception e) {
							// TODO: handle exception
							System.out.println(e);
							System.out.println("00error");
						}
						System.out.println(favCount);
						if(favCount>50) {
							System.out.println("favcountover50");
							if(originalTweetTextString.indexOf("pic")!=-1) {
								
								try{
									
									originalTweet=each.getSiteLink();
									System.out.println(originalTweet);
									break;
								}catch (Exception e) {
									// TODO: handle exception
									System.out.println(e);
									System.out.println("11error");
								}
							}else {
								try{
									originalTweetTop=pixivParse.twitterParsing(each.getSiteLink());
									originalTweet=originalTweetTop;
									System.out.println("comment found , return top tweet link");
									System.out.println(originalTweet);
									break;
								}catch (Exception e) {
									// TODO: handle exception
									System.out.println(e);
									System.out.println("22error");
								}
								
							}
						}
					}
				}
				
			}
			if(originalTweet.equals("")!=false) {
				for(ResultSites each:resultSitesList)  {
					if(each.getImgUrl().indexOf("twimg")!=-1 && each.getSiteLink().indexOf("twitter")!=-1) {
						if(each.getSiteLink().indexOf("status")==-1 && each.getSiteLink().indexOf("hashtag")==-1) {
								try{
									originalTweet=pixivParse.startBrowserTwitter(each.getSiteLink(), each.getImgUrl());
									System.out.println(originalTweet);
									Document tweetDoc=Jsoup.connect(originalTweet).get();
									originalTweetTextString=tweetDoc.select("p.TweetTextSize.TweetTextSize--jumbo.js-tweet-text.tweet-text").text();
									if(originalTweet.indexOf("status")==-1) {
										continue;
									}else {
										break;
									}
								}catch (Exception e) {
									// TODO: handle exception
									System.out.println(e);
									System.out.println("33error");
								}
							
						}
					}	
			}
			}
			
			//research with keyword:twitter if no results found
			if(originalTweet.equals("")!=false) {
				firstPageUrl=firstPageUrl.replace("search?", "search?q=twitter&");
				searchImage.research(firstPageUrl);
				for(ImageSite each:searchImage.getAllSites()) {
					//see if each site's image and site's link contains target keywords
					if(stringContainsItemFromList(each.getUrl(), imgkeywords) && stringContainsItemFromList(each.getSiteLink(), sitekeywords)) {
						String title=each.getTitle();
						String imgUrl=each.getUrl();
						String siteLink=each.getSiteLink();
						System.out.println(title);
						System.out.println(imgUrl);
						System.out.println(siteLink);
						float similarity=0;
						
					try {
						imageB=each.getUrl();
						urlToFile=new urlToFile(imageB, base);
						String imageBPath=urlToFile.getImgPath();
						//System.out.println(imageBPath);
						compareImage=new compareImage(base+imageA, imageBPath);
						compareImage.resize(urlToFile.getFile(), base+"compressedB.jpg",width,height );
						//System.out.println(width+"x"+height);
						
						similarity=compareImage.compare();
						}catch (Exception e) {
						// TODO: handle exception
						}
					
						System.out.println(similarity);
						System.out.println( );
						
						resultSitesList.add(new ResultSites(imgUrl, siteLink,similarity));
						
						}
				}//end of search loop
					
				
				resultSitesList=getResultSitesList();
				for(ResultSites each:resultSitesList) {
					if(each.getImgUrl().indexOf("twimg")!=-1 && each.getSiteLink().indexOf("twitter")!=-1) {
						if(each.getSiteLink().indexOf("status")!=-1 && each.getSimilarity()>50) {
							int favCount=0;
							try {
								Document tweetDoc=Jsoup.connect(each.getSiteLink()).get();
								favCount=Integer.valueOf(tweetDoc.select("span.ProfileTweet-action--favorite > span.ProfileTweet-actionCount")
					                    .attr("data-tweet-stat-count"));
								originalTweetTextString=tweetDoc.select("p.TweetTextSize.TweetTextSize--jumbo.js-tweet-text.tweet-text").text();
								
							}catch (Exception e) {
								// TODO: handle exception
								System.out.println(e);
								System.out.println("44error");
							}
							System.out.println(favCount);
							if(favCount>50) {
								System.out.println("favcountover50");
								if(originalTweetTextString.indexOf("pic")!=-1) {
									try{
										
										originalTweet=each.getSiteLink();
										System.out.println(originalTweet);
										break;
									}catch (Exception e) {
										// TODO: handle exception
										System.out.println(e);
										System.out.println("55error");
									}
								}else {
									try{
										originalTweetTop=pixivParse.twitterParsing(each.getSiteLink());
										originalTweet=originalTweetTop;
										System.out.println("comment found , return top tweet link");
										System.out.println(originalTweet);
										break;
									}catch (Exception e) {
										// TODO: handle exception
										System.out.println(e);
										System.out.println("66error");
									}
									
								}
							}
						}
					}
					
				}
				if(originalTweet.equals("")!=false) {
					for(ResultSites each:resultSitesList)  {
						if(each.getImgUrl().indexOf("twimg")!=-1 && each.getSiteLink().indexOf("twitter")!=-1) {
							if(each.getSiteLink().indexOf("status")==-1 && each.getSiteLink().indexOf("hashtag")==-1) {
									try{
										originalTweet=pixivParse.startBrowserTwitter(each.getSiteLink(), each.getImgUrl());
										System.out.println(originalTweet);
										Document tweetDoc=Jsoup.connect(originalTweet).get();
										originalTweetTextString=tweetDoc.select("p.TweetTextSize.TweetTextSize--jumbo.js-tweet-text.tweet-text").text();
										if(originalTweet.indexOf("status")==-1) {
											continue;
										}else {
											break;
										}
									}catch (Exception e) {
										// TODO: handle exception
										System.out.println(e);
										System.out.println("77error");
									}
								
							}
						}	
				}
				}
				
			}
			
			
			Thread.sleep(1000);
			
			
			String mainTwitterLink=originalTweet.substring(0,originalTweet.indexOf("/status"));
			System.out.println(mainTwitterLink);
			Document document=Jsoup.connect(originalTweet)
//					  .ignoreContentType(true)
//					  .timeout(12000)
//					  .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36") // missing
					  .get();
			
			Document document2=Jsoup.connect(mainTwitterLink)
//					  .ignoreContentType(true)
//					  .timeout(12000)
//					  .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36")
					  .get();
			
			String tweetTitle=document.title();
			System.out.println();
			String twitterTitle="";
			
			
			
			boolean success1=false;
			while (!success1) {
				try{
					twitterTitle=document2.title();
					success1=true;
				}catch (Exception e) {
					// TODO: handle exception
					System.out.println(mainTwitterLink);
					document2=Jsoup.connect(mainTwitterLink).get();
					System.out.println(e);
				}
			}
			
			System.out.println("twitterTitle: "+twitterTitle);
			
			
//			boolean success=false;
//			while (!success) {
//				try{
//					postTextString=document.select("p.TweetTextSize.TweetTextSize--jumbo.js-tweet-text.tweet-text").text();
//					//.select("p.js-tweet-text.tweet-text").first().text()
//					if(postTextString.indexOf("pic")!=-1) {
//						
//						postTextString=postTextString.substring(0, postTextString.indexOf("pic"));
//					}
//					success=true;
//				}catch (Exception e) {
//					// TODO: handle exception
//					System.out.println(originalTweetTop);
//					document=Jsoup.connect(originalTweetTop).get();
//					System.out.println(e);
//				}
//			}
			boolean success=false;
			while (!success) {
				try{
					originalTweetTextString=document.select("p.TweetTextSize.TweetTextSize--jumbo.js-tweet-text.tweet-text").text();
					//.select("p.js-tweet-text.tweet-text").first().text()
					
					if(originalTweetTextString.indexOf("pic")!=-1) {
						
						originalTweetTextString=originalTweetTextString.substring(0, originalTweetTextString.indexOf("pic"));
					}
					if(originalTweetTextString.length()>0) {
						success=true;
					}
				}catch (Exception e) {
					// TODO: handle exception
					System.out.println(originalTweet);
					document=Jsoup.connect(originalTweet).get();
					System.out.println(e);
				}
			}
			
			
			
			
//			if(originalTweetTextString.indexOf("pic")!=-1) {
//					originalTweetTextString=originalTweetTextString.substring(0, originalTweetTextString.indexOf("pic"));
//			}else {
//				try {
//					originalTweetTextString=document.select("p.TweetTextSize.TweetTextSize--jumbo.js-tweet-text.tweet-text").text();
//					if(originalTweetTextString.indexOf("pic")!=-1) {
//						originalTweetTextString=originalTweetTextString.substring(0, originalTweetTextString.indexOf("pic"));
//					}
//				}catch (Exception e) {
//					// TODO: handle exception
//					System.out.println(e);
//				}
//			}
					
				
			
//			System.out.println(postTextString);
			System.out.println(originalTweetTextString);
			topWebsitesLinksList.put(twitterTitle, mainTwitterLink);
			topWebsitesLinksList.put(tweetTitle, originalTweet);
			
			try{
				Elements profile=document2.select("#page-container > div.AppContainer > div > div > div.Grid-cell.u-size1of3.u-lg-size1of4 > div > div > div > div.ProfileHeaderCard > p");
			
				//ul.ProfileNav-list
//					System.out.println(stats);
//					System.out.println(profile.size());
				profile=profile.select("a");
				
//					System.out.println(profile);
				for(Element each:profile) {
					try {
						String externalLinks=each.attr("data-expanded-url");
						String externalLinkTitle=Jsoup.connect(externalLinks).get().title();
						System.out.println(externalLinks);
						topWebsitesLinksList.put(externalLinkTitle,externalLinks);
					}catch (Exception ex) {
						// TODO: handle exception
						System.out.println(ex);
					}
				}
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
			try {
				Elements profile2=document2.select("#page-container > div.AppContainer > div > div > div.Grid-cell.u-size1of3.u-lg-size1of4 > div > div > div > div.ProfileHeaderCard > div.ProfileHeaderCard-url > span");
				String extraSiteLink=profile2.select("a").attr("title");
				String extraSiteTitle=Jsoup.connect(extraSiteLink).get().title();
				System.out.println(extraSiteTitle+" : "+extraSiteLink);
				topWebsitesLinksList.put(extraSiteTitle, extraSiteLink);
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
			
			
			
			
			if(twitterTitle!="") {
				twitterTitle=twitterTitle.replace(" | Twitter", "");
				twitterTitle=twitterTitle.substring(0,twitterTitle.indexOf(" (@"));
			}else {
				twitterTitle=document2.title();
				try{
					twitterTitle=twitterTitle.replace(" | Twitter", "");
					twitterTitle=twitterTitle.substring(0,twitterTitle.indexOf(" (@"));
				}catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}
			}
			System.out.println(twitterTitle+"+"+originalTweetTextString);// or postTextString
			GoogleQuery googleQuery=new GoogleQuery(twitterTitle+"+"+originalTweetTextString);// or postTextString
			query = googleQuery.query();
			twitterTitle.replaceAll("[^\\x20-\\x7e]", "");
			if(twitterTitle.indexOf(" /")!=-1) {
				twitterTitle=twitterTitle.substring(0,twitterTitle.indexOf(" /"));
			}else if(twitterTitle.indexOf(" |")!=-1) {
				twitterTitle=twitterTitle.substring(0,twitterTitle.indexOf(" |"));
			}else if(twitterTitle.indexOf("@")!=-1){
				twitterTitle=twitterTitle.substring(0,twitterTitle.indexOf("@"));
			}else if(twitterTitle.indexOf(" @")!=-1){
				twitterTitle=twitterTitle.substring(0,twitterTitle.indexOf(" @"));
			}else if(twitterTitle.indexOf("｜")!=-1){
				twitterTitle=twitterTitle.substring(0,twitterTitle.indexOf("｜"));
			}
			
			System.out.println();
			System.out.println();
			System.out.println(twitterTitle+"+"+originalTweetTextString);//or postTextString
			GoogleQuery googleQuery2=new GoogleQuery(twitterTitle+"+"+originalTweetTextString);//or postTextString
			query.putAll(googleQuery2.query());
			System.out.println();
			System.out.println();
			System.out.println(twitterTitle);
			GoogleQuery googleQuery3=new GoogleQuery(twitterTitle);
			query.putAll(googleQuery3.query());
			System.out.println();
			System.out.println();
			System.out.println(twitterTitle+"+繪師+wiki");
			GoogleQuery googleQuery4=new GoogleQuery(twitterTitle+"+繪師+wiki");
			query.putAll(googleQuery4.query());
			System.out.println();
			System.out.println();
			
			
			String[] topWebFilter = {"pixiv.net/member","pixiv.net/artworks","instagram.com","moegirl.org"};
			String[] urlFilter = {"pixiv","gamer.com.tw","ptt.cc","wikipedia.org","plurk"};
			String[] excludedWebsites= {"circlecheck.site"};
//			for(Map.Entry<String, String> entry : query.entrySet()) {
//				try{
//					String titleString=entry.getKey();
//					String linkString=entry.getValue();
//				
//					if(stringContainsItemFromList(linkString, urlFilter)) {
//						try{
//						topWebsitesLinksList.put(titleString,linkString);
//						query.remove(titleString);}
//						catch (Exception e) {
//							// TODO: handle exception
//							System.out.println(e);
//						}
//					}
//				}catch (Exception e) {
//					// TODO: handle exception
//					System.out.println(e);
//				}
//			}
			
			//targeted websites dont need to be scored, move them straight to the topweblist
			Iterator<Map.Entry<String,String>> iter = query.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<String,String> entry = iter.next();
			    if(stringContainsItemFromList(entry.getValue(), topWebFilter) && entry.getKey().indexOf(twitterTitle)!=-1){
			    	topWebsitesLinksList.put(entry.getKey(),entry.getValue());
			        iter.remove();
			    }else if(entry.getValue().indexOf("twitter.com")!=-1 && entry.getKey().indexOf(tweetTitle)!=-1) {
			    	iter.remove();
			    }
			}
			
			System.out.println(twitterTitle);
			
			scoreList=new ArrayList<Score>();
//			HashMap<String, Score> scoreMap=new HashMap<String, Score>();
			long start = System.nanoTime();  
			for(Map.Entry<String, String> entry : query.entrySet()) {
				String title=entry.getKey();
				String link=entry.getValue();
				System.out.println(title);
			    System.out.println(link);
			    if(stringContainsItemFromList(link, excludedWebsites)!=true) {
			    WebPage rootPage = new WebPage(link, title);	
			    WebTree tree = new WebTree(rootPage);
			    try {
					Document firstLayer=Jsoup.connect(rootPage.url).get();
//						for(Element)
//						if(stringContainsItemFromList(each.getUrl(), urlFilter)) {
					ArrayList<String> firstLayerLinks=new ArrayList<String>();
					org.jsoup.select.Elements firstLayerElements=firstLayer.select("a[href]");
					int j=0;
					for(Element each:firstLayerElements ) {
						j++;
						if(j>10) {
							break;
						}
						String httpLink=each.attr("href");
						if(httpLink.equals(rootPage.url)==false) {
							if(stringContainsItemFromList(httpLink, urlFilter) && httpLink.indexOf("http")!=-1) {
								if(httpLink.indexOf("twitter.com")!=-1) {
									if(firstLayerLinks.contains(httpLink)==false) {
										if(httpLink.indexOf("/status")!=-1) {
											firstLayerLinks.add(httpLink);
										}
									}
								}else {
									if(firstLayerLinks.contains(httpLink)==false) {
										firstLayerLinks.add(httpLink);
									}
								}
							}
						}
					}
					
					for(String each:firstLayerLinks) {
						System.out.println(each);
						tree.root.addChild(new WebNode(new WebPage(each,each)));
						
//							i++;
					}
					
					ArrayList<Keyword> keywords = new ArrayList<Keyword>();
					
					Keyword k = new Keyword(twitterTitle, 1);
					keywords.add(k);
					
					
					
					tree.setPostOrderScore(keywords);
					tree.eularPrintTree();
			    	
					double totalScore=tree.getTotalScore();
					if(totalScore>0) {
						Score score=new Score(title,tree.getTotalScore());
						scoreList.add(score);
					}
					System.out.println(tree.getTotalScore());
					
			    }catch (Exception e) {
					// TODO: handle exception
			    	System.out.println(e);
				}
			    }
			}
			long elapsedTime = System.nanoTime() - start;
			System.out.println(elapsedTime);
			
			
			System.out.println();
			System.out.println();
			
			System.out.println("top:");
			//print out searched result
			int keyInt=5;
			for(Map.Entry<String, String> entry : topWebsitesLinksList.entrySet()) {
				String title=entry.getKey();
				String link=entry.getValue();
				System.out.println(title);
			    System.out.println(link);
			    
			    List<String> keyString=new ArrayList<String>();
			    keyString.add(title);
			    keyString.add(link);
			    if(link.indexOf("pixiv.net/artworks")!=-1) {
			    	finalResultMap.put(1, keyString);
			    }else if(link.indexOf("pixiv.net/member")!=-1) {
			    	finalResultMap.put(2, keyString);
			    }else if(link.indexOf("twitter.com")!=-1 && link.indexOf("status")!=-1) {
			    	finalResultMap.put(3, keyString);
			    }else if(link.indexOf("twitter.com")!=-1 && link.indexOf("status")==-1) {
			    	finalResultMap.put(4, keyString);
			    }else {
			    	finalResultMap.put(keyInt, keyString);
			    }
			    keyInt++;
			    
			}
			
			
			
			System.out.println("score:");
			sortScoreList();
			
			if(10<scoreList.size()) {
				for(int j=0;j<10;j++) {
					keyInt++;
					String title=scoreList.get(j).titleString;
					String link=query.get(title);
					System.out.println(title);
				    System.out.println(link);
				    List<String> keyString=new ArrayList<String>();
				    keyString.add(title);
				    keyString.add(link);
				    finalResultMap.put(keyInt, keyString);
				}
			}else {
				for(Score each: scoreList) {
					keyInt++;
					String title=each.titleString;
					String link=query.get(title);
					System.out.println(title);
				    System.out.println(link);
				    List<String> keyString=new ArrayList<String>();
				    keyString.add(title);
				    keyString.add(link);
				    finalResultMap.put(keyInt, keyString);
				}
			}
			
			
			
			
			
			
			
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
				
				
				siteTitleList=searchImage.getSiteTitleList();
				int j=0;
				for(ImageSite each:searchImage.getAllSites()) {
					List<String> stringList=new ArrayList<String>();
					stringList.add(siteTitleList.get(j));
					stringList.add(each.getSiteLink());
					finalResultMap.put(j,stringList);
					j++;
					if(j>=10) {
						break;
					}
				}
			}
			
			
		
		
	
//		request.getSession().setAttribute("query" , query);
//		request.getSession().setAttribute("scoreList" , scoreList);
		request.getSession().setAttribute("final" , finalResultMap);

		request.getRequestDispatcher("display.jsp")
		 .forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
	
		
	}
}
