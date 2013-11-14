/**
* ============================== Header ============================== 
* file:          DummyServiceReply.java
* project:       FIT4Green/iCore
* created:       30 juil. 2013 by cdupont
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

import iCore.Service.ServiceReply;
import com.google.common.base.Optional;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummyServiceReply implements ServiceReply {

	String name = null;
	Optional<Service> service = Optional.absent(); 
	
	public DummyServiceReply(Optional<Service> myService) {
		service = myService;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String myName) {
		name = myName;
	}

	@Override
	public Optional<Service> getService() {
		return service;
	}
}
