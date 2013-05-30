package gsn.wrappers.wsn.simulator_max;

public class DataPacket {
	
	private static final int NO_VALUE = Short.MIN_VALUE;
   
   private int             parent;
   
   private int             identifier;
   
   private int             value;
   
   private int             typeOfPacket;
   
   
   public static final int ROUTING_AND_DATA_PACKET    = 1;
   
   public static final int ROUTING_WITHOUT_DATA       = 2;
   
   public static final int TEMPERATURE_REQUEST_PACKET = 3;
   
   public int getTypeOfPacket ( ) {
      return typeOfPacket;
   }
   
   public DataPacket ( int identifier , int parent , int temperature , int typeOfThePacket ) {
      this.identifier = identifier;
      this.parent = parent;
      this.value = temperature;
      
      this.typeOfPacket = typeOfThePacket;
   }
   
   public int getParent ( ) {
      return parent;
   }
   
   public int getIdentifier ( ) {
      return identifier;
   }
   
   public int getValue ( ) {
      return value;
   }
   
   public double getRealValue ( ) {
	      return getTemperature(value);
   }
   
   public double getTemperature(int rawValue) {
       if (rawValue >=0) {
           return (rawValue * 0.01) - 39.6;
       } else {
           return NO_VALUE;
       }
   }
   
   public String toString ( ) {
      return "DataPacket{" + "parent=" + parent + ", identifier=" + identifier + ", value=" + value + '}';
   }
   
   
   
}
