package javaPackage;
import java.net.URL;
import java.net.HttpURLConnection;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class urlToFile {
	private String imgUrl;
	private String base;
	private File file;
	private BufferedImage img;
	
	public urlToFile(String imgUrl,String base) {
		this.imgUrl=imgUrl;
		this.base=base;
	}
	
	
	public String getImgPath() throws IOException{
		
		URL url = new URL(imgUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
		img = ImageIO.read(connection.getInputStream());
		file = new File(base+"downloaded.PNG");
		ImageIO.write(img, "PNG", file);
		
		return base+"downloaded.PNG";
	}
	public int getWidth() {
		int width=img.getWidth();
		return width;
	}
	public int getHeight() {
		int height=img.getHeight();
		return height;
	}
	public File getFile() {
		return file;
	}
}
