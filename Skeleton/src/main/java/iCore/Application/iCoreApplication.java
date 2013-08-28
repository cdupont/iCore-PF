/**
* ============================== Header ============================== 
* file:          Application.java
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
package iCore.Application;

import iCore.Service.ServiceOutput;


/**
 * An interface representing an application. 
 * An application can register with iCore, and issue requests.
 *
 * @author cdupont
 */
public interface iCoreApplication {

	String getName();
	
	void ServiceDataAvailable(ServiceOutput serviceOutput);
	
}
