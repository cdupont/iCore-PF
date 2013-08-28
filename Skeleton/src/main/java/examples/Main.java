package examples;



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
public final class Main {

	public static void main ( String[] args ) throws InterruptedException {
	
		iCore.Main.init();
		VO_Installer.installVO();
		
		Application_Example app = new Application_Example();
		app.performRequest();
		
		Thread.sleep(6000);
		
	}
}
