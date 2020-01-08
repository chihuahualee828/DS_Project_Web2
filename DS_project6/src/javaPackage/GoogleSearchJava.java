package javaPackage;

import java.io.File;
import org.jsoup.Connection.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Scanner;
import org.jsoup.parser.Parser;
import javax.imageio.ImageIO;
import javax.lang.model.element.VariableElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import org.apache.http.client.HttpClient;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.commons.codec.DecoderException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import org.apache.http.util.EntityUtils;
public class GoogleSearchJava {
	private String searchedUrl="";
	private File eachImage;
	private ArrayList<String> siteTitleList;
	private static ArrayList<ImageSite> imageSites;
	String relatedKeywordString="";
	public static final String GOOGLE_SEARCH_URL = "http://google.com/searchbyimage?image_url=";
	public void imageSearch(String imagePath) throws IOException {
		imageSites=new ArrayList<ImageSite>();
	    siteTitleList=new ArrayList<String>();
	    
		try {

		  String base="https://www.google.co.jp/searchbyimage/upload";
		 
		  HttpPost post=new HttpPost(base);
		  
		  
		//without proper User-Agent, we will get 403 error
		  MultipartEntityBuilder entity = MultipartEntityBuilder.create();
		  entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		  FileBody fileBody=new FileBody(new File(imagePath));
		  
	      entity.addPart("encoded_image", fileBody);
	      HttpEntity entity2=entity.build();
	      post.setEntity(entity2);
	      HttpClient client = new DefaultHttpClient();
	      HttpResponse response = client.execute(post);
//	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));         
	      String url=String.valueOf(response.getFirstHeader("Location")).substring(10);
	      System.out.println(url);
	      searchedUrl=url;
	      Response Jresponse1= Jsoup.connect(url)
	      .ignoreContentType(true)
          .header("Accept-Encoding", "gzip, deflate")
          .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")  
          .maxBodySize(0)   
          .timeout(600000) 
          .followRedirects(true)
          .execute();
	     Document doc=Jresponse1.parse();
	     /*int index=String.valueOf(doc).indexOf("data:image/jpeg");
	     System.out.println(String.valueOf(doc).substring(index,index+1000));
	     */
	     /*String background=doc.select("style[id]").html();
	     int index=background.indexOf("data:image/gif");
	     int end=background.indexOf("w==");
	     background=background.substring(index,end+3);
	     System.out.println(background);
	     */
	     //Elements links=doc.select("a[href]");
	     //Elements sites=doc.select("div[class]");
//	     Elements webSites=doc.select("div[data-hveid]");
	     Elements firstPageBlocks = doc.getElementsByClass("rc");
	     relatedKeywordString=doc.select("input[class=gLFyf gsfi]").attr("value");
//	     System.out.println(relatedKeywordString);
	     //*for(Element site : sites) {
	    	/* if(String.valueOf(site.attr("class")).equals("S1KrM")) {
	    		 System.out.println("Yess");
	    		 Elements AllImg=site.select("g-img[class]");
	    		 for(Element eachImg:AllImg) {
	    			 System.out.println(eachImg.select("img[id]").attr("id"));
	    			 System.out.println(eachImg.select("img[id]").attr("src"));
	    			 System.out.println(eachImg.select("img[id]").attr("title"));
	    			 
	    	 
	    			 //String imageData=String.valueOf(eachImg.select("img[id]").attr("src"));
	    			 imageData=imageData.substring(imageData.indexOf(",")+1);
	    			 System.out.println(imageData);
	    			 BufferedImage image=null;
	    			 byte[] imageByte;
	    			 Decoder decoder=Base64.getDecoder();
	    			 imageByte=decoder.decode(imageData);
	    			 ByteArrayInputStream bis=new ByteArrayInputStream(imageByte);
	    			 image=ImageIO.read(bis);
	    			 bis.close();
	    			 
	    			 File outputFile=new File("image.png");
	    			 //ImageIO.write(image, "png", outputFile);
	    		 }
	    	 }*/
	    	/*if(site.hasAttr("data-hveid")) {
		    	 System.out.println(String.valueOf(site.attr("data-hveid")));
		    	 if(String.valueOf(site.attr("data-hveid")).equals("CAAQAA")) {
		    		 System.out.println("Yes");
			    	 Elements imgIds=site.select("div[data-hveid]").select("g-img[class]");
			    	 
			    	 for(Element imgId:imgIds) {
			    		 System.out.println(imgId.attr("class"));
			    		 if(imgId.hasAttr("title")) {
			    			 String src=imgId.attr("src");
			    			 String base64str=src.substring(22);
			    			 String decoded=new String(Base64.getEncoder().encode(base64str.getBytes()));
			    			 
			    			 System.out.println(src);
			    			 System.out.println(imgId.attr("title"));
			    			 System.out.println(imgId.attr("width"));
			    			 System.out.println();
			    		 }
			    	 }
			    	 
		    	 }*/
	    for(Element each : firstPageBlocks) {
	    	String titleString=each.select("h3").text();
	    	siteTitleList.add(titleString);
	    	
	    	System.out.println(titleString);
	    	
	    	String link=each.getElementsByClass("s").select("a[class]").attr("href");
	    	
	    	try {
		    	int start=link.indexOf("/imgres?imgurl=");
		    	int end=link.indexOf("&imgrefurl");
		    	
		    	link=link.substring(start+15,end);
	    	}catch (Exception e) {
				// TODO: handle exception
			}
	    	
	    	System.out.println(link);
	    	String siteLink=each.select("a[href]").attr("href");
	    	System.out.println(siteLink);
	    	//System.out.println();
	    	ImageSite imageSite=new ImageSite(titleString,link, siteLink);
	    	imageSites.add(imageSite);
	    }
	    
	    /*
	    for(Element each:webSites) {
	    	System.out.println(each.select("h3").text());
	    	if(each.select("a[class]").hasAttr("style")) {
	    		//System.out.println(each.attr("data-hveid"));
	    		
	    		if(each.select("a[class]").hasAttr("style")) {
	    			Elements classes=each.select("a[class]");
	    			for(Element eachClass : classes) {
	    				if(eachClass.hasAttr("style")) {
					    	String link=eachClass.select("a[class]").attr("href");
					    	
					    	int start=link.indexOf("/imgres?imgurl=");
					    	int end=link.indexOf("&imgrefurl");
					    	
					    	link=link.substring(start+15,end);
					    	//System.out.println(link);
					    	String siteLink=each.select("a[href]").attr("href");
					    	System.out.println(siteLink);
					    	//System.out.println();
					    	ImageSite imageSite=new ImageSite(link, siteLink);
					    	imageSites.add(imageSite);
	    				}
	    			}
	    	}
	    }
	    }
	    
	    */
	    System.out.println();
	    
	    Elements pages=doc.select("tbody").select("td").select("a[aria-label]");
	    
	    String nextPage="";
	    for(Element eachPage:pages) {
	    	nextPage="https://www.google.co.jp"+eachPage.attr("href");
	    	System.out.println(nextPage);
	    	String ua = "Mozilla/5.0 ;Windows NT 6.1; "
	    		     + "WOW64; AppleWebKit/537.36 ;KHTML, like Gecko; "
	    		     + "Chrome/39.0.2171.95 Safari/537.36";
	    	
	    	Response jResponse=Jsoup.connect(nextPage).ignoreContentType(true)
	    	        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/46.0.1")
	    	               .referrer("http://www.google.com")
	    	               .timeout(12000)
	    	               .followRedirects(true)
	    	               .execute();
	    	
	    	Document doc2=jResponse.parse();
	    	
	    	Elements blocksElements = doc2.getElementsByClass("rc");
	    	for(Element block:blocksElements) {
	    		String titleString=block.select("h3").text();
	    		siteTitleList.add(titleString);
		    	System.out.println(titleString);
		    	
		    	String link=block.getElementsByClass("s").select("a[class]").attr("href");
		    	try {
			    	int start=link.indexOf("/imgres?imgurl=");
			    	int end=link.indexOf("&imgrefurl");
			    	
			    	link=link.substring(start+15,end);
		    	}catch (Exception e) {
				}
		    	System.out.println(link);
		    	String siteLink=block.select("a[href]").attr("href");
		    	System.out.println(siteLink);
		    	//System.out.println();
		    	ImageSite imageSite=new ImageSite(titleString,link, siteLink);
		    	imageSites.add(imageSite);
	    	}
//	    	Elements webSites2=doc2.select("div[data-hveid]");

			
	    	System.out.println();
	    }
	    Response Jresponse2= Jsoup.connect(nextPage)
	  	      .ignoreContentType(true)
	            .header("Accept-Encoding", "gzip, deflate")
	            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")  
	            .maxBodySize(0)   
	            .timeout(600000) 
	            .followRedirects(true)
	            .execute();
  	     Document nextDoc=Jresponse2.parse();
  	     pages=nextDoc.select("tbody").select("td").select("a[aria-label]");
  	   for(Element eachPage:pages) {
	    	nextPage="https://www.google.co.jp"+eachPage.attr("href");
	    	System.out.println(nextPage);
	    	String ua = "Mozilla/5.0 ;Windows NT 6.1; "
	    		     + "WOW64; AppleWebKit/537.36 ;KHTML, like Gecko; "
	    		     + "Chrome/39.0.2171.95 Safari/537.36";
	    	
	    	Response jResponse=Jsoup.connect(nextPage).ignoreContentType(true)
	    	        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/46.0.1")
	    	               .referrer("http://www.google.com")
	    	               .timeout(12000)
	    	               .followRedirects(true)
	    	               .execute();
	    	
	    	Document doc2=jResponse.parse();
	    	
	    	Elements blocksElements = doc2.getElementsByClass("rc");
	    	for(Element block:blocksElements) {
	    		String titleString=block.select("h3").text();
	    		siteTitleList.add(titleString);
		    	System.out.println(titleString);
		    	
		    	String link=block.getElementsByClass("s").select("a[class]").attr("href");
		    	try {
			    	int start=link.indexOf("/imgres?imgurl=");
			    	int end=link.indexOf("&imgrefurl");
			    	
			    	link=link.substring(start+15,end);
		    	}catch (Exception e) {
				}
		    	System.out.println(link);
		    	String siteLink=block.select("a[href]").attr("href");
		    	System.out.println(siteLink);
		    	//System.out.println();
		    	ImageSite imageSite=new ImageSite(titleString,link, siteLink);
		    	imageSites.add(imageSite);
	    	}
	    	Elements webSites2=doc2.select("div[data-hveid]");

			
	    	System.out.println();
	    }
	    
	     
	  	     
	  	     
	  	     
	  	     
	  	     
	  	     
	  	     
	  	     
	  	     
	  	     
	  	     
	     /*Elements img=doc.select("img");
	     for(Element imgElement:img) {
	    	 if(String.valueOf(imgElement).indexOf("img id")!=-1 || String.valueOf(imgElement).indexOf("img src")!=-1) {
	    		 System.out.println(imgElement.attr("src"));
	    		 if(imgElement.attr("title")!="")
	    			 System.out.println(imgElement.attr("title"));
	    		 	 System.out.println();
	    			 
	    		 
	    	 }
	     }*/
	     
	    /*if(site.select("div[data-hveid]").equals(null)==false) {
   		 System.out.println("no");
   		 System.out.println(site.select("img[src]").attr("src"));
   		 System.out.println(site.select("a[href]").attr("href"));
   	 	} */

	    
	    
		}catch (Exception e) {
			// TODO: handle exception
		}
		//below will print HTML data, save it to a file and open in browser to compare
		//System.out.println(doc.html());
		
		//If google search results HTML change the <h3 class="r" to <h3 class="r1"
		//we need to change below accordingly
		/*for(ImageSite each : imageSites) {
			System.out.println(each.toString());
		}
		System.out.println(imageSites.size());
		*/
		
		
	}
	public ArrayList<ImageSite> getAllSites(){
		return imageSites;
	}
	
