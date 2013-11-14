package iCore.Monitor;

import iCore.Application.ApplicationManager;
import iCore.Service.DefaultServiceStorage;
import iCore.Service.ServiceManager;
import iCore.Service.ServiceRequestAnalyser;
import iCore.Service.Dummy.DummyServiceRequestAnalyser;
import iCore.Service.Knowledge.ServiceKnowledgeDB;
import iCore.Service.Knowledge.Dummy.DummyServiceKnowledgeDB;
import iCore.Virtual_Object.VOContainer;
import iCore.Virtual_Object.VOManager;
import iCore.Virtual_Object.Dummy.DummyVORegistry;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

//The monitor is in charge of the maintenance of every iCore components (start, stop, etc.)
public final class Monitor extends Thread {
	
	static ApplicationManager appManager= null;
	static ServiceManager serviceManager = null;
	static VOManager voManager = null;

	
	public Monitor() {
		
		ServiceKnowledgeDB serviceKnlgDB = new DummyServiceKnowledgeDB();
		ServiceRequestAnalyser serviceRequestAnalyser = new DummyServiceRequestAnalyser(serviceKnlgDB);
		
		serviceManager = new ServiceManager(new DefaultServiceStorage(), serviceRequestAnalyser);
		voManager = new VOManager(new DummyVORegistry());
		appManager = new ApplicationManager(serviceManager);
	}
	
    @Override
	public void start() {
		
		serviceManager.start();
		voManager.start();
		appManager.start();
		
	}
		
	
	public static ServiceManager getServiceManager() {
		return serviceManager;
	}
	
	public static VOManager getVOManager() {
		return voManager;
	}
	
	public static ApplicationManager getApplicationManager() {
		return appManager;
	}
}
