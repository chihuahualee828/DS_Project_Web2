package javaPackage;
import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.URL;

import java.net.URLConnection;

import java.util.HashMap;



import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;



public class GoogleQuery 

{

	public String searchKeyword;

	public String url;

	public String content;

	public GoogleQuery(String searchKeyword)

	{

		this.searchKeyword = searchKeyword;

		this.url = "http://www.google.com/search?q="+searchKeyword+"&oe=utf8&num=10";

	}

	

	private String fetchContent() throws IOException

	{
		String retVal = "";
		if(url.contains(" ")) {
		    url = url.replace(" ", "%20");
		}
		//fetcher-cdn.nullmu.com/social/4af130e7d6fce08dbfe7e729ff7f0219.jpg%3Fu%3Dhttps%253A%252F%252Fpbs.twimg.com%252Fmedia%252FEIe8y-jVUAARcA9.jpg

		
		URL u = new URL(url);

		URLConnection conn = u.openConnection();

		conn.setRequestProperty("User-agent", "Chrome/7.0.517.44");

		InputStream in = conn.getInputStream();

		InputStreamReader inReader = new InputStreamReader(in,"utf-8");

		BufferedReader bufReader = new BufferedReader(inReader);
		String line = null;

		while((line=bufReader.readLine())!=null)
		{
			retVal += line;

		}
		return retVal;
	}
	public HashMap<String, String> query() throws IOException

	{
		System.out.println(url);
		if(content==null)

		{

			content= fetchContent();

		}
		

		HashMap<String, String> retVal = new HashMap<String, String>();
		
		Document doc = Jsoup.parse(content);
		
		//System.out.println(doc);
		Elements lis = doc.select("div");
		lis = lis.select("div[class=ZINbbc xpd O9g5cc uUPGi]");
		
//		System.out.println(lis.size());
//		System.out.println(lis);
		
		
		for(Element li : lis)
		{
			try 

			{
				String title = li.select("div[class=BNeawe vvjwJb AP7Wnd]").text();
				String citeUrl = li.select("a").get(0).attr("href");
				
				if(citeUrl.contains("%25")) {
					citeUrl = citeUrl.replace("%25", "%");
				}
				if(citeUrl.contains("%2F")) {
					citeUrl = citeUrl.replace("%2F", "/");
				}
				if(citeUrl.contains("%26")) {
					citeUrl = citeUrl.replace("%26", "&");
				}
				if(citeUrl.contains("%3D")) {
					citeUrl = citeUrl.replace("%3D", "=");
				}
				if(citeUrl.contains("%3F")) {
					citeUrl = citeUrl.replace("%3F", "?");
				}
//				for(int i = 0 ; i < block.size(); i++)
//					System.out.println(block.get(i).text());
				
//				System.out.println(block.get(1).text());
//				System.out.println(block.get(2).text());
				
//				String title = block.get(1).text();
//				String citeUrl = block.get(2).text();
				
//				System.out.println(title+" "+citeUrl);
				try{
					citeUrl=citeUrl.substring(7,citeUrl.indexOf("&sa=U"));
				
					System.out.println(title);
					System.out.println(citeUrl);
					retVal.put(title, citeUrl);
				}catch (Exception e) {
					// TODO: handle exception
				}

				

			} catch (IndexOutOfBoundsException e) {

				e.printStackTrace();

			}

			

		}

		

		return retVal;

	}

	

	

}