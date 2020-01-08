package javaPackage;

import java.awt.EventQueue;
import java.util.Iterator;
import java.util.HashMap;
import java.net.URI;
import java.awt.Desktop;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.net.ssl.ExtendedSSLSession;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilterReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;

import java.awt.Panel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.event.ChangeListener;

import org.apache.http.impl.cookie.PublicSuffixDomainFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import javax.swing.event.ChangeEvent;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.codec.binary.StringUtils;
import javax.swing.SwingConstants;
public class Frame1 {
	private String[] sitekeywords = {"pixiv","twitter.com","instagram"};
	private String[] imgkeywords = {"twimg","pximg"};
	private JFrame frame;
	private String imagePath="";
	private String imageName="";
	private LinkedList<ResultSites> resultSitesList=new LinkedList<>();
	private List<Score> scoreList=new ArrayList<Frame1.Score>();
	private String firstPageUrl="";
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static boolean stringContainsItemFromList(String inpputStr, String[] items) {
		return Arrays.stream(items).parallel().anyMatch(inpputStr::contains);
	}
	
	public LinkedList<ResultSites> getResultSitesList(){
		sortResultSiteList();
		return resultSitesList;
	}
	public class Score {
	    String titleString;
	    double score;
	    public Score(String titleString,double score) {
	    	this.titleString=titleString;
	    	this.score=score;
	    }
	}
	public void sortScoreList() {
		scoreList.sort(new Comparator<Score>() {
		@Override
			public int compare(Score o1,Score o2) {
				if(o1.score<o2.score) {
					return 1;
				}else {
					return -1;
				}
			}
			
		});
	}
	
	public void sortResultSiteList() {
		resultSitesList.sort(new Comparator<ResultSites>() {
		@Override
			public int compare(ResultSites o1,ResultSites o2) {
				if(o1.getSimilarity()<o2.getSimilarity()) {
					return 1;
				}else {
					return -1;
				}
			}
			
		});
	}
	
	
	
