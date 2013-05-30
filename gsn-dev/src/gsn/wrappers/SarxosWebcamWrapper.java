package gsn.wrappers;

import gsn.beans.DataField;
import gsn.beans.AddressBean;
import gsn.beans.StreamElement;
import gsn.wrappers.cameras.usb.ImageWrapper;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamCompositeDriver;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamAuth;
import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;




public class SarxosWebcamWrapper extends AbstractWrapper {
	
	/**
	 * Customized webcam driver.
	 */
	public static class HybridDriver extends WebcamCompositeDriver {

		public HybridDriver() {
			add(new WebcamDefaultDriver());
			add(new IpCamDriver());
		}
	}
	
    private static final transient Logger logger = Logger.getLogger(SarxosWebcamWrapper.class);
    private static int threadCounter=0;
    
    public static List<Webcam> wcl;
    
    public static boolean firstInstance=true;
    public static int howManyLocalCams=0;
    public static int howManyRemoteCams=0;
    
    private static final String PARAM_WCNAME = "webcam-name";
    private static final String PARAM_CAPTURERATE = "capture-rate";
    private static final String PARAM_LIVEVIEW = "live-view";

    private static final String PARAM_LOCAL = "local";
     
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PORT = "port";
    private static final String PARAM_SUFFIX = "suffix";
    private static final String PARAM_AUTH = "authentication";
    private static final String PARAM_USER = "user";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_MODE = "mode";
    
    
    
    private static final int DEFAULT_CAPTURERATE = 1000;
    private static final boolean DEFAULT_LIVEVIEW = true;
    
    private static final boolean DEFAULT_LOCAL = true;
    
    private static final String DEFAULT_ADDRESS = "192.168.9.140";
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_SUFFIX = "shot.jpg";
    private static final boolean DEFAULT_AUTH = false;
    private static final String DEFAULT_USER = "user";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_MODE = "jpeg";

    
    private  String webcamName;
    private  int capture_rate = DEFAULT_CAPTURERATE;
    private  boolean liveview = DEFAULT_LIVEVIEW;
    
    private  boolean localCamera = DEFAULT_LOCAL;
    
    private  String address = DEFAULT_ADDRESS;
    private  int port = DEFAULT_PORT;
    private  String suffix = DEFAULT_SUFFIX;
    private  boolean auth = DEFAULT_AUTH;
    private  String user = DEFAULT_USER;
    private  String password = DEFAULT_PASSWORD;
    private  String mode = DEFAULT_MODE;
    
    private final ByteArrayOutputStream   baos = new ByteArrayOutputStream( 16 * 1024 );
    
    private Webcam webcam = null;
    
   
    
    //private Executor executor; 
	
	//private IpCamDevice livecam;
    
    //for the GUI
    private JFrame mainFrame = null;
	private WebcamPanel panel = null;
	
    

    private DataField[] collection = new DataField[] { new DataField ("image", "binary:image/jpeg")};

    

