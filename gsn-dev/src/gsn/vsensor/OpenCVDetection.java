package gsn.vsensor;

import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;
import gsn.beans.DataField;
import gsn.beans.StreamElement;
import gsn.beans.VSensorConfig;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;



public class OpenCVDetection extends AbstractVirtualSensor {

	private final static transient Logger      logger         = Logger.getLogger( AbstractVirtualSensor.class );
	
	
	private static String classifierFolder="resources"+ File.separator + "OpenCVClassifiers";
	private String CASCADE_FILE;  //classifier file name
	
	private CvHaarClassifierCascade cascade;
	
	private static final String PARAM_FUNCTIONNAME = "detection";
	private static final String PARAM_SCALEFACTOR = "scale_factor";
	private static final String PARAM_MINNEIGH = "min_neigbors";
	private static final String PARAM_FLAGS = "flag";
	private static final String PARAM_STREAMYES = "stream";
	
	
	
	private String defaultFunctionName = "null";
	private double defaultScale = 1.1;
	private int defaultMinNeigh = 2;
	private int defaultFlag = 0;
	private Boolean defaultStream=true;
	
	
	
	private String functionName=defaultFunctionName;
	private double scale=defaultScale;
	private int neigh=defaultMinNeigh;
	private int flag=defaultFlag;
	private Boolean streamYes=defaultStream;
	
	private final ByteArrayOutputStream   baos = new ByteArrayOutputStream( 16 * 1024 );
	
	
	
	
	private DataField[] outputFormat;
	
	
	/* (non-Javadoc)
	 * @see gsn.vsensor.AbstractVirtualSensor#initialize()
	 */
	@Override
	public boolean initialize() {
		
		
		VSensorConfig vsensor = getVirtualSensorConfiguration();
		
		//get parameters from xml file
		TreeMap < String , String > params = vsensor.getMainClassInitialParams( );

		
		String readString=null;
		
		
		//parse function name
		readString = params.get(PARAM_FUNCTIONNAME);
         if(readString != null && !readString.trim().equals("")){
         	functionName = readString;
         }
		         
        //parse scale
		readString = params.get(PARAM_SCALEFACTOR);
	     if(readString != null && !readString.trim().equals("")){
	    	 scale=Double.parseDouble(readString);
	     }
         
        //parse neigh
 		readString = params.get(PARAM_MINNEIGH);
          if(readString != null && !readString.trim().equals("")){
        	  neigh=Integer.parseInt(readString);
          }
          
        //parse flag
  		readString = params.get(PARAM_FLAGS);
       	if(readString != null && !readString.trim().equals("")){
    	   flag=Integer.parseInt(readString);
    	}
           
         //parse streamYes
         readString = params.get(PARAM_STREAMYES);
         if(readString != null && !readString.trim().equals("")){
        	 streamYes = Boolean.parseBoolean(readString);
         }
         
         //show in logger
         if(logger.isDebugEnabled()){
        	 logger.debug(PARAM_FUNCTIONNAME +" configured to: " + functionName);
        	 logger.debug(PARAM_SCALEFACTOR +" configured to: " + scale);
        	 logger.debug(PARAM_MINNEIGH +" configured to: " + neigh);
        	 logger.debug(PARAM_FLAGS +" configured to: " + flag);
        	 logger.debug(PARAM_STREAMYES +" configured to: " + streamYes);
         }
         
         outputFormat = new DataField[] {  
  	    		new DataField("image", "binary:image/jpeg")
  	    	};
         
         CASCADE_FILE=null;
         
         classifierFolder += File.separator;
         
         if (functionName.equalsIgnoreCase("face")){
        	 CASCADE_FILE = classifierFolder + "haarcascade_frontalface_alt.xml";
         }
         else if(functionName.equalsIgnoreCase("car")){
        	 CASCADE_FILE = classifierFolder + "cars3.xml";
         }
         else if(functionName.equalsIgnoreCase("upperbody")){
        	 CASCADE_FILE = classifierFolder + "haarcascade_upperbody.xml";
         }
         else if(functionName.equalsIgnoreCase("lowerbody")){
        	 CASCADE_FILE = classifierFolder + "haarcascade_lowerbody.xml";
         }
         else if(functionName.equalsIgnoreCase("fullbody")){
        	 CASCADE_FILE = classifierFolder + "haarcascade_fullbody.xml";
         }
         else if(functionName.equalsIgnoreCase("mouth")){
        	 CASCADE_FILE = classifierFolder + "Mouth.xml";
         }
         else if(functionName.equalsIgnoreCase("eyes")){
        	 CASCADE_FILE = classifierFolder + "parojos.xml";
         }
         else if(functionName.equalsIgnoreCase("eyes")){
        	 CASCADE_FILE = classifierFolder + "Nariz.xml";
         }
         else if(functionName.equalsIgnoreCase("hand1")){
        	 CASCADE_FILE = classifierFolder + "1256617233-1-haarcascade_hand.xml";
         }
         else if(functionName.equalsIgnoreCase("hand2")){
        	 CASCADE_FILE = classifierFolder + "1256617233-2-haarcascade-hand.xml";
         }
         
         if (CASCADE_FILE!=null){
        	 cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
         }
         
		return true;
	}
	

