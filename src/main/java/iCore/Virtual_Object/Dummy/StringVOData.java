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
public class StringVOData extends DummyVOData {

	String data = null;
	
	public StringVOData(String myName, String myData) {
		super(myName);
		data = myData;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String myData) {
		data = myData;
	}
}


