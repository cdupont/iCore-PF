package gsn.wrappers;

import gsn.beans.DataField;
import gsn.beans.AddressBean;
import gsn.beans.StreamElement;
import gsn.wrappers.cameras.usb.ImageWrapper;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.ipcam.IpCamAuth;
import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;




/*
* This wrapper acquires a stream from a camera
* */

public class SarxosRemoteCamWrapper extends AbstractWrapper {

    private static final transient Logger logger = Logger.getLogger(SarxosRemoteCamWrapper.class);
    private static int threadCounter=0;
    
    

    
    public static IpCamDriver driver;
    
    
    public int n_post;
    
    private static final String PARAM_WCNAME = "webcam-name";
    private static final String PARAM_CAPTURERATE = "capture-rate";
    private static final String PARAM_MAXPOST = "max-posts";
    private static final String PARAM_LIVEVIEW = "live-view";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_AUTH = "authentication";
    private static final String PARAM_USER = "user";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_MODE = "mode";
    
    
    
    private static final int DEFAULT_CAPTURERATE = 1000;
    private static final boolean DEFAULT_LIVEVIEW = false;
    private static final long DEFAULT_MAXPOSTS = Long.MAX_VALUE;
    private static final boolean DEFAULT_AUTH = false;
    private static final String DEFAULT_USER = "user";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_MODE = "pull";

    
    private boolean liveview = DEFAULT_LIVEVIEW;
    private int capture_rate = DEFAULT_CAPTURERATE;
    private long max_posts = DEFAULT_MAXPOSTS;
    
    private final ByteArrayOutputStream   baos = new ByteArrayOutputStream( 16 * 1024 );
    
    private Webcam webcam = null;
    
    private IpCamDevice livecam = null;
    

    
    //for the GUI
    private JFrame mainFrame = null;
	private JPanel jpanel = null;
	


    private DataField[] collection = new DataField[] { new DataField ("image", "binary:image/jpeg")};

    static {
    		driver = new IpCamDriver();
    		Webcam.setDriver(driver);
    }

