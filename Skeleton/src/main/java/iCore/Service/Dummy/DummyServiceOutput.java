/**
* ============================== Header ============================== 
* file:          ServiceOutput.java
* project:       FIT4Green/iCore
* created:       1 août 2013 by cdupont
* 
* $LastChangedDate:$ 
* $LastChangedBy:$
* $LastChangedRevision:$
* 
* short description:
*   {To be completed}
* ============================= /Header ==============================
*/
package iCore.Service.Dummy;

import iCore.Service.ServiceOutput;
import iCore.Virtual_Object.AbstractVirtualObject;
import iCore.Virtual_Object.VOOutput;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummyServiceOutput implements ServiceOutput {
	
	String name = null;
	AbstractVirtualObject AVO = null;
	
	public DummyServiceOutput(String myName, AbstractVirtualObject myAVO) {
		name = myName;
		AVO = myAVO;
	}
	
	public String getServiceName() {
		return name;
	}
	
	public AbstractVirtualObject getServiceOutput() {
		return AVO;
	}

}
