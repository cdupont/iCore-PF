package gsn.vsensor;

import gsn.beans.DataTypes;
import gsn.beans.StreamElement;
import gsn.beans.VSensorConfig;
import gsn.utils.MatlabEngine;

import java.io.IOException;
import java.io.Serializable;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import matlabcontrol.*;

public class MatlabVS2 extends AbstractVirtualSensor {

	private final static transient Logger      logger         = Logger.getLogger( AbstractVirtualSensor.class );
	
	
	private String[] fieldNames = {"matlab_output"};
	private Byte[] fieldTypes = {DataTypes.DOUBLE};
	
	private String functionName, defaultFunctionName = "myGSNMatlabFunction";
	private Integer inArgs;
	private Double[] parameters;
	
	private  MatlabProxy proxy;
	
	/* (non-Javadoc)
	 * @see gsn.vsensor.AbstractVirtualSensor#dataAvailable(java.lang.String, gsn.beans.StreamElement)
	 */
	@Override
	public void dataAvailable(String inputStreamName, StreamElement streamElement) {
	
		//if(streamElement.getFieldTypes().length == inArgs+1)
		if(streamElement.getFieldTypes().length == inArgs)
			for(int i = 0; i < inArgs; i++)
				parameters[i] = (Double) streamElement.getData()[i];
		Double answer;
		String matlabCommand = functionName + "(" ;
		for(int i = 0; i < inArgs; i++) {
			matlabCommand = matlabCommand + parameters[i].toString();
			if(i != inArgs-1)
				matlabCommand = matlabCommand +",";
		}
		if(inArgs > 0)
			matlabCommand = matlabCommand + ")";
		if(logger.isDebugEnabled())
			logger.debug("Calling matlab engine with command: " + matlabCommand);
		try {
			if (proxy.isConnected()){
				proxy.eval("disp('dataAvailable():coming from GSN!!!')");
				Object realObject=proxy.returningEval(matlabCommand,1)[0];
				double[] realValues = (double[]) realObject;
	
				String matlabAnswer=String.valueOf(realValues[0]);
				if(logger.isDebugEnabled()){
					logger.debug("Received output from matlab: " + matlabAnswer +". Trying to interpret this answer as a Java Double object.");
				}
				answer = Double.parseDouble(matlabAnswer);
				StreamElement result = new StreamElement(fieldNames, fieldTypes , new Serializable[] {answer});
				dataProduced(result);
			}
			else{
				logger.warn("Proxy is disconnected!!!");
			}
		} catch (MatlabInvocationException e) {
			logger.warn(e);
		}
	}

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
		//engine = new MatlabEngine();
		
		MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
        .setUsePreviouslyControlledSession(true)
        .build();
		MatlabProxyFactory factory = new MatlabProxyFactory(options);
		try {
			proxy = factory.getProxy();
			proxy.eval("disp('initialize():coming from GSN!!!')");
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
