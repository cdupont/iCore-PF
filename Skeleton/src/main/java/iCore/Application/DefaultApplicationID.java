/**
* ============================== Header ============================== 
* file:          DefaultApplicationID.java
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

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class DefaultApplicationID implements ApplicationID {

	Integer appID = 0;
	
	public DefaultApplicationID(Integer myAppID) {
		appID = myAppID;
	}
	
	@Override
	public Integer getApplicationID() {
		return appID;
	}
	
	@Override
	public void setApplicationID(Integer myAppID) {
		appID = myAppID;
	}

	@Override
	public int compareTo(ApplicationID arg0) {
		return appID.compareTo(arg0.getApplicationID());
	}

}
