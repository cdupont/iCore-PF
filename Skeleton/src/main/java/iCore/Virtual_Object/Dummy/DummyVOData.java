/**
* ============================== Header ============================== 
* file:          DummyVOOuput.java
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
package iCore.Virtual_Object.Dummy;

import iCore.Virtual_Object.VOData;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummyVOData implements VOData {

	String VOName = null;
	
	public DummyVOData(String myName) {
		VOName = myName;
	}
		
	@Override
	public String getVOName() {
		return VOName;
	}
}