	/* (non-Javadoc)
	 * @see gsn.vsensor.AbstractVirtualSensor#dataAvailable(java.lang.String, gsn.beans.StreamElement)
	 */
	@Override
	public void dataAvailable(String inputStreamName, StreamElement streamElement) {
		
		boolean error=false;
		Serializable[] output=null;
		
		if (streamElement==null){
			error=true;
			logger.debug("Empty StreamElement");
		}
		else{
			output = new Serializable[this.outputFormat.length];
	        output[0]=streamElement.getData(this.outputFormat[0].getName());
	        
	
	        BufferedImage bufferedImage=null;
	        try {
				bufferedImage = ImageIO.read(new ByteArrayInputStream((byte[]) output[0]));
			} catch (IOException e) {
				//e.printStackTrace();
				//logger.warn(e.getMessage( ) , e);
				logger.debug("Unable to decode image[1]: I'll just drop it...sorry");
				error=true;
			}
	        if (cascade!=null){
	        	//perform detection here
	        	IplImage grayImage;
	    	    CvMemStorage storage;

	    	    IplImage grabbedImage = IplImage.createFrom(bufferedImage);
	    	    grayImage = IplImage.create(grabbedImage.width(),grabbedImage.height(), IPL_DEPTH_8U, 1);
				
				//convert the original image to grayscale.
				cvCvtColor(grabbedImage, grayImage, CV_BGR2GRAY);
				
				storage = CvMemStorage.create();
				

				//detect elements
				CvSeq elems = cvHaarDetectObjects(grayImage, cascade, storage, scale, neigh, flag);
				

				if (elems.total()>0){
					for (int i = 0; i < elems.total(); i++) {
						CvRect r = new CvRect(cvGetSeqElem(elems, i));
						cvRectangle(grabbedImage, cvPoint(r.x(), r.y()),cvPoint(r.x() + r.width(), r.y() + r.height()),CvScalar.YELLOW, 1, CV_AA, 0);
					}
					bufferedImage=grabbedImage.getBufferedImage();
				}
				else{
					if (streamYes==false){
						//force to null!!!
						bufferedImage=null;
						output=null;
					}
				}
	        }
	        
	        if (bufferedImage!=null){
	        	try {
	        		baos.reset( );
	        		ImageIO.write(bufferedImage, "jpeg", baos);
	        		output[0]= baos.toByteArray( ) ;
	        		baos.close();
	        	} catch ( Exception e ) {
	   	         //logger.warn( e.getMessage( ) , e );
	   	         logger.debug("Unable to decode image[2]: I'll just drop it...sorry");
	   	         error=true;
	        	}
	        }
        }
	       
        
        if ( output!=null && error==false){
	        //build StreamElement
			StreamElement se = new StreamElement(getOutputFormat(), output);
			dataProduced(se);
        }
        else{
        	//sistema le cose qui....
        	logger.debug("forward nothing");
        	output=null;
        	return;
        }
        
	} //dataAvailable
	
	public DataField[] getOutputFormat() {
    	return outputFormat;
    }
	
	
	/* (non-Javadoc)
	 * @see gsn.vsensor.AbstractVirtualSensor#dispose()
	 */
	@Override
	public void dispose() {
	}
	
}
