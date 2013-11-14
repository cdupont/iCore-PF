/**
* ============================== Header ============================== 
* file:          VirtualObject.java
* project:       FIT4Green/iCore
* created:       29 juil. 2013 by cdupont
* 
* $LastChangedDate:$ 
* $LastChangedBy:$
* $LastChangedRevision:$
* 
* short description:
*   {To be completed}
* ============================= /Header ==============================
*/
package iCore.Virtual_Object.Dummy;

import iCore.Virtual_Object.VOData;
import iCore.Virtual_Object.VirtualObject;
import iCore.Virtual_Object.VirtualObjectModel;

/**
 * interface that a Virtual Object must implement
 * 
 *
 * @author cdupont
 */
public class DummyVirtualObject extends DummyAbstractVirtualObject implements VirtualObject, Runnable {

	Thread thread = null;
	boolean started = false;
	
	public DummyVirtualObject(String myName, VirtualObjectModel myVOModel) {
		super(myName, myVOModel);
		thread = new Thread(this);
	}
	
	
	@Override
	public boolean connectToDevice() {
		return true;
	}

	@Override
	public void run() {

		while(started) {
		
			VOData voData;
			if(name.equals("myVO1")) {
				voData = new IntegerVOData("myVO1", 22);
			} else {
				voData = new IntegerVOData("myVO2", 30);
			}
			getAVOOuput().publishData(voData);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void start() {
		if(!started) {
			started = true;
			thread.start();	
		}
		
	}

	@Override
	public void stop() {
		started = false;
	}


	@Override
	public String display() {
		return name;
	}
	
}
