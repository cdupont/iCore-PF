package gsn.vsensor;

import gsn.beans.DataField;
import gsn.beans.StreamElement;
import gsn.beans.VSensorConfig;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.TreeMap;
import javaanpr.imageanalysis.CarSnapshot;
import javaanpr.intelligence.Intelligence;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;



public class ImageRecognitionVS extends AbstractVirtualSensor {

	private final static transient Logger      logger         = Logger.getLogger( AbstractVirtualSensor.class );
	
	
	private static final String PARAM_FUNCTIONNAME = "recognition";;
	
	private String functionName;
	private String defaultFunctionName = "null";
	
	private final ByteArrayOutputStream   baos = new ByteArrayOutputStream( 16 * 1024 );
	
	
	private Intelligence systemLogic=null;
	
	private DataField[] outputFormat;
	
	
	/* (non-Javadoc)
	 * @see gsn.vsensor.AbstractVirtualSensor#initialize()
	 */
	@Override
	public boolean initialize() {
		
		
		VSensorConfig vsensor = getVirtualSensorConfiguration();
		
		//get parameters from xml file
		TreeMap < String , String > params = vsensor.getMainClassInitialParams( );

		
         //parse function name
         functionName = params.get(PARAM_FUNCTIONNAME);
         if(functionName == null || functionName.trim().equals("")){
         	functionName = defaultFunctionName;
         }

         //show in logger
         if(logger.isDebugEnabled()){
        	 logger.debug("Recognition configured to: " + functionName);
         }
         
         
         if (functionName.equalsIgnoreCase("null")){
        	 outputFormat = new DataField[] {  
     	    		new DataField("image", "binary:image/jpeg")
     	    	};
         }
         else if (functionName.equalsIgnoreCase("qr") || functionName.equalsIgnoreCase("plate")){
        	 outputFormat = new DataField[] {  
        	    		new DataField("image", "binary:image/jpeg"),
        	    		new DataField("decoded", "binary:text/plain")
        	    		//new DataField("decoded", "varchar(100)")
        	    	};
        	 
        	 if (functionName.equalsIgnoreCase("plate")){
                 try {
                     systemLogic=new Intelligence(false);
                 } catch (Exception e) {
                	 logger.warn("unable to initialize Intelligence!");
                	 return false;
				} 
        	 }
         }
         else{
        	 logger.warn("Recognition function <" + functionName +"> not recognized!!!");
        	 return false;
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
	        if (this.functionName.equalsIgnoreCase("null")){
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
	        else if(this.functionName.equalsIgnoreCase("qr") && error==false && bufferedImage!=null){
	        	Result result = null;
	        	
	        	LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
	    		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	
	    		try {
	    			result = new MultiFormatReader().decode(bitmap);
	    		} catch (NotFoundException e) {
	    			// simply no QR code in image...go ahead...
	    			//return;
	    			error=true;
	    		}
	            
	    		if (result != null) {
	    			//post data here!!!!!
	    			try {
	    		         baos.reset( );
	    	        	 ImageIO.write(bufferedImage, "jpeg", baos);
	    	        	 output[0]= baos.toByteArray( ) ;
	    	        	 baos.close();
	    	        	 
	    	        	 StringBuilder sb = new StringBuilder();
	    	    	     sb.append(result) ; 
	    	    	     output[1] =sb.toString().getBytes();
	    	        	 
	    	    	     logger.info("I have decoded this QR: " + result);
	
	    		    } catch ( Exception e ) {
	    		         //logger.warn( e.getMessage( ) , e );
	    		         logger.info("Unable to decode image[3]: I'll just drop it...sorry");
	    		         error=true;
	    		    }
	    		}
	        }
	        else if(this.functionName.equalsIgnoreCase("plate")  && error==false){
	            String licencePlate=null;
	            try {
	                licencePlate=systemLogic.recognize(new CarSnapshot(bufferedImage));
	                
	                if (licencePlate!=null){
	    	            StringBuilder sb = new StringBuilder();
	    	            sb.append(licencePlate) ;
	    	            output[1]=sb.toString().getBytes();
	    	            //output[1]=sb.toString();
	    	            logger.info("I have decoded this LP: " + licencePlate);
	                }
	                
	            } catch (Exception e) {
	            	//logger.warn( e.getMessage( ) , e );
	            	logger.debug("Unable to decode image[4]: I'll just drop it...sorry");
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
