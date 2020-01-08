package javaPackage;

public class ResultSites {
	private String imgUrl;
	private String siteLink;
	private float similarity;
	
	public ResultSites(String imgUrl,String siteLink,float similarity) {
		this.imgUrl=imgUrl;
		this.siteLink=siteLink;
		this.similarity=similarity;
		
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public String getSiteLink() {
		return siteLink;
	}
	public float getSimilarity() {
		return similarity;
	}
}
