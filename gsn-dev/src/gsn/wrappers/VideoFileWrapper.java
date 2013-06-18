package gsn.wrappers;

import static com.googlecode.javacv.cpp.opencv_highgui.*;

import gsn.beans.DataField;
import gsn.beans.AddressBean;
import gsn.beans.StreamElement;
import gsn.wrappers.cameras.usb.ImageWrapper;
import java.io.*;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;


/*
* This wrapper plays a video file
* */

public class VideoFileWrapper extends AbstractWrapper {
	
    private static final transient Logger logger = Logger.getLogger(VideoFileWrapper.class);
    private int threadCounter;
    
    
    private static final String PARAM_DIRECTORY = "directory";
    private static final String PARAM_FILENAME = "file";
    private static final String PARAM_RATE = "rate";
    private static final String PARAM_LOOP = "loop";
    private static final String PARAM_LIVEVIEW = "liveview";
    
    private final String DEFAULT_DIRECTORY = "resources/Videos\\";
    private final String DEFAULT_FILE = "cars1.avi";
    private final long DEFAULT_RATE = 1000;
    private final Boolean DEFAULT_LOOP = true;
    private final Boolean DEFAULT_LIVEVIEW = true;
    
    
    
    private String videoDirectory=DEFAULT_DIRECTORY;
    private String fileName=DEFAULT_FILE;
    private long rate=DEFAULT_RATE;
    private Boolean loop=DEFAULT_LOOP;
    private Boolean liveview=DEFAULT_LIVEVIEW;
    
    private File filePointer=null;
    
    private CvCapture capture=null;
    private CanvasFrame canvas=null;
    
    private final ByteArrayOutputStream   baos = new ByteArrayOutputStream( 16 * 1024 );
    
    private DataField[] collection = new DataField[] {
    									new DataField ("image", "binary:image/jpeg", "jpeg image")
    												};

    

    public boolean initialize() {
    	
        setName(getWrapperName() + "-" + (++threadCounter));
        
        AddressBean addressBean = getActiveAddressBean();

        String readString=null;
		
		
		//parse videoDirectory
		readString = addressBean.getPredicateValue(PARAM_DIRECTORY);
        if(readString != null && !readString.trim().equals("")){
        	 videoDirectory = readString;
         }
        
        //parse fileName
		readString = addressBean.getPredicateValue(PARAM_FILENAME);
	      if(readString != null && !readString.trim().equals("")){
	    	  fileName = readString;
	       }
	      
	    //parse rate
		readString = addressBean.getPredicateValue(PARAM_RATE);
	      if(readString != null && !readString.trim().equals("")){
	    	  rate = Long.parseLong(readString);
	       }
		      
	    //parse loop
		readString = addressBean.getPredicateValue(PARAM_LOOP);
	      if(readString != null && !readString.trim().equals("")){
	    	  loop = Boolean.parseBoolean(readString);
	       }
	      
	    //parse liveview
		readString = addressBean.getPredicateValue(PARAM_LIVEVIEW);
	      if(readString != null && !readString.trim().equals("")){
	    	  liveview = Boolean.parseBoolean(readString);
	       }

         //show in logger
         if(logger.isDebugEnabled()){
        	 logger.debug(PARAM_DIRECTORY +" configured to: " + videoDirectory);
        	 logger.debug(PARAM_FILENAME +" configured to: " + fileName);
        	 logger.debug(PARAM_RATE +" configured to: " + rate);
        	 logger.debug(PARAM_LOOP +" configured to: " + loop);
        	 logger.debug(PARAM_LIVEVIEW +" configured to: " + liveview);
         }

         videoDirectory=videoDirectory.replaceAll("\\\\|/", "\\"+File.separator);
         if (String.valueOf(videoDirectory.charAt(videoDirectory.length()-1)).compareTo(File.separator)!=0){
        	 videoDirectory+=File.separator;
         }
         
         filePointer=new File(videoDirectory+fileName);
         if (!filePointer.exists()){
        	 try {
 				logger.warn(filePointer.getCanonicalFile() + " does not exist!!!");
 			} catch (IOException e) {
 				logger.warn("problems in the initialization of the video feed!");
 			}
        	 return false;
         }
         
        return initVideoCapture();
    }
    
    private boolean initVideoCapture(){
    	

    	capture = cvCreateFileCapture(filePointer.getAbsolutePath());
    	

    	IplImage currentImage;
        if (liveview==true){
        	canvas = new CanvasFrame(fileName);
    		
    		currentImage = cvQueryFrame( capture );
    	    
    	    int width=currentImage.width();
    	    int height=currentImage.height();
    	    
    	    canvas.setCanvasSize(width, height);
        }

        return true;
    }

    public void run() {

    	IplImage currentImage;
    	while (true){
    		
    		currentImage = cvQueryFrame( capture );
    		
    		if (currentImage==null){
    			//video is over...
    			if (this.loop==true){
    				//restart video
    				System.out.println("done: we should reset the stream here");
    				
    			    cvReleaseCapture(capture);
    			    if (this.liveview && canvas.isVisible()){
    			    	canvas.dispose();
    			    }
    			    //reset
    			    initVideoCapture();
    			    currentImage = cvQueryFrame( capture );
    	    	}
    			else{
    				//not loopback: exit the loop
    				break;
    			}
    			
    		}
			if (this.liveview && canvas.isVisible()) {
				canvas.showImage(currentImage);  
	        }
			PostData(new ImageWrapper(currentImage.getBufferedImage()));
	    	
			try {
				Thread.sleep(rate);
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
    	}
    }
    
    
    public DataField[] getOutputFormat() {
    	return collection;
    }

    public void dispose() {
        threadCounter--;
    }

    public String getWrapperName() {
        return "VideoFileWrapper";
    }
    


    
    private boolean PostData(ImageWrapper reading) {
        boolean success = true;
        Serializable[] output = new Serializable[this.getOutputFormat().length];
		
        
        baos.reset( );
		 if ( reading != null ) {
			 try {
				ImageIO.write(reading.getIm(), "png", baos);
			} catch (IOException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
			 
			 output[0]= baos.toByteArray( ) ;
			 try {
				baos.close();
			} catch (IOException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
		}

		//build StreamElement
		StreamElement se = new StreamElement(getOutputFormat(), output);
		
        if (success) {
            success = postStreamElement(se);
        }

        return success;		
    }
}
