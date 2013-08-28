/**
* ============================== Header ============================== 
* file:          ServiceManager.java
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
package iCore.Service;

import iCore.Application.ApplicationID;
import iCore.Service.Dummy.DummyServiceOutput;
import iCore.Service.Knowledge.ServiceKnowledgeDB;
import iCore.Service.Knowledge.Dummy.DummyServiceKnowledgeDB;

/* {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class ServiceManager extends Thread {

	ServiceRequestAnalyser analyser = null;
	ServiceStorage storage = null;
	ServiceKnowledgeDB knowledgeDB = null;
	
	public ServiceManager(ServiceStorage myStorage, ServiceRequestAnalyser myAnalyser) {
		knowledgeDB = new DummyServiceKnowledgeDB();
		analyser = myAnalyser;
		storage = myStorage;
	}
	
    @Override
    public void run() {
        
    }
	
    public ServiceReply serviceRequest(ServiceRequest request) {
    	System.out.println("iCore: Service request received");
    	return analyser.analyse(request);
    }
	
    public boolean registerService(ApplicationID app, Service service) {
    	
    	startService(service);
    	return storage.storeService(service);
    }
    
    public boolean unregisterService(ApplicationID app, ServiceID service) {
    	return false;
    }
    
    public void startService(Service service) {
    	if(service.getServiceOutput() instanceof DummyServiceOutput) {
    		DummyServiceOutput output = (DummyServiceOutput)service.getServiceOutput();
    		
    		output.getServiceOutput().start();
    	}
    }
    
}
