package gsn.vsensor;

import gsn.beans.DataTypes;
import gsn.beans.StreamElement;
import gsn.beans.VSensorConfig;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.log4j.Logger;
import matlabcontrol.*;
//import matlabcontrol.extensions.MatlabNumericArray;
//import matlabcontrol.extensions.MatlabTypeConverter;

public class MatlabVS2_CB extends AbstractVirtualSensor {

	private final static transient Logger      logger         = Logger.getLogger( AbstractVirtualSensor.class );
	
	
	private String[] fieldNames = {"matlab_output"};
	private Byte[] fieldTypes = {DataTypes.DOUBLE};
	
	private String functionName, defaultFunctionName = "myGSNMatlabFunction";
	private Integer inArgs;
	private Double[] parameters;
	
	private Hashtable<String, ArrayBlockingQueue<StreamElement>> circularBuffers;
	  
	private Integer windowSize;
	
	private  MatlabProxy proxy;
	
	//private MatlabTypeConverter processor;
	
	/* (non-Javadoc)
	 * @see gsn.vsensor.AbstractVirtualSensor#dataAvailable(java.lang.String, gsn.beans.StreamElement)
	 */
	@Override
	public void dataAvailable(String inputStreamName, StreamElement streamElement) {
		
		ArrayBlockingQueue<StreamElement> circularBuffer = circularBuffers.get(inputStreamName);
		
		if (circularBuffer == null){
	      circularBuffer = new ArrayBlockingQueue<StreamElement>(windowSize);
	      circularBuffers.put(inputStreamName, circularBuffer);
	    }
	    try {
			circularBuffer.put(streamElement);
		} catch (InterruptedException e1) {
			logger.warn(e1);
		}
	    
	    logger.debug("Window for " + inputStreamName + " contains: " + circularBuffer.size() + " of " + windowSize);
      
	    if (circularBuffer.size() == windowSize){
	        logger.info("Window for " + inputStreamName + " contains: " + circularBuffer.size() + " of " + windowSize);
	        
	        double [] values = new double[windowSize];
	        StreamElement elt = null;
	        Object[] elts = circularBuffer.toArray();
	      
	        //can be more efficient!
	        // convert the circular buffer to an array
	        for (int i = 0; i < elts.length; i++){
	          elt = (StreamElement) elts[i];
	          values[i] = ((Number) elt.getData()[0]).doubleValue(); //
	        }
	        
	        if (proxy.isConnected()){
		        try {
					proxy.setVariable("gsn_in",(Object)values);  //set variable in matlab workspace
				} catch (MatlabInvocationException e) {
					logger.warn(e);
				}
		        String matlabCommand = "gsn_out="+ functionName + "(gsn_in);" ;
		        logger.debug("Calling matlab engine with command: " + matlabCommand);
		        try {
					//proxy.eval("disp(gsn_in);");
					proxy.eval(matlabCommand);
					
					//Get the array from MATLAB:
					//case 1: need processor and MatlabNumericArray
				    //MatlabNumericArray array = processor.getNumericArray("gsn_out");
	
					//case 2
					double result = ((double[]) proxy.getVariable("gsn_out"))[0];
					
					if(logger.isDebugEnabled()){
						logger.debug("Received output from matlab: " + String.valueOf(result));
					}
					
					StreamElement resultStream = new StreamElement(fieldNames, fieldTypes , new Serializable[] {result});
					dataProduced(resultStream);
					
					// Remove the oldest element from the queue!
			        circularBuffer.take();
				} catch (MatlabInvocationException e) {
					logger.warn(e);
				} catch (InterruptedException e) {
					logger.warn(e);
				}
		    }//is connected
	        else{
	        	logger.warn("Proxy is disconnected!!!");
	        }
	   }//window-size
	} //dataAvailable
      
      

	/* (non-Javadoc)
	 * @see gsn.vsensor.AbstractVirtualSensor#dispose()
	 */
	@Override
	public void dispose() {
		proxy.disconnect();
	}

	/* (non-Javadoc)
	 * @see gsn.vsensor.AbstractVirtualSensor#initialize()
	 */
	@Override
	public boolean initialize() {
		boolean success = false;
		VSensorConfig vsensor = getVirtualSensorConfiguration();
		TreeMap < String , String > params = vsensor.getMainClassInitialParams( );
		
		
		MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
        .setUsePreviouslyControlledSession(true)
        .build();
		MatlabProxyFactory factory = new MatlabProxyFactory(options);
		try {
			proxy = factory.getProxy();
			proxy.eval("disp('initialize():coming from GSN!!!')");
			
			//processor = new MatlabTypeConverter(proxy);
			
			functionName = params.get("function");
            if(functionName == null || functionName.trim().equals("")){
            	functionName = defaultFunctionName;
            }
            if(logger.isDebugEnabled()){
            	logger.debug("Function name configured to: " + functionName);
            }
            inArgs = Integer.parseInt(params.get("arguments"));
            if(inArgs == null){
            	inArgs = new Integer(0);
            }
            else{
            	parameters = new Double[inArgs];
            }
            if(logger.isDebugEnabled()){
            	logger.debug("Number of arguments configured to: " + inArgs);
            }
            
            windowSize = Integer.parseInt(params.get("windows-size"));
            if(windowSize == null){
            	windowSize = new Integer(1);
            }
            if(logger.isDebugEnabled()){
            	logger.debug("Window-size configured to: " + windowSize);
            }
            
          //initialize the circular buffer
    	  circularBuffers = new Hashtable<String, ArrayBlockingQueue<StreamElement>>();
    	  
    	  
            
            success = true;
		} catch (MatlabConnectionException e) {
			logger.warn(e);
		} catch (MatlabInvocationException e) {
			logger.warn(e);
		} catch (Exception e) {
            logger.warn(e);
        }
		
		
		
		return success;
	}

}
