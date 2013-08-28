/**
* ============================== Header ============================== 
* file:          Service.java
* project:       iCore
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
package iCore.Service.Dummy;

import iCore.Service.Service;
import iCore.Service.ServiceDescription;
import iCore.Service.ServiceOutput;

/**
 * A service (i.e. a foreign piece of software that iCore can call if needed)
 */
public class DummyService implements Service {
	
	String name = null;
	ServiceOutput output = null;
	ServiceDescription serviceDescription = null;
	
	DummyService(String myName, ServiceOutput myServiceOutput) {
		name = myName;
		output = myServiceOutput;
	}
	
	public String getName() {
		return name;
	}
	
	public ServiceOutput getServiceOutput() {
		return output;
	}
	
	public ServiceDescription getServiceDescription() {
		return serviceDescription;
	}
	
}
