package gsn.vsensor;

import gsn.beans.DataTypes;
import gsn.beans.StreamElement;
import gsn.wrappers.general.SerialWrapper;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * description= First implementation of a libelium virtual sensor
 * author=		Massimo Vecchio
 * version=		1.0
 */

public class WaspmoteVS extends AbstractVirtualSensor {
   
   private static final transient Logger logger = Logger.getLogger( WaspmoteVS.class );
   
   
   private static final String DEFAULT_NODEID = "id";
   private static final String DEFAULT_X = "x";
   private static final String DEFAULT_Y = "y";
   private static final String DEFAULT_Z = "z";
   private static final String DEFAULT_TEMP = "temperature";
   private static final String DEFAULT_BAT = "battaery";
   
   private static final int NULL_INT = Short.MIN_VALUE;
   private static final double NULL_DOUBLE=Double.MIN_VALUE;
   
   int id;
   int x;
   int y;
   int z;
   double temp;
   double bat;
   
   
   private String[] fieldNames = {"ID","X","Y","Z","TEMPERATURE","BATTERY"};
   private Byte[] fieldTypes = {DataTypes.INTEGER, DataTypes.INTEGER, DataTypes.INTEGER, DataTypes.INTEGER ,DataTypes.DOUBLE, DataTypes.DOUBLE};
   
   private Serializable [ ] outputData = new Serializable [ fieldNames.length ];
   
   public boolean initialize ( ) {
      
      return true;
   }
   
   public void dataAvailable ( String inputStreamName , StreamElement data ) {
      if ( logger.isDebugEnabled( ) ){
    	  logger.debug( "SERIAL RAW DATA :"+new String((byte[])data.getData(SerialWrapper.RAW_PACKET)));
      }
      
      
      id=NULL_INT;
      x=NULL_INT;
      y=NULL_INT;
      z=NULL_INT;
      temp=NULL_DOUBLE;
      bat=NULL_DOUBLE;
      
      
      //raw data from serial
      String s = new String( ( byte [ ] ) data.getData( SerialWrapper.RAW_PACKET ) );
      
      //really bad: look at 2 % sign
      int count = StringUtils.countMatches(s, "%");
      if (count<2){
    	  if ( logger.isDebugEnabled( ) ){
        	  logger.debug( "INCOMPLETE PACKET!!!!!");
          }
    	  return;
      }
      
      String d4 = "[\\*%]"; //  \\n
      String [ ] tkns=s.split(d4);
      
      
      
      String[] couples;
	  for (int i=0;i<tkns.length;i++){
			couples= tkns[i].split("[ :]");
			if(couples.length==2){
				//System.out.println(couples[0]+"="+couples[1]);
				if (couples[0].equals("id")){
					try{
						id=Integer.valueOf(couples[1]);
						}catch (NumberFormatException nfe) {     
							logger.debug( "ID is not a number!!");
						 }catch (Exception e){
							 logger.debug("Generic exception caught on ID");
						 }
				}
				else if (couples[0].equals("x")){
					try{
						x=Integer.valueOf(couples[1]);
						}catch (NumberFormatException nfe) {     
							logger.debug( "X is not a number!!");
						 }catch (Exception e){
							 logger.debug("Generic exception caught on X");
						 }
				}
				else if (couples[0].equals("y")){
					try{
					y=Integer.valueOf(couples[1]);
					}catch (NumberFormatException nfe) {     
						logger.debug( "Y is not a number!!");
					 }catch (Exception e){
						 logger.debug("Generic exception caught on Y");
					 }
				}
				else if (couples[0].equals("z")){
					try{
						z=Integer.valueOf(couples[1]);
						}catch (NumberFormatException nfe) {     
							logger.debug( "Z is not a number!!");
						 }catch (Exception e){
							 logger.debug("Generic exception caught on z");
						 }
				}
				else if (couples[0].equals("temp")){
					try{
						temp=Double.valueOf(couples[1]);
						}catch (NumberFormatException nfe) {     
							logger.debug( "TEMP is not a number!!");
						 }catch (Exception e){
							 logger.debug("Generic exception caught on TEMP");
						 }
				}
				else if (couples[0].equals("bat")){
					try{
						bat=Double.valueOf(couples[1]);
						}catch (NumberFormatException nfe) {     
							logger.debug( "BAT is not a number!!");
						 }catch (Exception e){
							 logger.debug("Generic exception caught on BAT");
						 }
				}
			}
	  }
	  
	  if (id==NULL_INT){
		  if ( logger.isDebugEnabled( ) ){
        	  logger.debug( "MISSING NODE-ID:PACKET DISCARDED!!!!!");
          }
		  return;
	  }
      //here data are produced!
            
	  if ( logger.isDebugEnabled( ) ){
		  logger.debug( "ID=" + id + " X=" + x + " Y=" + y + " Z=" + z + " TEMP=" + temp + " BAT=" + bat);
	  }
        
        //send back the data
        outputData[ 0 ] = id;
        outputData[ 1 ] = x;
        outputData[ 2 ] = y;
        outputData[ 3 ] = z;
        outputData[ 4 ] = temp;
        outputData[ 5 ] = bat;
        StreamElement output = new StreamElement( fieldNames , fieldTypes , outputData , System.currentTimeMillis( ) );
        dataProduced( output );
         
   }
   
   public void dispose ( ) {
   }
   
}
