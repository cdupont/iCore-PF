/**
* ============================== Header ============================== 
* file:          VirtualObjectConfig.java
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
package iCore.Virtual_Object.Dummy;

import iCore.Virtual_Object.VirtualObjectModel;

/**
 * Contains the VO Model
 * 
 *
 * @author cdupont
 */
public class DummyVirtualObjectModel implements VirtualObjectModel {
	
	String name = null;
	DummyVOType voType = null;


	public DummyVirtualObjectModel(String myName, DummyVOType myVOType) {
		name = myName;
		voType = myVOType;
	}
	
	
	public String getVOName() {
		return name;
	}
	

	public DummyVOType getVoType() {
		return voType;
	}


	public void setVoType(DummyVOType voType) {
		this.voType = voType;
	}
	

}
