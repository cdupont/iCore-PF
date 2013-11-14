/**
* ============================== Header ============================== 
* file:          DummyServiceKnowledgeDB.java
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
package iCore.Service.Knowledge.Dummy;

import java.util.Collection;

import iCore.Service.Knowledge.ServiceKnowledge;
import iCore.Service.Knowledge.ServiceKnowledgeDB;
import iCore.Service.Knowledge.ServiceKnowledgeQuery;
import iCore.Service.Knowledge.ServiceKnowledgeResponse;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummyServiceKnowledgeDB implements ServiceKnowledgeDB{

	Collection<ServiceKnowledge> serviceKnowledges = null;
	
	@Override
	public void addKnowledge(ServiceKnowledge knlg) {
		serviceKnowledges.add(knlg);
		
	}

	@Override
	public ServiceKnowledgeResponse queryKnowledge(ServiceKnowledgeQuery query) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
