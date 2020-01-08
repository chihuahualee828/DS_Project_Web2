package javaPackage;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


import org.apache.http.impl.client.AbstractAuthenticationHandler;

import java.io.IOException;
import java.awt.image.DataBuffer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;
public class compareImage extends GoogleSearchJava {
	private String fileA;
	private String fileB;
	private String base;
	public compareImage(String fileA, String fileB) {
		this.fileA=fileA;
		this.fileB=fileB;
		this.base=fileA.replace(new File(fileA).getName(),"");
	}
	
	
	public File resize(File inputFile1,
			String outputImagePath,int scaledWidth, int scaledHeight)
			throws IOException {
		// reads input image
		//String inputPath=inputFile1.getAbsolutePath();
		 //if(inputPath.indexOf(".jpg")!=-1) {
			 
			// BufferedImage bufferedImage = ImageIO.read(new File(inputPath));

			 //inputPath=inputPath.replace(".jpg", ".png");
			 //ImageIO.write(bufferedImage, "png", new File(inputPath));
			 
			 //inputFile1=new File(inputPath);
			 //System.out.println(inputPath);
		 //}
		BufferedImage inputImage1 = ImageIO.read(inputFile1);
		//System.out.println(inputImage1);
		// creates output image
		BufferedImage outputImage1 = new BufferedImage(scaledWidth,
				scaledHeight, BufferedImage.TYPE_BYTE_BINARY);
	

		// scales the input image to the output image
		Graphics2D g2d1 = outputImage1.createGraphics();
		
		g2d1.drawImage(inputImage1, 0, 0, scaledWidth, scaledHeight, null);
		g2d1.dispose();
		

		// extracts extension of output file
		 String formatName = outputImagePath.substring(outputImagePath
	                .lastIndexOf(".") + 1);
		 
	        // writes to output file
	     ImageIO.write(outputImage1, formatName, new File(outputImagePath));
		
	     File compressedImage=new File(outputImage1+formatName);
		return compressedImage;
	}

	/**
	 * Resizes an image by a percentage of original size (proportional).
	 * @param inputImagePath Path of the original image
	 * @param outputImagePath Path to save the resized image
	 * @param percent a double number specifies percentage of the output image
	 * over the input image.
	 * @throws IOException
	 */


	
	
	
	
	public float compare() {
	    float percentage = 0;
	    File file1=new File(fileA);    
	    File file2=new File(fileB);
	    File compressedA=null;
	    File compressedB=null;
	    int sizeA=0;
	    int sizeB=0;
	    DataBuffer dbA =null;
	    DataBuffer dbB =null;
	    /*try {
	    	resize(file1, "C:/Users/Nick/Pictures/compressedA.jpg", 1000, 1000);
	    	resize(file2, "C:/Users/Nick/Pictures/compressedB.jpg", 1000, 1000);
	    	
	    }catch (Exception e) {
				// TODO: handle exception
			}
		*/
	    try{
	    	compressedA=new File(base+"compressedA.jpg");
	    	BufferedImage biA = ImageIO.read(compressedA);
	    	//System.out.println(biA);
	         dbA = biA.getData().getDataBuffer();
	         sizeA = dbA.getSize();
		     //System.out.println(sizeA);
	    }catch (Exception e) {
			// TODO: handle exception
	    	System.out.println("Failed to read A");
		}
	        
	   
	    try{ 
	    	compressedB=new File(base+"compressedB.jpg");
	    	BufferedImage biB = ImageIO.read(compressedB);
	    	//System.out.println(biB);
	         dbB = biB.getData().getDataBuffer();
	         
	        sizeB = dbB.getSize();
	        //System.out.println(sizeB);
	    }catch (Exception e) {
		// TODO: handle exception
	    	System.out.println("Failed to read B");
	    }
	        int count = 0;
	        // compare data-buffer objects //
	        if (sizeA == sizeB) {
//	        	int AblackPixel=0;
//	        	int BBlackPixel=0;
//	        	int AWhitePixel=0;
	        	//int imageAFirstPixel=dbA.getElem(1000);
            	//int imageBFirstPixel=dbB.getElem(1000);
            	//int delta=imageAFirstPixel-imageBFirstPixel;
	        	for (int i = 0; i < sizeA; i++) {
	        		if(dbA.getElem(i)<127) {
	        			dbA.setElem(i, 0);
	        		}else {
	        			dbA.setElem(i, 255);
	        		}
	        	}
	        	for (int i = 0; i < sizeB; i++) {
	        		if(dbB.getElem(i)<127) {
	        			dbB.setElem(i, 0);
	        		}else {
	        			dbB.setElem(i, 255);
	        		}
	        	}
            	
	            for (int i = 0; i < sizeA; i++) {
	            	int imageAPixel=dbA.getElem(i);
	            	int imageBPixel=dbB.getElem(i);
	            	
	            	
	                if (imageAPixel==imageBPixel ) {
	                    count = count + 1;
	                }else {
	                	count=count-1;
	                }
	                
	            	//System.out.println(dbA.getElem(i)-dbB.getElem(i));
	            	
	            	/*if (dbA.getElem(i)==dbB.getElem(i)+delta) {
	                 	
	                    count = count + 1;
	                }*/
	
	            }
	            //System.out.println(count);
	            //System.out.println(AblackPixel);
	            
	            percentage = ((float)count/dbA.getSize())*100;
	            /*if(percentage>100) {
	            	percentage=100;
	            }else if(percentage<0) {
	            	percentage=0;
	            }*/
	        } else {
	            System.out.println("Both the images are not of same size");
	        }
	
	    
	    return percentage;
	}
	
}

	
