package examples;

import static iCore.Service.Language.ServiceLanguage.average;
import static iCore.Service.Language.ServiceLanguage.temperatureSensors;
import iCore.Main;
import iCore.Application.ApplicationAPI;
import iCore.Application.ApplicationID;
import iCore.Application.Dummy.DummyApplicationAPI;
import iCore.Application.iCoreApplication;
import iCore.Service.Service;
import iCore.Service.ServiceOutput;
import iCore.Service.ServiceReply;
import iCore.Service.ServiceRequest;
import iCore.Service.Dummy.DummyServiceOutput;
import iCore.Service.Dummy.DummyServiceRequest;
import iCore.Service.Language.ServiceExpression;
import iCore.Virtual_Object.VOData;
import iCore.Virtual_Object.VOOutput.VOOutputSubscriber;
import iCore.Virtual_Object.Dummy.DummyVOData;
import iCore.Virtual_Object.Dummy.DummyVOType;
import iCore.Virtual_Object.Dummy.IntegerVOData;
import iCore.Virtual_Object.Dummy.StringVOData;


/**
 * ============================== Header ============================== 
 * file:          Application_Example.java
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

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public final class Application_Example {

	static class MyApp implements iCoreApplication {

		@Override
		public String getName() {
			return "myApp";
		}

		@Override
		public void ServiceDataAvailable(ServiceOutput serviceOutput) {
			System.out.println("data available from service: " + serviceOutput.getServiceName());
		}						
	}

	class myVOOutputSubscriber implements VOOutputSubscriber {
		
		@Override
		public void dataAvailable(VOData data) {
			if(data instanceof IntegerVOData) {
				System.out.println("Application_Example: data available from service: " + ((IntegerVOData)data).getData());				
			}
		}		
	}
		
	public void performRequest() {
	
		ApplicationAPI iCoreAppAPI = new DummyApplicationAPI();
		MyApp app = new MyApp();
		System.out.println("Application_Example: Registering application...");
		ApplicationID appID = iCoreAppAPI.registerApplication(app);
		
		System.out.println("Application_Example: Performing service request");
		ServiceExpression<Integer> se = average(temperatureSensors("Room A"));
		
		ServiceRequest serviceRequest = new DummyServiceRequest(se);
		ServiceReply serviceReply = iCoreAppAPI.serviceRequest(appID, serviceRequest);
		
		if(serviceReply.getService().isPresent()) {
			
		   System.out.println("Application_Example: Service returned by iCore, registering on its output: ");
		   Service service = serviceReply.getService().get();
		   iCoreAppAPI.registerService(appID, service);
		   
		   if(service.getServiceOutput() instanceof DummyServiceOutput) {
			   DummyServiceOutput output = (DummyServiceOutput)service.getServiceOutput();
			   
			   //TODO: to move?
			   output.getServiceOutput().getAVOOuput().subscribe(new myVOOutputSubscriber());
			   
		   }
		}
		
	}
}
