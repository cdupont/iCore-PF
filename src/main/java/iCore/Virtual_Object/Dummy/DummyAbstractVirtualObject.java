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
package iCore.Virtual_Object.Dummy;

import iCore.Virtual_Object.AbstractVirtualObject;
import iCore.Virtual_Object.VOOutput;
import iCore.Virtual_Object.VirtualObjectModel;

/**
 * AbstractVirtualObject contains the minimal common interface that a VO and a CVO have in common 
 * 
 *
 * @author cdupont
 */
public abstract class DummyAbstractVirtualObject implements AbstractVirtualObject {

	protected String name = null;
	protected VOOutput voOutput = null;
	protected VirtualObjectModel voModel = null;
	
	public DummyAbstractVirtualObject(String myName, VirtualObjectModel myVOModel){
		name = myName;		
		voOutput = new VOOutput();
		voModel = myVOModel;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public VOOutput getAVOOuput() {
		return voOutput;
	}

	@Override
	public VirtualObjectModel getVOModel() {
		return voModel;
	}

	
}
