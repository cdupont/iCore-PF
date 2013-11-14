/**
* ============================== Header ============================== 
* file:          ServiceKnowledge.java
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

import iCore.Service.Knowledge.ServiceKnowledge;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummySKIsIn implements ServiceKnowledge {

	String token1;
	String token2;
		
	public DummySKIsIn(String myToken1, String myToken2) {
		token1 = myToken1;
		token2 = myToken2;
	}
	
	String getContainer() {
		return token1;
	}
	
	String getContainee() {
		return token2;
	}
}
