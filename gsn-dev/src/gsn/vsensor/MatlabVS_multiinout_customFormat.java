package gsn.vsensor;

import gsn.beans.DataTypes;
import gsn.beans.StreamElement;
import gsn.beans.VSensorConfig;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.log4j.Logger;
import matlabcontrol.*;
//import matlabcontrol.extensions.MatlabNumericArray;
//import matlabcontrol.extensions.MatlabTypeConverter;

public class MatlabVS_multiinout_customFormat extends AbstractVirtualSensor {

	private final static transient Logger      logger         = Logger.getLogger( AbstractVirtualSensor.class );
	
	//output structures
	private Integer outArgs;
	private String[] outFieldNames;
	private Byte[] outFieldTypes;
	
	private String functionName, defaultFunctionName = "myGSNMatlabFunction";
	
	//input structures
	private Integer inArgs;
	private Byte[] inFieldTypes;
	
	
	
	//private Double[] parameters;
	
	private Hashtable<String, ArrayBlockingQueue<StreamElement>> circularBuffers;
	  
	private Integer windowSize;
	
	private  MatlabProxy proxy;
	
	
	
	//private MatlabTypeConverter processor;
	
	
	
	/* (non-Javadoc)
	 * @see gsn.vsensor.AbstractVirtualSensor#initialize()
	 */
	@Override
	public boolean initialize() {
		
		boolean success = false;
		VSensorConfig vsensor = getVirtualSensorConfiguration();
		
		//get parameters from xml file
		TreeMap < String , String > params = vsensor.getMainClassInitialParams( );
		
		
		//build output structure
		 outArgs = Integer.parseInt(params.get("out-arguments"));
		 if(outArgs == null){
        	 outArgs = new Integer(1);
         }
		 outFieldNames = new String[outArgs];
		 outFieldTypes =   new Byte[outArgs];
         for (int i=0;i<outArgs;i++){
        	 outFieldNames[i]="matlab_output_"+String.valueOf(i+1);
        	 outFieldTypes[i]=DataTypes.DOUBLE;
         }
         
        
         
         
         //build input structure
         inArgs = Integer.parseInt(params.get("in-arguments"));
         if(inArgs == null){
         	inArgs = new Integer(0);
         }
         else{
        	 //we have input parameters: parse their format
        	 
        	 inFieldTypes =   new Byte[inArgs];
        	 
        	 boolean parsed=true;
	         String inFormat = params.get("in-format");
	         if(inFormat == null || inFormat.trim().equals("")){
	        	 logger.warn("in-format not defined: set everything to double[]");
	        	 parsed=false;
	         }
	         else{
	        	 String[] cks=inFormat.split("[:]");
	        	 if (cks.length!=inArgs){
	        		 logger.warn("Input format cannot be met: set everything to double[]");
	        		 parsed=false;
	        	 }
	        	 else{
	        		 //here I have everything to set the input parameters:do it
	        		 
	        		 for (int i=0;i<cks.length;i++) {
	        	            if (cks[i].toUpperCase().equals("DOUBLE")) {
	        	            	inFieldTypes[i]=DataTypes.DOUBLE;
	        	            }
	        	            else if (cks[i].toUpperCase().equals("STRING")) {
	        	            	inFieldTypes[i]=DataTypes.BINARY;
	        	            }
	        	     }
	        		
	        		 parsed=true;
	        	 }
	         }
	         if (parsed==false){
	        	 for (int i=0;i<inArgs;i++){
	            	 inFieldTypes[i]=DataTypes.DOUBLE;
	             }
	         }
         }
         
         //parse function name
         functionName = params.get("function-name");
         if(functionName == null || functionName.trim().equals("")){
         	functionName = defaultFunctionName;
         }
         
         //parse windows size
         windowSize = Integer.parseInt(params.get("windows-size"));
         if(windowSize == null){
         	windowSize = new Integer(1);
         }
         
         
         //show in logger
         if(logger.isDebugEnabled()){
        	 logger.debug("Number of OUTPUT args configured to: " + outArgs);
        	 logger.debug("Number of INPUT args configured to: " + inArgs);
        	 logger.debug("Function NAME configured to: " + functionName);
        	 logger.debug("WINDOW SIZE configured to: " + windowSize);
         }
		
		
		MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
        .setUsePreviouslyControlledSession(true)
        .build();
		MatlabProxyFactory factory = new MatlabProxyFactory(options);
		try {
			proxy = factory.getProxy();
			proxy.eval("disp('initialize():coming from GSN!!!')");
			
			//processor = new MatlabTypeConverter(proxy);
		} catch (MatlabConnectionException e) {
			logger.warn(e);
		} catch (MatlabInvocationException e) {
			logger.warn(e);
		}
			    
		//initialize the circular buffer ARRAY
		circularBuffers = new Hashtable<String, ArrayBlockingQueue<StreamElement>>();
		
        success = true;
		return success;
	}
	
	
	
	
	
	
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
	        
	        String[] fieldname = streamElement.getFieldNames();
	        for (int n = 0; n < fieldname.length; n++){
	            // Build the window
	            double [] values = new double[windowSize];
	            StreamElement elt = null;
	            
	            // convert the circular buffer to an array
	            Object[] elts = circularBuffer.toArray();
	            for (int i = 0; i < elts.length; i++)
	            {
	              elt = (StreamElement) elts[i];
	              values[i] = ((Number) elt.getData()[n]).doubleValue(); //
	            }
	            
	        
	            try {
					proxy.setVariable("gsn_input_"+String.valueOf(n+1),(Object)values);
				} catch (MatlabInvocationException e) {
					logger.warn(e);
				}
	        }
	        
	        // Remove the oldest element from the queue!
	        try {
				circularBuffer.take();
			} catch (InterruptedException e) {
				logger.warn(e);
			}

	        //build matlabCommand
        	String matlabCommand = buildMatlabCommand(functionName, inArgs, outArgs);
        	if(logger.isDebugEnabled()){
        		logger.debug("Calling matlab engine with command: " + matlabCommand);
        	}
		    try {
				proxy.eval(matlabCommand);
				
				//Get the array from MATLAB:
				//case 1: need processor and MatlabNumericArray
			    //MatlabNumericArray array = processor.getNumericArray("gsn_out")
				
				
				//case 2
				double thisResult;
				ArrayList<Serializable> datas = new ArrayList<Serializable>();
				for(int i=0;i<outArgs;i++){
					thisResult = ((double[]) proxy.getVariable("gsn_output_"+String.valueOf(i+1)))[0];
					datas.add(thisResult);
				}
				
				
				if(logger.isDebugEnabled()){
					logger.debug("Received output from matlab: " + datas.toString());
				}
				
				
				StreamElement resultStream = new StreamElement(outFieldNames, outFieldTypes , datas.toArray(new Serializable[]{}));
				
				dataProduced(resultStream);
				} catch (MatlabInvocationException e) {
					logger.warn(e);
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
	
	public static String buildMatlabCommand(String fN, int inA, int outA){
		String retString="[";
		for (int i=0;i<outA;i++){
			retString+="gsn_output_"+String.valueOf(i+1);
			if (i<outA-1){
				retString+=",";
			}
		}
		retString+="] = " + fN + "(";
		for (int i=0;i<inA;i++){
			retString+="gsn_input_"+String.valueOf(i+1);
			if (i<inA-1){
				retString+=",";
			}
		}
		retString+=");";
		
		return retString;
	}
	
	

}
