/**
* ============================== Header ============================== 
* file:          ApplicationAPI.java
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
package iCore.Application;

import iCore.Service.Service;
import iCore.Service.ServiceID;
import iCore.Service.ServiceReply;
import iCore.Service.ServiceRequest;

/**
 * iCore Application API.
 * in charge of forwarding application's requests to the iCore PF.
 *
 * @author cdupont
 */
public interface ApplicationAPI {
	
	public ApplicationID registerApplication(iCoreApplication app);
	
    public boolean unregisterApplication(ApplicationID appID);
   
    public ServiceReply serviceRequest(ApplicationID appID, ServiceRequest request);
    
    public boolean registerService(ApplicationID appID, Service service);
    
    public boolean unregisterService(ApplicationID appID, ServiceID serviceID);

	
   
}
