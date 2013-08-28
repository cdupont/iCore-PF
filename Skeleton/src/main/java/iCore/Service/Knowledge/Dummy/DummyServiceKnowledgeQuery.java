/**
* ============================== Header ============================== 
* file:          ServiceKnowledgeQuery.java
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

import iCore.Service.Knowledge.ServiceKnowledgeQuery;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummyServiceKnowledgeQuery implements ServiceKnowledgeQuery{

	String query = null;
	
	public DummyServiceKnowledgeQuery(String myQuery) {
		query = myQuery;
	}
	
	@Override
	public String getQueryAsString() {
		return query;
	}
}
