/**
* ============================== Header ============================== 
* file:          CompositeVirtualObject.java
* project:       FIT4Green/iCore
* created:       31 juil. 2013 by cdupont
* 
* $LastChangedDate:$ 
* $LastChangedBy:$
* $LastChangedRevision:$
* 
* short description:
*   {To be completed}
* ============================= /Header ==============================
*/
package iCore.Composite_Virtual_Object;

import iCore.Service.Knowledge.ServiceKnowledgeDB;


/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public interface SituationObserverCVO extends CompositeVirtualObject{
	
	public void updateServiceKnowledge(ServiceKnowledgeDB knowledgeDB);
	
}
