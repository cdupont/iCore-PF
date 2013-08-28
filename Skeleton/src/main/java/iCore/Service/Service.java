/**
* ============================== Header ============================== 
* file:          Service.java
* project:       iCore
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

/**
 * A service (i.e. a foreign piece of software that iCore can call if needed)
 */
public interface Service {
	
	public String getName();
	public ServiceOutput getServiceOutput();
	public ServiceDescription getServiceDescription();
	
}
