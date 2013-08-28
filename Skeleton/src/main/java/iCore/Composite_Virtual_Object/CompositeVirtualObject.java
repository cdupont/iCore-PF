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


import iCore.Virtual_Object.AbstractVirtualObject;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public interface CompositeVirtualObject extends AbstractVirtualObject{
	
	boolean addAbstractVOs(AbstractVirtualObject AVO);
	
	public void setProcess(CVOProcess process);
	CVOProcess getProcess();
	
	
}
