package javaPackage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class siteParsing {
	private String siteLink;
	private String imgUrl;
	
	public siteParsing(String siteLink, String imgUrl) {
		this.imgUrl=imgUrl;
		this.siteLink=siteLink;
	}
	
	
	//if imgUrl or siteLink contains pixiv
	public String pixivParsing() throws IOException {
		String retVal="";
		if(imgUrl.indexOf("pixiv.cat/")!=-1) {
			//System.out.println("yesss");
			int start=imgUrl.indexOf("https://pixiv.cat/")+18;
			int end = imgUrl.length()-4;
			String pixivID=imgUrl.substring(start,end);
			String pixivLink="https://www.pixiv.net/artworks/"+pixivID;
			System.out.println(pixivLink);
			URL link=new URL(pixivLink);
			URLConnection conn=link.openConnection();
			InputStream in=conn.getInputStream();
			BufferedReader br= new BufferedReader(new InputStreamReader(in,"UTF-8"));
			
			
			String line=null;
			
			while((line=br.readLine()) !=null) {
				retVal=retVal+line;
			}
		}else {
			if(siteLink.contains("pixiv")!=false) {
				URL link=new URL(this.siteLink);
				URLConnection conn=link.openConnection();
				InputStream in=conn.getInputStream();
				BufferedReader br= new BufferedReader(new InputStreamReader(in,"UTF-8"));
				
				
				String line=null;
				
				while((line=br.readLine()) !=null) {
					retVal=retVal+line;
				}
				
				
			}
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
	
	
	
	//twimg or twitter
	public ArrayList<String> twitterParsing(String siteLink) throws IOException {
		
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
				System.out.println(index);
				String baseString=dElements.get(index).attr("href");
				System.out.println(baseString);
				for(Element each:dElements) {
					if(each.attr("href").toString().indexOf(baseString+"/status")!=-1) {
						if(("https://twitter.com"+each.attr("href")).equals(siteLink)==false) {
							links.add("https://twitter.com"+each.attr("href"));
							System.out.println("https://twitter.com"+each.attr("href"));
						}
					}
				}
				
			}
		
		
		return links;
		
	}
	
	
	

}
