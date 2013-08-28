/**
* ============================== Header ============================== 
* file:          ServiceKnowledgeResponse.java
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

import iCore.Service.Knowledge.ServiceKnowledgeResponse;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummyServiceKnowledgeResponse implements ServiceKnowledgeResponse {

	String response = null;
	
	public DummyServiceKnowledgeResponse(String myResponse) {
		response = myResponse;
	}
	
	@Override
	public String getResponseAsString() {
		return response;
	}
	
}
