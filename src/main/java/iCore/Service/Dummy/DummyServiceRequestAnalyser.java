/**
* ============================== Header ============================== 
* file:          ServiceRequestAnalyser.java
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
package iCore.Service.Dummy;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

import iCore.Composite_Virtual_Object.Dummy.AveragingCVO;
import iCore.Monitor.Monitor;
import iCore.Service.Service;
import iCore.Service.ServiceReply;
import iCore.Service.ServiceRequest;
import iCore.Service.ServiceRequestAnalyser;
import iCore.Service.Knowledge.ServiceKnowledgeDB;
import iCore.Service.Language.AverageSE;
import iCore.Service.Language.ServiceExpression;
import iCore.Service.Language.TemperatureSensorSE;
import iCore.Virtual_Object.AbstractVirtualObject;
import iCore.Virtual_Object.VOManager;
import iCore.Virtual_Object.VirtualObjectModel;
import iCore.Virtual_Object.Dummy.DummyVODiscoveryRequest;
import iCore.Virtual_Object.Dummy.DummyVOType;
import iCore.Virtual_Object.Dummy.DummyVirtualObjectModel;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummyServiceRequestAnalyser implements ServiceRequestAnalyser {

	ServiceKnowledgeDB knowledgeDB = null;
	
	public DummyServiceRequestAnalyser(ServiceKnowledgeDB myKnowledgeDB) {
		knowledgeDB = myKnowledgeDB;		
	}
	
	public ServiceReply analyse(ServiceRequest serviceRequest) {
		
		DummyServiceReply serviceReply;
		
		if(serviceRequest instanceof DummyServiceRequest) {
			DummyServiceRequest dummyServiceRequest = (DummyServiceRequest) serviceRequest;
								
			Collection<AbstractVirtualObject> VODiscovered = evaluate(dummyServiceRequest.getServiceExpression());
				
			if(VODiscovered.size() > 0) {
				AbstractVirtualObject firstVO = Iterables.getFirst(VODiscovered, null);
					
				System.out.println("iCore: CVO found that fulfills the service request: \n" + firstVO.display() );
					
				DummyServiceOutput serviceOuput = new DummyServiceOutput(dummyServiceRequest.getName() + "_Output", firstVO);
					
				Service service = new DummyService(dummyServiceRequest.getName() + "_Service", serviceOuput);
				
				serviceReply = new DummyServiceReply(Optional.of(service));	
				
			} else {
				//TODO throw an exception instead?
				serviceReply = new DummyServiceReply(Optional.<Service>absent());
			}
				
		} else {
				
			serviceReply = new DummyServiceReply(Optional.<Service>absent());
		}
		return serviceReply;
	}
	
	
	//TODO make generic
	public <T> Collection<AbstractVirtualObject> evaluate(ServiceExpression<T> myServiceExpression) {
		
		Collection<AbstractVirtualObject> AVOs = new ArrayList<AbstractVirtualObject>();
		
		//TODO: change this case analysis for more type-safe (for ex. Visitor pattern).
		if(myServiceExpression instanceof AverageSE) {
			AverageSE averageSE = (AverageSE)myServiceExpression;
			
			Collection<AbstractVirtualObject> averagedVOs = evaluate(averageSE.getValues());
			
			AveragingCVO averagingCVO = new AveragingCVO("average", new DummyVirtualObjectModel("CVO", DummyVOType.Temperature), null);
		
			for(AbstractVirtualObject avo : averagedVOs) {
				averagingCVO.addAbstractVOs(avo);				
			}
		
			AVOs.add(averagingCVO);
		}
		if(myServiceExpression instanceof TemperatureSensorSE) {
			
			//TODO add location search
			TemperatureSensorSE tempSE = (TemperatureSensorSE)myServiceExpression;
			String loc = tempSE.getLocation();
			
			DummyVODiscoveryRequest voDiscoveryRequest = new DummyVODiscoveryRequest(DummyVOType.Temperature);
			Collection<VirtualObjectModel> VOMDiscovered = VOManager.getVORegistry().VODiscovery(voDiscoveryRequest);
			
			if(VOMDiscovered.size() > 0) {
				for(VirtualObjectModel avo : VOMDiscovered) {
					Optional<AbstractVirtualObject> opVO = VOManager.getVOContainer().getVOByName(avo.getVOName());
				
					if(opVO.isPresent()) {
						AVOs.add(opVO.get());
					}
				}
			}
		}
		return AVOs;
				
	}
	
}
