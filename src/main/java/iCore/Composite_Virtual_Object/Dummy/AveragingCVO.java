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

import iCore.Composite_Virtual_Object.CVOProcess;
import iCore.Composite_Virtual_Object.CompositeVirtualObject;
import iCore.Virtual_Object.AbstractVirtualObject;
import iCore.Virtual_Object.VOData;
import iCore.Virtual_Object.VOOutput.VOOutputSubscriber;
import iCore.Virtual_Object.VirtualObjectModel;
import iCore.Virtual_Object.Dummy.DummyAbstractVirtualObject;
import iCore.Virtual_Object.Dummy.IntegerVOData;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class AveragingCVO extends MemoryCVO {

	
	public AveragingCVO(String myName, VirtualObjectModel myVOModel, CVOProcess process) {
		super(myName, myVOModel, process);
	}


	@Override
	public void compute() {
	
		Integer count = 0;
		Integer sum = 0;
		for(VOData value : lastValues.values()) {
			
			if(value != null && value instanceof IntegerVOData) {
				count++;
				IntegerVOData integerValue = (IntegerVOData) value;
				sum += integerValue.getData();
			}
		}
		if(count!=0) {
			VOData data = new IntegerVOData(getName(), sum/count);
			getAVOOuput().publishData(data);	
		}		
		
	}
	
}
