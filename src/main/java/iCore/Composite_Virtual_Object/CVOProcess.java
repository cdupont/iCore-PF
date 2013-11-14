/**
* ============================== Header ============================== 
* file:          CVOProcess.java
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

import java.util.Collection;

import iCore.Virtual_Object.VOOutput;
import iCore.Virtual_Object.AbstractVirtualObject;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public interface CVOProcess {

	//process a VO ouput from a list on abstract VOs
	VOOutput process(Collection<AbstractVirtualObject> AVOs);
}
