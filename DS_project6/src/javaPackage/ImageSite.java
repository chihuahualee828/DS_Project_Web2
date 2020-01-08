package javaPackage;

public class ImageSite {
	private String siteTitle;
	private String imgUrl;
	private String siteLink;
	
	public ImageSite(String siteTitle, String imgUrl,String siteLink) {
		this.siteTitle=siteTitle;
		this.imgUrl=imgUrl;
		this.siteLink=siteLink;
		
	}
	
	public String toString(){
		return "["+imgUrl+","+siteLink+"]";
	}
	public String getTitle() {
		return siteTitle;
	}
	public String getUrl() {
		return imgUrl;
	}
	public String getSiteLink() {
		return siteLink;
	}
}