    public boolean initialize() {
    	
    	if (firstInstance==true){
    		firstInstance=false;
    		
    		wcl=Webcam.getWebcams();
    		
    		howManyLocalCams=wcl.size();
    		Webcam.registerDriver(HybridDriver.class);
    	}
    	
    	boolean success = true;
    	
    	
        setName(getWrapperName() + "-" + (++threadCounter));
        
        AddressBean addressBean = getActiveAddressBean();

        String readString;
        
        webcamName = addressBean.getPredicateValue(PARAM_WCNAME);
        if (webcamName == null) {
            logger.error("The > "+PARAM_WCNAME+" < parameter is missing from the wrapper for VS " + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }

      //capture_rate
        readString= addressBean.getPredicateValue(PARAM_CAPTURERATE);
        if (readString != null) {
            try {
            	capture_rate = Integer.parseInt(readString);
            } catch (NumberFormatException e) {
                //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            }
        } /*else {
            logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
        }*/
        

      //liveview
        readString= addressBean.getPredicateValue(PARAM_LIVEVIEW);
        if (readString != null) {
            try {
            	liveview = Boolean.parseBoolean(readString);
            } catch (NumberFormatException e) {
                //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            }
        } /*else {
            logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
        }*/
        
        //localCamera
        readString= addressBean.getPredicateValue(PARAM_LOCAL);
        if (readString != null) {
            try {
            	localCamera = Boolean.parseBoolean(readString);
            } catch (NumberFormatException e) {
                //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            }
        } /*else {
            logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
        }*/
        
        
        
        if (localCamera == false) {
        	
        	//executor= Executors.newSingleThreadExecutor(); //???
        	
        	//load also remote parameters
        	readString= addressBean.getPredicateValue(PARAM_ADDRESS);
            if (readString != null) {
                try {
                	address = readString;
                } catch (NumberFormatException e) {
                    //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
                }
            } /*else {
                logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            }*/
            
          //port
            readString= addressBean.getPredicateValue(PARAM_PORT);
            if (readString != null) {
                try {
                	port = Integer.parseInt(readString);
                } catch (NumberFormatException e) {
                    //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
                }
            } /*else {
                logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            }*/
            
          //suffix
            readString= addressBean.getPredicateValue(PARAM_SUFFIX);
            if (readString != null) {
                try {
                	suffix = readString;
                } catch (NumberFormatException e) {
                    //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
                }
            } /*else {
                logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            }*/
            
          //auth
            readString= addressBean.getPredicateValue(PARAM_AUTH);
            if (readString != null) {
                try {
                	auth = Boolean.parseBoolean(readString);
                } catch (NumberFormatException e) {
                    //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
                }
            } /*else {
                logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            }*/
            
            if (auth==true){
    		      //user
    		        readString= addressBean.getPredicateValue(PARAM_USER);
    		        if (readString != null) {
    		            try {
    		            	user = readString;
    		            } catch (NumberFormatException e) {
    		                //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
    		            }
    		        } /*else {
    		            logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
    		        }*/
    		        
    		      //password
    		        readString= addressBean.getPredicateValue(PARAM_PASSWORD);
    		        if (readString != null) {
    		            try {
    		            	password = readString;
    		            } catch (NumberFormatException e) {
    		                //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
    		            }
    		        } /*else {
    		            logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
    		        }*/
            }
            
          //mode
            readString= addressBean.getPredicateValue(PARAM_MODE);
            if (readString != null) {
                try {
                	mode = readString;
                } catch (NumberFormatException e) {
                    //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
                }
            } /*else {
                logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            }*/
        	
        }
        
        /*if (liveview==true){
        	//liveview_rate
            readString= addressBean.getPredicateValue(PARAM_LIVEVIEWRATE);
            if (readString != null) {
                try {
                	liveview_rate = Integer.parseInt(readString);
                } catch (NumberFormatException e) {
                    //logger.debug("The > "+PARAM_RATE+" < parameter is invalid for wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
                	liveview= false;
                	liveview_rate=-1;
                }
            } else {
                logger.warn("The > "+PARAM_RATE+" < parameter is missing from the wrapper in VS " + this.getActiveAddressBean().getVirtualSensorName());
            }
        }*/
        
        try {
        	success= webcamInitialization();
		} catch (MalformedURLException e) {
			success=false;
		}
        
        return success;
    }
    
    
    private boolean webcamInitialization ( ) throws MalformedURLException{
    	
    	

    	String title="";
    	Dimension size;
    	
    	if (this.localCamera == true){
	    	List<Webcam> webcamList=Webcam.getWebcams();
	    	if (webcamList.isEmpty()){
				logger.error( "No webcams installed!!!");
	            return false;
			}
	    	for (int i=0;i<webcamList.size();i++){
	    		if (webcamName.equalsIgnoreCase(webcamList.get(i).getName())){
	    			webcam = webcamList.get(i);
	    			break;
	    		}
	    	}
	    	if (webcam==null){
	    		logger.debug( "webcam named " + webcamName + " not found!");
	    		webcam = Webcam.getWebcams().get(0);
	    		logger.debug( "Using default webcam named " + webcam.getName());
	    	}
	    	if (webcam==null){
	    		//can we stay here????
	    		logger.error( "STRANGE ERROR IN " + this.getActiveAddressBean().getVirtualSensorName());
	            return false;
	    	}
			
			if (webcam.isOpen()){
				logger.debug( webcam.getName() + " is open: I'll try to close it");
				webcam.close();
			}
	    	
			size = WebcamResolution.QVGA.getSize();
			webcam.setViewSize(size);
			
			title = "Live-view from " + this.webcamName;
			
			
		
    	}
    	else{
    		
    		
    		
    		
    		//remote camera

    		String protString="http://";
    		
    		
    		IpCamMode camMode=IpCamMode.PULL;
    		if (this.mode.toLowerCase().equals("jpeg")){
    			camMode=IpCamMode.PULL;
    		}
    		else if (this.mode.toLowerCase().equals("mjpeg")){
    			camMode=IpCamMode.PUSH;
    		}
    		else{
    			System.err.println("specify one mode!");
    		}
    		
    		IpCamAuth aut=null;
    		if (this.auth==true){
    			new IpCamAuth(this.user, this.password);
    		}
    		
    		
    		String addressStr = buildConnectionString(protString,this.address,this.port,this.suffix);	
    		IpCamDevice livecam = new IpCamDevice(this.webcamName, new URL(addressStr), camMode,aut);
    		
    		IpCamDeviceRegistry.register(livecam);
    		
    		Webcam
    		
    		//wcl=Webcam.getWebcams();
    		webcam=Webcam.getWebcams().get(howManyLocalCams+howManyRemoteCams); //it is the last added! ;)
    		
    		howManyRemoteCams++;
    		size = WebcamResolution.QVGA.getSize();
			webcam.setViewSize(size);
    		//size=webcam.getViewSize();
    		
    		title = "Live IP Camera: " +  address;
    		
    		//executor.execute(this); //???????
    		
    	}
    	
    	if ( liveview ) {
    		
			mainFrame = new JFrame( title );
            
            mainFrame.setLayout(new FlowLayout());
    		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		
    		panel = new WebcamPanel(webcam,size, true);
    		//panel.setPreferredSize(size);
    		panel.setFPS(20);
    		
    		mainFrame.add(panel);
    		mainFrame.pack();
    		mainFrame.setVisible(true);
  
		}
    	else{
    		//be sure webcam is open!
        	if (!webcam.isOpen()){
        		webcam.open();
        	}
        	//executor= Executors.newSingleThreadExecutor(); //???
        	//executor.execute(this); //???????
    	}
		
    	//Webcam_list.add(webcam);
    	
		return true;
    	
    	
    }
    
    

    public void run() {
    	
    	boolean postDataOk=true;
    	
    	
    	while(postDataOk==true){
			try {
				Thread.sleep(capture_rate);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}

			BufferedImage image = null;
			if (webcam.isOpen()) {
				if ((image = webcam.getImage()) != null) {
					postDataOk= PostData(new ImageWrapper(image));
				}
			}	
    	}
    	
    	//for debug close everything!
		if (postDataOk==false){//if not necessary, indeed
			this.dispose();
			logger.error("Post data Failed!!!!");
		}
    }
    
    
    public DataField[] getOutputFormat() {
    	return collection;
    }

    public void dispose() {
    	logger.debug("dispose called in " + this.getActiveAddressBean().getVirtualSensorName());
    	//mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	if (liveview){
    		mainFrame.dispose();
    	}
    	
    	if (webcam!=null){
    		if (webcam.isOpen()){
    			logger.debug("Webcam closed during dispose called in " + this.getActiveAddressBean().getVirtualSensorName());
    			webcam.close();
    		}
    	}
        threadCounter--;
    }

    public String getWrapperName() {
        return "SarxosWebcamWrapper";
    }
    


    private boolean PostData(ImageWrapper reading) {
        boolean success = true;
        Serializable[] output = new Serializable[this.getOutputFormat().length];
		
        
        //image 
        try {
	         baos.reset( );
	         if ( reading != null ) {
	        	 //ImageIO.write(reading.getIm(), "jpeg", baos);
	        	 
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
    
    
    
    public static String buildConnectionString(String protocol, String address, int port, String filename){
		return protocol + address + ":" + String.valueOf(port) + "/" + filename;
	}
    
}
