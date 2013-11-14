/**
* ============================== Header ============================== 
* file:          VODiscoveryRequest.java
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

import iCore.Virtual_Object.VODiscoveryRequest;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DummyVODiscoveryRequest implements VODiscoveryRequest{

	DummyVOType request = null;
	
	public DummyVODiscoveryRequest(DummyVOType myRequest) {
		request = myRequest;
	}

	public DummyVOType getVOType() {
		return request;
	}
}
