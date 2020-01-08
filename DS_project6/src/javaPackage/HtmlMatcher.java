package javaPackage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Reader;
import java.io.InputStreamReader;

public class HtmlMatcher {
	private String keywords;
	private String content;
	
	public HtmlMatcher(String keywords) {
		this.keywords=keywords;
	}
	
	
	//get all cites links from search
	public ArrayList<String> search() throws IOException{
		String encoding="UTF-8";
		ArrayList<String> sitesLinks=new ArrayList<>();
		try {
			String searchText=keywords;
			org.jsoup.nodes.Document google=Jsoup.connect("https://www.google.com/search?q="
					+URLEncoder.encode(searchText,encoding)).get();
			//System.out.println(google);
			Elements webSitesLinks=google.select("div");

			
			for(Element each: webSitesLinks) {
				if(each.hasAttr("data-hveid")) {
					String siteLink=each.select("a[href]").attr("href");
					//System.out.println(each.select("a[href]").attr("href"));
					if(siteLink.indexOf("http")!=-1) {
						sitesLinks.add(siteLink);
					}
				}
				
			}
			
			if(sitesLinks.isEmpty()) {
				System.out.println("No results found");
				return null;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return sitesLinks;
	}
	
	
	
	//fetch content of each site
	public String fetchContent(String urlStr) throws IOException{
		if(urlStr.indexOf("http")==-1) {
			urlStr="https://"+urlStr;
		}
		String retVal="";
		System.out.println(urlStr);
		try{
			URL url=new URL(urlStr);
		
		URLConnection connection=url.openConnection();
		InputStream inputStream =connection.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		String line=null;
		while((line=br.readLine())!=null) {
			retVal=retVal+line+"\n";
		}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return retVal;
		
	}
	
	
	

}
