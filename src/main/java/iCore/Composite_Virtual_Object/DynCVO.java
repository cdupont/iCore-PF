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


/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public interface DynCVO extends CompositeVirtualObject{
	
	DynCVOQuery getCVOQuery();
	
	void updateInputVOs();
}
