package javaPackage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 String base="C:/Users/Nick/Pictures/";
		
		String imageA="74649584_p0_master1200.jpg";
		//String imageB="photomania-f35b9c6eb4fc17a95d1e44c973a94a39.jpg";
		String imageB="";	
		String searchUrl="https://www.google.hr/searchbyimage/upload";
		GoogleSearchJava searchImage=new GoogleSearchJava();
		urlToFile urlToFile=new urlToFile("", base);
		
	
		try{
			searchImage.imageSearch(base+imageA);
			compareImage compareImage=new compareImage(base+imageA, "");
			File fileA=new File(base+imageA);
			BufferedImage bufferedImage=ImageIO.read(fileA);
			int width=bufferedImage.getWidth();
			int height=bufferedImage.getHeight();
			compareImage.resize(fileA, base+"compressedA.jpg", width, height);
			for(ImageSite each:searchImage.getAllSites()) {
				//if(each.getUrl().contains("pixiv")) {
					System.out.println(each.getUrl());
					try {
						imageB=each.getUrl();
						urlToFile=new urlToFile(imageB, base);
						String imageBPath=urlToFile.getImgPath();
						System.out.println(imageBPath);
						compareImage=new compareImage(base+imageA, imageBPath);
						compareImage.resize(urlToFile.getFile(), base+"compressedB.jpg",width,height );
						System.out.println(width+"x"+height);
						System.out.println(compareImage.compare());
						System.out.println( );
						}catch (Exception e) {
							// TODO: handle exception
						}
				//}
			}
			*/
			/*for(ImageSite each:searchImage.getAllSites()) {
				try{
					imageB=each.getUrl();
				
					urlToFile=new urlToFile(imageB, base);
					String imageBPath=urlToFile.getImgPath();
					System.out.println(imageBPath);
					compareImage=new compareImage(base+imageA, imageBPath);
					compareImage.resize(urlToFile.getFile(), base+"compressedB.jpg",width,height );
					System.out.println(width+"x"+height);
					System.out.println(compareImage.compare());
					System.out.println( );
				}catch (Exception e) {
					// TODO: handle exception
				}
				
			}*/
		
			//compareImage compareImage=new compareImage(base+imageA, base+imageB);
			//System.out.println(compareImage.compare());
		/*}catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}*/
		
		
		
	
	}

}
