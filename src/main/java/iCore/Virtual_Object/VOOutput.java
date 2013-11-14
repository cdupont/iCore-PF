/**
* ============================== Header ============================== 
* file:          VOOutput.java
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
package iCore.Virtual_Object;

import java.util.ArrayList;
import java.util.Collection;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class VOOutput {
		
	public interface VOOutputSubscriber {
		void dataAvailable(VOData data);
	}
	
	Collection<VOOutputSubscriber> subscribers = null;
	
	public VOOutput() {
		subscribers = new ArrayList<VOOutputSubscriber>();
	}
	
	public boolean subscribe(VOOutputSubscriber subscriber) {
		return subscribers.add(subscriber);
	}
	
	public void publishData(VOData data) {
		for(VOOutputSubscriber subscriber : subscribers) {
			subscriber.dataAvailable(data);
		}
	}
}
