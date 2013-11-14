/**
* ============================== Header ============================== 
* file:          Application.java
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
package iCore.Application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import iCore.Service.ServiceManager;
import iCore.Service.ServiceReply;
import iCore.Service.ServiceRequest;
import iCore.Virtual_Object.Dummy.DummyVOData;

/**
 * interface for applications
 * 
 *
 * @author cdupont
 */
public class ApplicationManager extends Thread {
	
	Map<ApplicationID, iCoreApplication> applications = null;
	ServiceManager serviceManager = null;
	
	public ApplicationManager(ServiceManager myServiceManager) {
		serviceManager = myServiceManager;
		applications = new HashMap<ApplicationID, iCoreApplication>();
	}
	
	public ApplicationID registerApplication(iCoreApplication app) {
		ApplicationID appID = getNewApplicationID();
		applications.put(appID, app);
		System.out.println("iCore: Application registered: " + app.getName());
		return appID;
	}
	
    public boolean unregisterApplication(ApplicationID appID) {
		return true;
	}
	
    ServiceReply serviceRequest(ServiceRequest request) {
		return null;
	}
	    
    @Override
    public void run() {
        //go through all applications
    }
    
    ApplicationID getNewApplicationID() {
    	ApplicationID appID = null;
    	Set<ApplicationID> appIDs = applications.keySet();
    	ApplicationID maxAppId; 
    	if(applications.size() > 0) {
    		maxAppId = Collections.max(appIDs);	
    		appID = new DefaultApplicationID(maxAppId.getApplicationID() + 1);
    	} else {
    		appID = new DefaultApplicationID(0);
    	}
    	
    	return appID;
    }
}
