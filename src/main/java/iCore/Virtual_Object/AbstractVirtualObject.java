/**
* ============================== Header ============================== 
* file:          VirtualObject.java
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
package iCore.Virtual_Object;

/**
 * AbstractVirtualObject contains the minimal common interface that a VO and a CVO have in common 
 * 
 *
 * @author cdupont
 */
public interface AbstractVirtualObject {

	String getName();
	VOOutput getAVOOuput();
	VirtualObjectModel getVOModel();
	void start();
	void stop();
	String display();
}
