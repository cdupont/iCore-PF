/**
* ============================== Header ============================== 
* file:          ServiceReply.java
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
package iCore.Service;

import com.google.common.base.Optional;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */

/*
 * A service reply contains:
 * - a name
 * - optionally a service 
 */
public interface ServiceReply {
	
	String getName();
	Optional<Service> getService();
}
