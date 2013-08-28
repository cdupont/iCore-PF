package iCore.Service;

import java.util.ArrayList;
import java.util.Collection;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

public class DefaultServiceStorage implements ServiceStorage{
	
	Collection<Service> services = null;
	
	public DefaultServiceStorage() {
		services = new ArrayList<Service>();
	}
	
	@Override
	public boolean storeService(Service service) {
		return services.add(service);
	}
	
	public Service getServiceByName(String serviceName) {
		return null; //TODO
	}
	
	@Override
	public boolean deleteService(ServiceID serviceID) {
		return true; //TODO
	}

	@Override
	public Service retrieveService(ServiceID serviceID) {
		return null; //TODO
	}
	
}
