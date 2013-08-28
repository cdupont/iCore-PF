package examples;

import iCore.Main;
import iCore.Application.ApplicationAPI;
import iCore.Application.ApplicationID;
import iCore.Application.Dummy.DummyApplicationAPI;
import iCore.Application.iCoreApplication;
import iCore.Monitor.Monitor;
import iCore.Service.ServiceOutput;
import iCore.Service.ServiceReply;
import iCore.Service.ServiceRequest;
import iCore.Virtual_Object.VirtualObjectModel;
import iCore.Virtual_Object.Dummy.DummyVOType;
import iCore.Virtual_Object.Dummy.DummyVirtualObject;
import iCore.Virtual_Object.Dummy.DummyVirtualObjectModel;


/**
 * ============================== Header ============================== 
 * file:          Application_Example.java
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

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public final class VO_Installer {

	public static void installVO() {

		VirtualObjectModel myVOModel1 = new DummyVirtualObjectModel("myVO1", DummyVOType.Temperature);
		DummyVirtualObject myVO1 = new DummyVirtualObject("myVO1", myVOModel1);
		VirtualObjectModel myVOModel2 = new DummyVirtualObjectModel("myVO2", DummyVOType.Temperature);
		DummyVirtualObject myVO2 = new DummyVirtualObject("myVO2", myVOModel2);
		    
		Monitor.getVOManager().addVO(myVO1);
		Monitor.getVOManager().addVO(myVO2);
	}
	
}