	/**
	 * Create the application.
	 */
	public Frame1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 394, 558);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				
				
				JProgressBar progressBar = new JProgressBar();
				progressBar.setBounds(14, 469, 348, 29);
				frame.getContentPane().add(progressBar);
				
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
					
					int progessTotal=searchImage.getAllSites().size();//progressBarScale
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
					String originalTweet="";
					for(ResultSites each:resultSitesList) {
						if(each.getImgUrl().indexOf("twimg")!=-1 && each.getSiteLink().indexOf("twitter")!=-1) {
							if(each.getSiteLink().indexOf("status")!=-1) {
								try{
									originalTweet=pixivParse.twitterParsing(each.getSiteLink());
									System.out.println(originalTweet);
									break;
								}catch (Exception e) {
									// TODO: handle exception
									System.out.println(e);
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
											break;
										}catch (Exception e) {
											// TODO: handle exception
											System.out.println(e);
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
								if(each.getSiteLink().indexOf("status")!=-1) {
									try{
										originalTweet=pixivParse.twitterParsing(each.getSiteLink());
										System.out.println(originalTweet);
										break;
									}catch (Exception e) {
										// TODO: handle exception
										System.out.println(e);
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
												if(originalTweet.indexOf("status")==-1) {
													continue;
												}else {
													break;
												}
											}catch (Exception e) {
												// TODO: handle exception
												System.out.println(e);
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
//							  .ignoreContentType(true)
//							  .timeout(12000)
//							  .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36") // missing
							  .get();
					
					Document document2=Jsoup.connect(mainTwitterLink)
//							  .ignoreContentType(true)
//							  .timeout(12000)
//							  .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36")
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
							document=Jsoup.connect(mainTwitterLink).get();
							System.out.println(e);
						}
					}
					
					
					
					
					
					
					
					System.out.println(twitterTitle);
					String postTextString="";
					boolean success=false;
					while (!success) {
						try{
							postTextString=document.select("p.js-tweet-text.tweet-text").first().text();
							postTextString=postTextString.substring(0, postTextString.indexOf("pic"));
							success=true;
						}catch (Exception e) {
							// TODO: handle exception
							System.out.println(originalTweet);
							document=Jsoup.connect(originalTweet).get();
							System.out.println(e);
						}
					}
					
					System.out.println(postTextString);
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
					System.out.println(twitterTitle+"+"+postTextString);
					GoogleQuery googleQuery=new GoogleQuery(twitterTitle+"+"+postTextString);
					HashMap<String, String> query = googleQuery.query();

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
					System.out.println(twitterTitle+"+"+postTextString);
					GoogleQuery googleQuery2=new GoogleQuery(twitterTitle+"+"+postTextString);
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

//					for(Map.Entry<String, String> entry : query.entrySet()) {
//						try{
//							String titleString=entry.getKey();
//							String linkString=entry.getValue();
//						
//							if(stringContainsItemFromList(linkString, urlFilter)) {
//								try{
//								topWebsitesLinksList.put(titleString,linkString);
//								query.remove(titleString);}
//								catch (Exception e) {
//									// TODO: handle exception
//									System.out.println(e);
//								}
//							}
//						}catch (Exception e) {
//							// TODO: handle exception
//							System.out.println(e);
//						}
//					}
					
					//targeted websites dont need to be scored, move them straight to the topweblist
					Iterator<Map.Entry<String,String>> iter = query.entrySet().iterator();
					while (iter.hasNext()) {
					    Map.Entry<String,String> entry = iter.next();
					    if(stringContainsItemFromList(entry.getValue(), topWebFilter)){
					    	topWebsitesLinksList.put(entry.getKey(),entry.getValue());
					        iter.remove();
					    }
					}
					
					System.out.println(twitterTitle);
					
					scoreList=new ArrayList<Frame1.Score>();
//					HashMap<String, Score> scoreMap=new HashMap<String, Score>();
					long start = System.nanoTime();  
					for(Map.Entry<String, String> entry : query.entrySet()) {
						String title=entry.getKey();
						String link=entry.getValue();
						System.out.println(title);
					    System.out.println(link);
					    WebPage rootPage = new WebPage(link, title);	
					    WebTree tree = new WebTree(rootPage);
					    try {
							Document firstLayer=Jsoup.connect(rootPage.url).get();
	//						for(Element)
	//						if(stringContainsItemFromList(each.getUrl(), urlFilter)) {
							ArrayList<String> firstLayerLinks=new ArrayList<String>();
							org.jsoup.select.Elements firstLayerElements=firstLayer.select("a[href]");
							for(Element each:firstLayerElements ) {
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
	//							String name="Child"+i;
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
					long elapsedTime = System.nanoTime() - start;
					System.out.println(elapsedTime);
					
					
					System.out.println();
					System.out.println();
					
					//print out searched result
					for(Map.Entry<String, String> entry : topWebsitesLinksList.entrySet()) {
						String title=entry.getKey();
						String link=entry.getValue();
						System.out.println(title);
					    System.out.println(link);
					    
					}
					
					sortScoreList();
					for(Score each: scoreList) {
					
						String link=query.get(each.titleString);
						System.out.println(each.titleString);
					    System.out.println(link);
					}
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
							if(arg0.getSource()==btnNewButton) {
								progressBar.setVisible(true);
								
								try {
									Thread.sleep(50);
									progressBar.paintImmediately(0,0,348,29);
									progressBar.setValue((int)(i*100));
									progressBar.setStringPainted(true);
									progressBar.setString("Matching..."+(int)(i*100)+"%");
									i=i+(1.0/progessTotal);
									if(i>=0.98 && i<1) {
										i=1;
									}
									//System.out.println(i);
								}catch (Exception e) {
									// TODO: handle exception
								}
							}
						
					
				
					progressBar.setString("Done!");
					
					//resultFrame();
				}catch (Exception e) {
					// TODO: handle exception
					JLabel lblNewLabel2 = new JLabel("                Failed...");
					lblNewLabel2.setBounds(98, 472, 182, 26);
					frame.getContentPane().add(lblNewLabel2);
					System.out.println(e);
				}
				
				
			}
		});
		btnNewButton.setEnabled(false);
		btnNewButton.setForeground(Color.RED);
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		btnNewButton.setFont(new Font("Bell MT", Font.BOLD | Font.ITALIC, 17));
		btnNewButton.setBounds(116, 388, 139, 50);
		frame.getContentPane().add(btnNewButton);
		
		//Drag and drop image-----
		JLabel drag_container = new JLabel("                                Drag an image");
		drag_container.setBounds(14, 13, 348, 374);
		frame.getContentPane().add(drag_container);
		TransferHandler tHandler=new TransferHandler(){
			
			@Override
			public boolean canImport(JComponent arg0, DataFlavor[] arg1) {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public boolean importData(JComponent arg0, Transferable arg1) {
				// TODO Auto-generated method stub
				try {
					List<File> files=(List<File>) arg1.getTransferData(DataFlavor.javaFileListFlavor);
					if(files.size()==1) {
						File f=files.get(0);
						ImageIcon imageIcon=new ImageIcon(f.getPath());
						Image image=imageIcon.getImage();
						Image newimg=image.getScaledInstance(348, 374, Image.SCALE_SMOOTH);
						drag_container.setIcon(new ImageIcon(newimg));
						imagePath=f.getPath();
						imageName=f.getName();
						System.out.println(imagePath);
					}
					
					if(imagePath!="") {
						btnNewButton.setEnabled(true);
					}
				}
					 catch (Exception e) {
						 System.out.println(e);
				}
				return true;
				}
			};
			
			drag_container.setTransferHandler(tHandler);
	}
			
			
			
	
	// pop up frame after clicking the button
	public void resultFrame() {
		
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	JPanel contentPane=new JPanel();
            	contentPane.setLayout(null);
            	JFrame frame=new JFrame("Test");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setBounds(100, 100, 1000, 1500);
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
				
				LinkedList<ResultSites> resultSitesList=getResultSitesList();
				String pixivInfo="";
				String twitterInfo="";
				for(int i=0;i<resultSitesList.size();i++) {
					
					String siteLink=resultSitesList.get(i).getSiteLink();
					String imgUrl=resultSitesList.get(i).getImgUrl();
					
					siteParsing siteParsing=new siteParsing(siteLink, imgUrl);
					
					JLabel label1=new JLabel("Rank"+i+": "+resultSitesList.get(i).getSimilarity());
					label1.setBounds(10,10+150*i,727,37);
					
					label1.setHorizontalAlignment(SwingConstants.LEFT);
					JLabel label2=new JLabel(resultSitesList.get(i).getImgUrl());
					label2.setBounds(10,30+150*i,727,37);
					label2.setHorizontalAlignment(SwingConstants.LEFT);
					
					
					JLabel label3=new JLabel(siteLink);
					label3.setBounds(10,50+100*i,727,37);
					label3.setHorizontalAlignment(SwingConstants.LEFT);
					//System.out.println(siteLink+", "+imgUrl);
					
					
					
					try{
						//save illustrator info 
//						if(imgUrl.indexOf("pixiv.cat/")!=-1 ||siteLink.contains("pixiv.net/artworks")!=false) {
//							pixivInfo=siteParsing.pixivParsing().replace("\n", ", ");}
//						else if(siteLink.indexOf("twitter")!=-1 && imgUrl.indexOf("twimg")!=-1) {
//							if(siteLink.indexOf("status")!=-1) {
//								twitterInfo=siteParsing.twitterParsing(siteLink).get(0);
//							}
//						}
						//System.out.println(pixivInfo);
						}catch (Exception e) {
						// TODO: handle exception
						System.out.println(e);
					}
					
					contentPane.add(label1);
					contentPane.add(label2);
					//contentPane.add(label3);
					
					JLabel label4=new JLabel();
					label4.setBounds(780,10+150*i,120,120);
					try{
						URL url = new URL(imgUrl);
					
						Image image = ImageIO.read(url);
						Image dImage=image.getScaledInstance(120, 120,
						        Image.SCALE_SMOOTH);
						label4.setIcon(new ImageIcon(dImage));
					}catch (Exception e) {
						// TODO: handle exception
					}
					
					contentPane.add(label4);
	
					
					/*
					contentPane.add(new JLabel("Rank"+i+": "+resultSitesList.get(i).getSimilarity(),SwingConstants.LEFT)).setBounds(10,10+60*i,727,37);
					contentPane.add(new JLabel(resultSitesList.get(i).getImgUrl(),SwingConstants.LEFT)).setBounds(10,10+60*i,727,37);
					contentPane.add(new JLabel(siteLink,SwingConstants.LEFT)).setBounds(10,10+80*i,727,37);
					*/
					JButton button=new JButton();
					button.setText("<HTML><FONT color=\"#000099\"><U>"+siteLink+"</U></FONT></HTML>");
					button.setHorizontalAlignment(SwingConstants.LEFT);
					button.setBounds(0,60+150*i,727,15);
					button.setBorderPainted(false);
				    button.setOpaque(false);
				    button.setBackground(Color.WHITE);
				    //button.setToolTipText(siteLink);
					try{
						button.addActionListener(new ActionListener() {
					
						
						URI uri=new URI(siteLink);
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							try{
								open(uri);
							}catch (Exception ex) {
								// TODO: handle exception
								System.out.println(ex);
							}
							
						}
					});
					}catch (Exception e) {
						// TODO: handle exception
						System.out.println(e);
					};
					
					contentPane.add(button);
					
					
					//System.out.println(i+", "+resultSitesList.get(i));
					
				}
				frame.getContentPane().add(BorderLayout.CENTER,contentPane);
                frame.setVisible(true);
                frame.setResizable(false);
                System.out.println(pixivInfo+"dfsdfswe");
                System.out.println(twitterInfo+"dsfsdfdssd");
                if(pixivInfo!="") {
                		pixivInfo=pixivInfo.split(", ")[1]+" "+pixivInfo.split(", ")[2];
                }
                System.out.println(pixivInfo);
                String searchText=pixivInfo;
                if (searchText!="") {
	                try {
						HtmlMatcher htmlMatcher=new HtmlMatcher(searchText);
						ArrayList<String> siteLists=htmlMatcher.search();
						if(siteLists!=null) {
							for(String eachLink : siteLists) {
								try {
									//htmlMatcher.fetchContent(eachLink);
									System.out.println(eachLink);
								}
								catch (Exception e) {
									System.out.println(e);
									// TODO: handle exception
								}
							}
					}
					}catch (Exception e) {
						e.printStackTrace();
					}
                }
				
            }
        
        });
	}
	
	private static void open(URI uri) throws IOException{
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	  }

}
