package iCore.Service;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

public interface ServiceStorage {
	
	public boolean storeService(Service service);
	public Service retrieveService(ServiceID serviceID);
	public boolean deleteService(ServiceID serviceID);
	
}
