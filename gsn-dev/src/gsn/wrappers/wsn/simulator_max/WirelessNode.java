package gsn.wrappers.wsn.simulator_max;

public class WirelessNode extends Thread implements WirelessNodeInterface {
   
   int                  sleepingTime = 5000;
   
   boolean              alive        = true;
   
   private WirelessNode parent;
   
   private DataListener dataListener = null;
   
   public int getIdentifier ( ) {
      return identity;
   }
   
   private int identity;
   
   public WirelessNode ( int identifier ) {
      this.identity = identifier;
   }
   
   // public WirelessNode ( int identifier, ) {
   // this.identity = identifier;
   // }
   //
   protected void setParent ( WirelessNode parent ) {
      this.parent = parent;
   }
   
   public WirelessNode getParent ( ) {
      return parent;
   }
   
   public void run ( ) {
      while ( alive ) {
         try {
            Thread.sleep( sleepingTime );
         } catch ( InterruptedException e ) {
            e.printStackTrace( ); // To change body of catch statement use
            // File | Settings | File Templates.
         }
         //int temperature = ( int ) ( Math.random( ) * 100 );
         //DataPacket dataPacket = new DataPacket( identity , ( parent == null ) ? -1 : parent.getIdentifier( ) , temperature , DataPacket.ROUTING_AND_DATA_PACKET );
         
         int Min=0;
         int Max=(int)(Math.pow(2, 14)-1);
         int t= Min + (int)(Math.random() * ((Max - Min) + 1));
         DataPacket dataPacket = new DataPacket( identity , ( parent == null ) ? -1 : parent.getIdentifier( ) , t , DataPacket.ROUTING_AND_DATA_PACKET );
         
         send( dataPacket );
      }
   }
   
   public void send ( DataPacket e ) {
      if ( parent != null ) {
         parent.send( e );
      } else {
         if ( dataListener != null ) dataListener.newDataAvailable( e );
         else {
            System.out.println( "Data received by the wireless node with the id of = " + identity );
         }
      }
   }
   
   public void stopNode ( ) {
      alive = false;
   }
   
   public void addDataListener ( DataListener wsnsWrapper ) {
      this.dataListener = wsnsWrapper;
   }
}
