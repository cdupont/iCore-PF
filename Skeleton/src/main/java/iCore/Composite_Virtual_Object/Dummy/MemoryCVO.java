/**
* ============================== Header ============================== 
* file:          CompositeVirtualObject.java
* project:       FIT4Green/iCore
* created:       31 juil. 2013 by cdupont
* 
* $LastChangedDate:$ 
* $LastChangedBy:$
* $LastChangedRevision:$
* 
* short description:
*   {To be completed}
* ============================= /Header ==============================
*/
package iCore.Composite_Virtual_Object.Dummy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import iCore.Composite_Virtual_Object.CVOProcess;
import iCore.Composite_Virtual_Object.CompositeVirtualObject;
import iCore.Virtual_Object.AbstractVirtualObject;
import iCore.Virtual_Object.VOData;
import iCore.Virtual_Object.VOOutput.VOOutputSubscriber;
import iCore.Virtual_Object.VirtualObjectModel;
import iCore.Virtual_Object.Dummy.DummyAbstractVirtualObject;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public abstract class MemoryCVO extends DummyCVO {

	//a map to keep the last value of each referenced VOs
	Map<String, VOData> lastValues = null;
	
	public MemoryCVO(String myName, VirtualObjectModel myVOModel, CVOProcess process) {
		super(myName, myVOModel, process);
		AVOs = new ArrayList<AbstractVirtualObject>();
		lastValues = new HashMap<String, VOData>();
	}

	public class MySubscriber implements VOOutputSubscriber {
		
		@Override
		public void dataAvailable(VOData data) {
			lastValues.put(data.getVOName(), data);
			compute();
		}
		
	}
	
	//Subscribe to each referenced VOs
	@Override
	public void start() {
		
		for(AbstractVirtualObject AVO : AVOs) 
		{
			MySubscriber subscriber = new MySubscriber();
			AVO.getAVOOuput().subscribe(subscriber);
			AVO.start();
		}
	}
	
	public abstract void compute();

	
	@Override
	public void stop() {
		
		//TODO unsubscribe all AVOs
		for(AbstractVirtualObject AVO : AVOs) 
		{
			AVO.stop();
		}
	}
		
}
