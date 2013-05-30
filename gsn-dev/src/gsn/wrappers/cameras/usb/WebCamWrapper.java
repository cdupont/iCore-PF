package gsn.wrappers.cameras.usb;

/**
 * Before using this class make sure you have the <code>jmf.jar</code>
 * [form http://java.sun.com/products/java-media/jmf/index.jsp] in your classpath.
 * Once you have the above jar file in the classpath (e.g., by putting it in the lib directory of GSN,
 * you need to remove the <code>excludes="gsn/wrappers/cameras/usb/WebCamWrapper.java"</code> from the
 * build.xml file so that GSN will compile this file.
 */

// For more resources see :
// http://www.geocities.com/marcoschmidt.geo/java-image-coding.html

import gsn.beans.AddressBean;
import gsn.beans.DataField;
import gsn.beans.StreamElement;
import gsn.wrappers.AbstractWrapper;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSourceException;
import javax.media.NoProcessorException;
import javax.media.NotRealizedError;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.control.FormatControl;
import javax.media.format.RGBFormat;
import javax.media.format.YUVFormat;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.util.BufferToImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;


import javax.imageio.ImageIO;

public class WebCamWrapper extends AbstractWrapper implements ControllerListener {

	private static final transient Logger logger                       = Logger.getLogger( WebCamWrapper.class );
	private static int                    threadCounter                = 0;
	
	
   private static final String PARAM_WCNAME = "webcam-name";
   private static final String PARAM_CAPTURERATE = "capture-rate";
   private static final String PARAM_LIVEVIEW = "live-view";
   private static final String PARAM_MAXPOST = "max-posts";
   
   private static final String DEFAULT_WCNAME = "vfw:Microsoft WDM Image Capture (Win32):0";
   private static final int DEFAULT_CAPTURERATE = 1000;
   private static final boolean DEFAULT_LIVEVIEW = false;
   private static final long DEFAULT_MAXPOSTS = Long.MAX_VALUE;
   
   private  String webcamName = DEFAULT_WCNAME ;
   private  int capture_rate = DEFAULT_CAPTURERATE;
   private  boolean liveview = DEFAULT_LIVEVIEW;
   private  long max_posts = DEFAULT_MAXPOSTS;
   
   public int n_post;
   
   private final ByteArrayOutputStream   baos                         = new ByteArrayOutputStream( 16 * 1024 );
   
   private Buffer                        buff                         = new Buffer( );
   
   private PushBufferStream              camStream;                                                             // Global
   
   private BufferToImage                 converter;                                                             // Global
                                                                                                                 
   private JPanel                        panel                        = null;
 
   private ImageWrapper             reading;                                                                                             
   private Object                        stateLock                    = new Object( );
   
   private int                           height;
   
   private int                           width;
   
   private JFrame                        mainFrame;
   
   private DataSource                    ds                           = null;
   
   private Processor                     deviceProc                   = null;
   
   private PushBufferDataSource          source                       = null;
   
   
   

   
   private DataField[] collection = new DataField[] { new DataField ("image", "binary:image/jpeg")};
   // -----------------------------------START----------------------------------------
   // DEVICE NAME FOR LINUX CAM: "v4l:OV518 USB Camera:0"
   
   
   public DataField[] getOutputFormat() {
	   return collection;
   }
   
   
   public boolean initialize ( ) {
      setName( "WebCamWrapper-Thread:" + ( ++threadCounter ) );
      
      AddressBean addressBean = getActiveAddressBean( );
      
      String readString;
      
      readString  = addressBean.getPredicateValue(PARAM_WCNAME);
      if (readString != null) {
    	  webcamName = readString;
      }

      //capture_rate
      readString= addressBean.getPredicateValue(PARAM_CAPTURERATE);
      if (readString != null) {
          try {
          	capture_rate = Integer.parseInt(readString);
          } catch (NumberFormatException e) {}
      }
      

      //liveview
      readString= addressBean.getPredicateValue(PARAM_LIVEVIEW);
      if (readString != null) {
          try {
          	liveview = Boolean.parseBoolean(readString);
          } catch (NumberFormatException e) {}
      }
      
      /*
      //force false to live-view!!!!
      if (liveview){
      	logger.info("Live view mode is not implemented yet...setting live-view to FALSE!");
      	liveview=false;
      }
      */
      
      //max_posts
      readString= addressBean.getPredicateValue(PARAM_MAXPOST);
      if (readString != null) {
          try {
          	max_posts = Long.parseLong(readString);
          	
          	if (max_posts<=0){
          		max_posts=Long.MAX_VALUE;
          	}
          } catch (NumberFormatException e) {}
      }
      
      n_post=0;
      
      return webcamInitialization();
   }
   
