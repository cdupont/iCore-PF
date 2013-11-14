package iCore.Service;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

public class ServiceRegistration {
	
	ServiceStorage serviceStorage = null; 
	
	public ServiceRegistration(ServiceStorage myServiceStorage) {
		serviceStorage = myServiceStorage;
	}
	
	public boolean registerService(Service service) {
		return serviceStorage.storeService(service);		
	}
	
    public boolean unregisterService(ServiceID serviceID) {
    	return serviceStorage.deleteService(serviceID);
	}
	
}
