/**
* ============================== Header ============================== 
* file:          VOManager.java
* project:       FIT4Green/Skeleton
* created:       1 août 2013 by cdupont
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

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public class VOManager extends Thread {
	
	static VORegistry voRegistry = null;
	static VOContainer voContainer = null;
	
	public VOManager(VORegistry myVORegistry) {
		voRegistry = myVORegistry;
		voContainer = new VOContainer();
	}
	
	@Override
	public void run() {
        
    }
	
	@Override
	public void start() {
        
    }	

	public static VORegistry getVORegistry() {
		return voRegistry;
	}
	
	public static VOContainer getVOContainer() {
		return voContainer;
	}
	
	public boolean addVO(AbstractVirtualObject AVO) {
		boolean res1 = voContainer.addVO(AVO);
		boolean res2 = voRegistry.registerNewVO(AVO.getVOModel());
		return res1 && res2;
	}
	
	public boolean deleteVO(VirtualObjectID voID) {
		return true; //TODO
	}
	
	public boolean updateVO(AbstractVirtualObject AVO) {
		return true; //TODO
	}
	
	public AbstractVirtualObject getVOByName(String voName) {
		return null; //TODO
	}
		
}
