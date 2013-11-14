/**
* ============================== Header ============================== 
* file:          CompositeVirtualObject.java
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
package iCore.Composite_Virtual_Object.Dummy;

import java.util.ArrayList;
import java.util.Collection;

import iCore.Composite_Virtual_Object.CVOProcess;
import iCore.Composite_Virtual_Object.CompositeVirtualObject;
import iCore.Virtual_Object.AbstractVirtualObject;
import iCore.Virtual_Object.VOOutput.VOOutputSubscriber;
import iCore.Virtual_Object.VirtualObjectModel;
import iCore.Virtual_Object.Dummy.DummyAbstractVirtualObject;

/**
 * {To be completed; use html notation, if necessary}
 * 
 *
 * @author cdupont
 */
public abstract class DummyCVO extends DummyAbstractVirtualObject implements CompositeVirtualObject{

	
	protected Collection<AbstractVirtualObject> AVOs = null;
	protected CVOProcess process = null;
	
	public DummyCVO(String myName, VirtualObjectModel myVOModel, CVOProcess process) {
		super(myName, myVOModel);
		AVOs = new ArrayList<AbstractVirtualObject>();
	}

	@Override
	public boolean addAbstractVOs(AbstractVirtualObject AVO) {
		return AVOs.add(AVO);
	}

	@Override
	public void setProcess(CVOProcess myProcess) {
		process = myProcess;
	}

	
	@Override
	public CVOProcess getProcess() {
		return process;
	}

	@Override
	public String display() {
		String display = name;
		for (AbstractVirtualObject avos : AVOs) {
			display = display + "\n |- " + avos.getName();
		}
		return display;
	}
}