   private boolean webcamInitialization (  ) {
	  
	  //printDeviceList();
	  
	  
	  CaptureDeviceInfo device = null;
	  
	  Vector<CaptureDeviceInfo> list = CaptureDeviceManager.getDeviceList ( null );
	  if (!list.isEmpty()){
		  for (int i=0;i<list.size();i++){
			  if (list.get(i).getName().equalsIgnoreCase(webcamName)){
				  device=list.get(i);
				  break; //FOUND!!!
			  }
		  }
	  }
	  else{
		  logger.error("WebCamWrapper: Something wrong in listing media devices!");
		  return false;
	  }
	  if (device==null){
		  logger.error("WebCamWrapper: Cannot find " + webcamName +" media device!");
		  return false;
	 }
	 
	  int max_rety=10;
	  int retry=0;
	  
	  MediaLocator ml = null;
	  while(ml==null){
	  	ml=device.getLocator();
	  	
	  	retry++;
	  	if (ml==null){
			  logger.debug("WebCamWrapper: [getLocator] RETRY "+String.valueOf(retry)+"/"+String.valueOf(max_rety)+" FAILED");
			  if (retry==max_rety){
			  		logger.error("WebCamWrapper: Cannot locate media device!");
					return false;
			  }
		}
	  }
	  
	  logger.debug("WebCamWrapper: [getLocator] OK!");
	  
	  try {
         ds = Manager.createDataSource( ml );
      } catch ( NoDataSourceException e ) {
    	  //busy resource!!
         logger.error( "WebCamWrapper: Cannot create dataSource!" );
         logger.error( e.getMessage( ) , e );
         return false;
      } catch ( IOException e ) {
         logger.error( "WebCamWrapper: I/O error creating dataSource" );
         logger.error( e.getMessage( ) , e );
         return false;
      }
      
      if ( !( ds instanceof CaptureDevice ) ) {
         logger.error( "WebCamWrapper: DataSource not a CaptureDevice" );
         return false;
      }
	 
	 
      FormatControl [ ] fmtControls = ( ( CaptureDevice ) ds ).getFormatControls( );
      

      if ( fmtControls == null || fmtControls.length == 0 ) {
         logger.error( "WebCamWrapper: No FormatControl available" );
         return false;
      }
      
      Format setFormat = null;
      YUVFormat userFormat = null;
      
      for ( Format format : device.getFormats( ) ){
         if ( format instanceof YUVFormat ){
        	 userFormat = ( YUVFormat ) format;
         }
      }
      
      this.width = userFormat.getSize( ).width;
      this.height = userFormat.getSize( ).height;
      
      for ( int i = 0 ; i < fmtControls.length ; i++ ) {
         if ( fmtControls[ i ] == null ) continue;
         if ( ( setFormat = fmtControls[ i ].setFormat( userFormat ) ) != null ) {
            break;
         }
      }
      
      if ( setFormat == null ) {
         logger.error( "WebCamWrapper: Failed to set device to specified mode" );
         return false;
      }
      
      try {
         ds.connect( );
      } catch ( IOException ioe ) {
         logger.error( "WebCamWrapper: Unable to connect to DataSource" );
         logger.error( ioe.getMessage( ) , ioe );
         return false;
      }
      logger.debug( "WebCamWrapper: SUCCESS!!!!Data source created and format set" );
      
      try {
         deviceProc = Manager.createProcessor( ds );
      } catch ( IOException ioe ) {
         logger.error( "WebCamWrapper: Unable to get Processor for device: " + ioe.getMessage( ) );
         return false;
      } catch ( NoProcessorException npe ) {
         logger.error( "WebCamWrapper: Unable to get Processor for device: " + npe.getMessage( ) );
         return false;
      }
      
      /*
       * In order to use the controller we have to put it in the realized state.
       * We do this by calling the realize method, but this will return
       * immediately so we must register a listener (this class) to be notified
       * TIMED the controller is ready. The class containing this code must
       * implement the ControllerListener interface.
       */

      deviceProc.addControllerListener( this );
      deviceProc.realize( );
      
      while ( deviceProc.getState( ) != Controller.Realized ) {
         synchronized ( stateLock ) {
            try {
               stateLock.wait( );
            } catch ( InterruptedException ie ) {
               logger.error( "Device failed to get to realized state" );
               return false;
            }
         } 		
      }
      
      deviceProc.start( );
      logger.info( "Before Streaming" );
      try {
         source = ( PushBufferDataSource ) deviceProc.getDataOutput( );
      } catch ( NotRealizedError nre ) {
         /* Should never happen */
         logger.error( "Internal error: processor not realized" );
         return false;
      }
      
      PushBufferStream [ ] streams = source.getStreams( );
      
      for ( int i = 0 ; i < streams.length ; i++ )
         if ( streams[ i ].getFormat( ) instanceof RGBFormat ) {
            camStream = streams[ i ];
            RGBFormat rgbf = ( RGBFormat ) streams[ i ].getFormat( );
            converter = new BufferToImage( rgbf );
         } else if ( streams[ i ].getFormat( ) instanceof YUVFormat ) {
            camStream = streams[ i ];
            YUVFormat yuvf = ( YUVFormat ) streams[ i ].getFormat( );
            converter = new BufferToImage( yuvf );
         }
      if (this.liveview ) {
         mainFrame = new JFrame( "Live-view from USB" );
         panel = new JPanel( );
         
 		 mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         mainFrame.getContentPane( ).add( panel );
         mainFrame.setSize( getWidth( ), getHeight( ));
         mainFrame.setResizable( false );
         mainFrame.setVisible( true );
      }
      return true;
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
   
   
   
   
   public void controllerUpdate ( ControllerEvent ce ) {
      if ( ce instanceof RealizeCompleteEvent ) {
         logger.info( "Realize transition completed" );
         synchronized ( stateLock ) {
            stateLock.notifyAll( );
         }
      }
   }
   
   
   private int getHeight ( ) {
      return height;
   }
   
   private int getWidth ( ) {
      return width;
   }
   
   private Image getImage ( ) {
      try {
         camStream.read( buff );
      } catch ( Exception ioe ) {
         logger.error( "Unable to capture frame from camera" );
         logger.error( ioe.getMessage( ) , ioe );
         return null;
      }
      return converter.createImage( buff );
   }
   


   public void run() {
   	//boolean postDataOk;
	   
   	while(n_post<max_posts){
			try {
				Thread.sleep(capture_rate);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
			reading = new ImageWrapper(getImage());
			
			
			if (this.liveview) {
	        	  Graphics2D graphics2D = ( Graphics2D ) panel.getGraphics( );
	        	  graphics2D.drawImage( reading.getIm( ) , 0  , 0 , null );
	        }
			PostData(reading);
			n_post++;
				
				
   	}
   	logger.info("posted " + String.valueOf(n_post)+ " images: now stopping!");
   	this.dispose();
   	/*for debug close everything!
		if (postDataOk==false){//if not necessary, indeed
			this.dispose();
			//logger.error("Post data Failed!!!!");
		}
		*/
   }
   
   
   
   public void dispose ( ) {
      source.disconnect( );
      deviceProc.stop( );
      deviceProc.deallocate( );
      deviceProc.close( );
      ds.disconnect( );
      if ( this.mainFrame != null ){
    	  this.mainFrame.dispose( );
      }
      threadCounter--;
   }
   
   
   public String getWrapperName() {
	   return "WebCamWrapper";
   }
   
  
   
   
   public static void printDeviceList() {
	   System.out.println("List of capturing devices: ");
	   Vector devices = CaptureDeviceManager.getDeviceList(null);
	   Enumeration list = devices.elements();
	   while (list.hasMoreElements()) {
		   CaptureDeviceInfo deviceInfo =(CaptureDeviceInfo) list.nextElement();
		   String name = deviceInfo.getName();
		   
		   Format[] fmts = deviceInfo.getFormats();
		   System.out.println("NAME: " +name);
		   for (int i = 0; i < fmts.length; i++) {
			   System.out.println("\t"+fmts[i]);
		   }
	   }
   }

}
