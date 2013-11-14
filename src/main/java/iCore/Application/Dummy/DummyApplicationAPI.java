/**
* ============================== Header ============================== 
* file:          DummyApplicationAPI.java
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
package iCore.Application.Dummy;

import iCore.Application.ApplicationAPI;
import iCore.Application.ApplicationID;
import iCore.Application.iCoreApplication;
import iCore.Monitor.Monitor;
import iCore.Service.Service;
import iCore.Service.ServiceID;
import iCore.Service.ServiceReply;
import iCore.Service.ServiceRequest;

/**
 * Dummy application API with direct connection to iCore PF
 * provided just as an example, real implementation should use a communication method like
 * REST, RMI, sockets...
 *
 * @author cdupont
 */
public class DummyApplicationAPI implements ApplicationAPI {
	
	public DummyApplicationAPI() {
	}
	
	@Override
	public ApplicationID registerApplication(iCoreApplication app) {
		return Monitor.getApplicationManager().registerApplication(app);
	}

	@Override
	public boolean unregisterApplication(ApplicationID appID) {
		return Monitor.getApplicationManager().unregisterApplication(appID);
	}

	@Override
	public ServiceReply serviceRequest(ApplicationID appID, ServiceRequest request) {
		return Monitor.getServiceManager().serviceRequest(request);
	}

	@Override
	public boolean registerService(ApplicationID appID, Service service) {
		return Monitor.getServiceManager().registerService(appID, service);
	}

	@Override
	public boolean unregisterService(ApplicationID appID, ServiceID serviceID) {
		return Monitor.getServiceManager().unregisterService(appID, serviceID);
	}

	

}
