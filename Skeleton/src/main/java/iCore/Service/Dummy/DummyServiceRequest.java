/**
* ============================== Header ============================== 
* file:          DummyServiceRequest.java
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

import iCore.Service.ServiceRequest;
import iCore.Service.Language.ServiceExpression;
import iCore.Virtual_Object.Dummy.DummyVOType;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummyServiceRequest implements ServiceRequest {

	String name = null;
	ServiceExpression<Integer> serviceExpression = null;
	
	public DummyServiceRequest(ServiceExpression<Integer> myServiceExpression) {
		
		serviceExpression = myServiceExpression;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String myName) {
		name = myName;
	}
	

	public ServiceExpression<Integer> getServiceExpression() {
		return serviceExpression;
	}

	public void setServiceExpression(ServiceExpression<Integer> serviceExpression) {
		this.serviceExpression = serviceExpression;
	}

}