    public boolean initialize() {
    	
    	threadCounter++;

    	boolean success = true;
    	
    	
        setName(getWrapperName() + "-" + (threadCounter));
        
        AddressBean addressBean = getActiveAddressBean();

        String readString;
        
        
        String webcamName = addressBean.getPredicateValue(PARAM_WCNAME);
        if (webcamName == null) {
            logger.warn("The > "+PARAM_WCNAME+" < parameter is missing from the wrapper for VS " + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }
        
        
        String address = addressBean.getPredicateValue(PARAM_ADDRESS);
        if (address == null) {
            logger.warn("The > "+PARAM_ADDRESS+" < parameter is missing from the wrapper for VS " + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }
        
        //capture_rate
        readString= addressBean.getPredicateValue(PARAM_CAPTURERATE);
        if (readString != null) {
            try {
            	this.capture_rate = Integer.parseInt(readString);
            } catch (NumberFormatException e) {}
        }
        

        //liveview
        readString= addressBean.getPredicateValue(PARAM_LIVEVIEW);
        if (readString != null) {
            try {
            	this.liveview = Boolean.parseBoolean(readString);
            } catch (NumberFormatException e) {}
        }
        
        //max_posts
        readString= addressBean.getPredicateValue(PARAM_MAXPOST);
        if (readString != null) {
            try {
            	this.max_posts = Long.parseLong(readString);
            	
            	if (this.max_posts<=0){
            		this.max_posts=Long.MAX_VALUE;
            	}
            } catch (NumberFormatException e) {}
        }
        
 
        //mode
        String mode = DEFAULT_MODE;
        IpCamMode camMode=IpCamMode.PULL;
        readString= addressBean.getPredicateValue(PARAM_MODE);
        if (readString != null) {
            try {
            	mode = readString;
            } catch (NumberFormatException e) {}
        }
        
        if (mode.toLowerCase().equals("push")){
			camMode=IpCamMode.PUSH;
        }
        
        
        //auth
        boolean auth = DEFAULT_AUTH;
        IpCamAuth aut=null;
        readString= addressBean.getPredicateValue(PARAM_AUTH);
        if (readString != null) {
            try {
            	auth = Boolean.parseBoolean(readString);
            } catch (NumberFormatException e) {}
        }
        
        if (auth==true){
		      	//user
        		String user = DEFAULT_USER;
		        readString= addressBean.getPredicateValue(PARAM_USER);
		        if (readString != null) {
		            try {
		            	user = readString;
		            } catch (NumberFormatException e) {}
		        }
		        
		        //password
		        String password = DEFAULT_PASSWORD;
		        readString= addressBean.getPredicateValue(PARAM_PASSWORD);
		        if (readString != null) {
		            try {
		            	password = readString;
		            } catch (NumberFormatException e) {}
		        }
		        
		        aut = new IpCamAuth(user, password);
        }
        
        
        
        try {
        	success= webcamInitialization(webcamName, address, camMode, aut);
		} catch (MalformedURLException e) {
			success=false;
		}
        
        n_post=0;
        
        return success;
    }
    
    
    private boolean webcamInitialization (String wcname, String addr, IpCamMode camMode, IpCamAuth aut) throws MalformedURLException{
    	
  
    	Dimension size=new Dimension(320,240);

		
		
		livecam = new IpCamDevice(wcname, new URL(addr), camMode, aut);
		
		livecam.setResolution(size);
		
		if (!IpCamDeviceRegistry.isRegistered(livecam)){
			driver.register(livecam);
			if (!livecam.isOpen()){
				livecam.open();
			}
		}
		
		//be sure camera can produce images....
		try{
			if (!livecam.isOpen()){
				livecam.open();
			}
			livecam.getImage();
		} catch (Exception e) {
			if (livecam.isOpen()){
				livecam.close();
			}
			logger.warn("Remote camera " + livecam.getName() + " (" + livecam.getURL().toString() + ") could not be initialized: is it online??");
			return false;
		}
		
		

    	if ( this.liveview ) {

	         mainFrame = new JFrame( "Live-view from " + wcname );
	         mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	         //mainFrame.setPreferredSize(livecam.getResolution());
	         mainFrame.setSize(livecam.getResolution());
	         
	         mainFrame.setResizable( true );
	         jpanel = new JPanel( );
	         //jpanel.setPreferredSize(livecam.getResolution());
	         jpanel.setSize(livecam.getResolution());
	         
	         mainFrame.add(jpanel);
	 		 
	         //mainFrame.getContentPane( ).add( jpanel );
	         //mainFrame.setSize( size.width, size.height);
	        // mainFrame.setResizable( false );
	         mainFrame.setVisible( true );
		}
    	
    	
		return true;	
    }
    
    

    public void run() {
    	
    	//boolean postDataOk;
    	
    	try{
	    	while(n_post<this.max_posts){
				try {
					Thread.sleep(this.capture_rate);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
					return;
				}
				
				
				BufferedImage image = null;
				if (livecam.isOpen()) {
					if ((image = livecam.getImage()) != null) {
						if (this.liveview) {
				        	  Graphics2D graphics2D = ( Graphics2D ) jpanel.getGraphics( );
				        	  graphics2D.drawImage(image, 0  , 0 , null );
				        	  
				        }
						PostData(new ImageWrapper(image));
						n_post++;
					}
				}	
	    	}
	    	
	    	logger.info("posted " + String.valueOf(n_post)+ " images: now stopping!");
	    	this.dispose();
	    	
    	} catch (Exception e) {
			if (livecam.isOpen()){
				livecam.close();
			}
			logger.warn("Remote camera " + livecam.getName() + " (" + livecam.getURL().toString() + ") produced an error: is it still online??");
			this.dispose();
    	}
    }
    
    
    public DataField[] getOutputFormat() {
    	return collection;
    }

    

    public String getWrapperName() {
        return "SarxosRemoteCamWrapper";
    }
    


    private boolean PostData(ImageWrapper reading) {
        boolean success = true;
        Serializable[] output = new Serializable[this.getOutputFormat().length];
		
        
        try {
	         baos.reset( );
	         if ( reading != null ) {
	        	 ImageIO.write(reading.getIm(), "png", baos);
	        	 
	        	 output[0]= baos.toByteArray( ) ;
	        	 baos.close();
	        }
	    } catch ( Exception e ) {
	         logger.error( e.getMessage( ) , e );
	    }
		

		//build StreamElement
		StreamElement se = new StreamElement(getOutputFormat(), output);
		
        if (success) {
            success = postStreamElement(se);
        }

        return success;		
    }
    
    public void dispose() {
	    	logger.debug("dispose called in " + this.getActiveAddressBean().getVirtualSensorName());
	    	if (this.liveview){
	    		mainFrame.dispose();
	    	}
	    	
	    	if (webcam!=null){
	    		if (webcam.isOpen()){
	    			logger.debug("Webcam closed during dispose called in " + this.getActiveAddressBean().getVirtualSensorName());
	    			
	    			IpCamDeviceRegistry.unregister(livecam);
	    			webcam.close();
	    		}
	    	}
	        threadCounter--;
    }
    

    public static String buildConnectionString(String protocol, String address, int port, String filename){
		return protocol + address + ":" + String.valueOf(port) + "/" + filename;
	}
     
}