	public void research(String urlString) {
		try {
		      System.out.println(urlString);
		      
		      Response Jresponse1= Jsoup.connect(urlString)
		      .ignoreContentType(true)
	          .header("Accept-Encoding", "gzip, deflate")
	          .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")  
	          .maxBodySize(0)   
	          .timeout(600000) 
	          .followRedirects(true)
	          .execute();
		     Document doc=Jresponse1.parse();
     
//		     Elements webSites=doc.select("div[data-hveid]");
		     Elements firstPageBlocks = doc.getElementsByClass("rc");
		     relatedKeywordString=doc.select("input[class=gLFyf gsfi]").attr("value");

		    for(Element each : firstPageBlocks) {
		    	String titleString=each.select("h3").text();
		    	siteTitleList.add(titleString);
		    	
		    	System.out.println(titleString);
		    	
		    	String link=each.getElementsByClass("s").select("a[class]").attr("href");
		    	
		    	try {
			    	int start=link.indexOf("/imgres?imgurl=");
			    	int end=link.indexOf("&imgrefurl");
			    	
			    	link=link.substring(start+15,end);
		    	}catch (Exception e) {
					// TODO: handle exception
				}
		    	
		    	System.out.println(link);
		    	String siteLink=each.select("a[href]").attr("href");
		    	System.out.println(siteLink);
		    	//System.out.println();
		    	ImageSite imageSite=new ImageSite(titleString,link, siteLink);
		    	imageSites.add(imageSite);
		    }
		    System.out.println();
		    
		    Elements pages=doc.select("tbody").select("td").select("a[aria-label]");
		    
		    String nextPage="";
		    for(Element eachPage:pages) {
		    	nextPage="https://www.google.co.jp"+eachPage.attr("href");
		    	System.out.println(nextPage);
		    	String ua = "Mozilla/5.0 ;Windows NT 6.1; "
		    		     + "WOW64; AppleWebKit/537.36 ;KHTML, like Gecko; "
		    		     + "Chrome/39.0.2171.95 Safari/537.36";
		    	
		    	Response jResponse=Jsoup.connect(nextPage).ignoreContentType(true)
		    	        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/46.0.1")
		    	               .referrer("http://www.google.com")
		    	               .timeout(12000)
		    	               .followRedirects(true)
		    	               .execute();
		    	
		    	Document doc2=jResponse.parse();
		    	
		    	Elements blocksElements = doc2.getElementsByClass("rc");
		    	for(Element block:blocksElements) {
		    		String titleString=block.select("h3").text();
		    		siteTitleList.add(titleString);
			    	System.out.println(titleString);
			    	
			    	String link=block.getElementsByClass("s").select("a[class]").attr("href");
			    	try {
				    	int start=link.indexOf("/imgres?imgurl=");
				    	int end=link.indexOf("&imgrefurl");
				    	
				    	link=link.substring(start+15,end);
			    	}catch (Exception e) {
					}
			    	System.out.println(link);
			    	String siteLink=block.select("a[href]").attr("href");
			    	System.out.println(siteLink);
			    	//System.out.println();
			    	ImageSite imageSite=new ImageSite(titleString,link, siteLink);
			    	imageSites.add(imageSite);
		    	}


				
		    	System.out.println();
		    }
		    Response Jresponse2= Jsoup.connect(nextPage)
		  	      .ignoreContentType(true)
		            .header("Accept-Encoding", "gzip, deflate")
		            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")  
		            .maxBodySize(0)   
		            .timeout(600000) 
		            .followRedirects(true)
		            .execute();
	  	     Document nextDoc=Jresponse2.parse();
	  	     pages=nextDoc.select("tbody").select("td").select("a[aria-label]");
	  	   for(Element eachPage:pages) {
		    	nextPage="https://www.google.co.jp"+eachPage.attr("href");
		    	System.out.println(nextPage);
		    	String ua = "Mozilla/5.0 ;Windows NT 6.1; "
		    		     + "WOW64; AppleWebKit/537.36 ;KHTML, like Gecko; "
		    		     + "Chrome/39.0.2171.95 Safari/537.36";
		    	
		    	Response jResponse=Jsoup.connect(nextPage).ignoreContentType(true)
		    	        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/46.0.1")
		    	               .referrer("http://www.google.com")
		    	               .timeout(12000)
		    	               .followRedirects(true)
		    	               .execute();
		    	
		    	Document doc2=jResponse.parse();
		    	
		    	Elements blocksElements = doc2.getElementsByClass("rc");
		    	for(Element block:blocksElements) {
		    		String titleString=block.select("h3").text();
		    		siteTitleList.add(titleString);
			    	System.out.println(titleString);
			    	
			    	String link=block.getElementsByClass("s").select("a[class]").attr("href");
			    	try {
				    	int start=link.indexOf("/imgres?imgurl=");
				    	int end=link.indexOf("&imgrefurl");
				    	
				    	link=link.substring(start+15,end);
			    	}catch (Exception e) {
					}
			    	System.out.println(link);
			    	String siteLink=block.select("a[href]").attr("href");
			    	System.out.println(siteLink);
			    	//System.out.println();
			    	ImageSite imageSite=new ImageSite(titleString,link, siteLink);
			    	imageSites.add(imageSite);
		    	}
		    	Elements webSites2=doc2.select("div[data-hveid]");

				
		    	System.out.println();
		    }
		    

		    
			}catch (Exception e) {
				// TODO: handle exception
			}
	}
	
	public ArrayList<String> getSiteTitleList(){
		return siteTitleList;
	}
	public String getSearchedUrl() {
		return searchedUrl;
	}
	public String getRelatedKeyword() {
		return relatedKeywordString;
	}
}