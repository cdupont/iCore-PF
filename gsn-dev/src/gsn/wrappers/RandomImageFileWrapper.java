package gsn.wrappers;

import gsn.beans.DataField;
import gsn.beans.AddressBean;
import gsn.beans.StreamElement;
import java.io.*;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;


/*
* This wrapper searches in a local directory for images
* that match the file-mask (given as regular expression)
* with the defined rate (rate parameter).
* The timestamp for the image is created from the file name following the time-format parameter
* See ./virtual-sensors/\S\S for an example
* */

public class RandomImageFileWrapper extends AbstractWrapper {
	
    private static final transient Logger logger = Logger.getLogger(RandomImageFileWrapper.class);

    private int threadCounter;
    private String imagesDirectory;
    private String fileExtension;
    private String fileMask;
    private long rate;

    

    private static final String PARAM_DIRECTORY = "directory";
    private static final String PARAM_FILE_MASK = "file-mask";
    private static final String PARAM_EXTENSION = "extension";
    private static final String PARAM_RATE = "rate";
    
    
    //private DataField[] collection = new DataField[] { new DataField ("image", "binary:image/"+ fileExtension, fileExtension + " image"),
	//	      new DataField("lastModified", "bigint", "Last time modified") };
    
    private DataField[] collection = new DataField[] {
    									new DataField ("image", "binary:image/"+ fileExtension, fileExtension + " image")
    												};

    

    public boolean initialize() {
        setName(getWrapperName() + "-" + (++threadCounter));
        AddressBean addressBean = getActiveAddressBean();

        fileExtension = addressBean.getPredicateValue(PARAM_EXTENSION);
        if (fileExtension == null) {
            logger.warn("The > "+PARAM_EXTENSION+" < parameter is missing from the wrapper for VS " + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }

        

        fileMask = addressBean.getPredicateValue(PARAM_FILE_MASK);
        if (fileMask == null) {
            logger.warn("The > "+PARAM_FILE_MASK+" < parameter is missing from the wrapper for VS " + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }

        imagesDirectory = addressBean.getPredicateValue(PARAM_DIRECTORY);
        if (imagesDirectory == null) {
            logger.warn("The > "+PARAM_DIRECTORY+" < parameter is missing from the wrapper for VS " + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }

        String rateStr = addressBean.getPredicateValue(PARAM_RATE);
        if (rateStr != null) {

            try {
                rate = Integer.parseInt(rateStr);
            } catch (NumberFormatException e) {
                logger.warn("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
                return false;
            }
        } else {
            logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }


        return true;
    }

    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        while (isActive()) {
            try {

            	SelectRandomImage(imagesDirectory,fileMask);

                Thread.sleep(rate);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
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
        return "RandomImageFileWrapper";
    }
    



    
    
     private Vector<String> SelectRandomImage(String dir, String regexFileMask) {
         
    	 File folder = new File(dir);
         String[] fileNames = folder.list();
         
         Vector <String> v = new Vector<String>();
         
         Random generator = new Random();
         int r;
         
         while (true){
	         r = generator.nextInt(fileNames.length);
	         
	         String fileName = dir+File.separator+fileNames[r];
	         
	         File file = new File(fileName);
	
	         Pattern pattern = Pattern.compile(regexFileMask);
	         Matcher matcher = pattern.matcher(fileName);
	
	         if (matcher.find()) {
	             //Long lastModified = file.lastModified();
	             //Date d = new Date(lastModified);
	             //System.out.println(d);
	             v.add(fileName);
	             //postData(fileName, lastModified);
	             postData(fileName);
	             
	             break;
	         }
         }
         return v;
     }

    /*
    * Posting data to database
    * */
    private boolean postData(String imagePath, long lastModified) {

        logger.debug("trying to post... " + imagePath);

        boolean success = true;

     
        
        Serializable[] stream = new Serializable[this.getOutputFormat().length];

        try {
            FileInputStream fileinputstream = new FileInputStream(imagePath);
            int numberBytes = fileinputstream.available();
            logger.debug("Image file has size: " + numberBytes + " bytes");
            byte bytearray[] = new byte[numberBytes];
            fileinputstream.read(bytearray);
            fileinputstream.close();
            stream[0] = bytearray;
        } catch (FileNotFoundException e) {
            logger.warn("Couldn't find image file: " + imagePath);
            logger.warn(e.getMessage(), e);
            success = false;
        } catch (IOException e) {
            logger.warn("Couldn't read image file: " + imagePath);
            logger.warn(e.getMessage(), e);
            success = false;
        }
       
        stream[1]=lastModified;
        StreamElement se = new StreamElement(getOutputFormat(), stream);

        if (success) {
            success = postStreamElement(se);
        }

        return success;
    }
    
    
    /*
     * Posting data to database
     * */
     private boolean postData(String imagePath) {

         logger.debug("trying to post... " + imagePath);

         boolean success = true;

      
         
         Serializable[] stream = new Serializable[this.getOutputFormat().length];

         try {
             FileInputStream fileinputstream = new FileInputStream(imagePath);
             int numberBytes = fileinputstream.available();
             logger.debug("Image file has size: " + numberBytes + " bytes");
             byte bytearray[] = new byte[numberBytes];
             fileinputstream.read(bytearray);
             fileinputstream.close();
             stream[0] = bytearray;
         } catch (FileNotFoundException e) {
             logger.warn("Couldn't find image file: " + imagePath);
             logger.warn(e.getMessage(), e);
             success = false;
         } catch (IOException e) {
             logger.warn("Couldn't read image file: " + imagePath);
             logger.warn(e.getMessage(), e);
             success = false;
         }
        
         StreamElement se = new StreamElement(getOutputFormat(), stream);

         if (success) {
             success = postStreamElement(se);
         }

         return success;
     } 
}
