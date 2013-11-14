package iCore.Virtual_Object;

import java.util.Collection;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

public interface VORegistry {
	public boolean registerNewVO(VirtualObjectModel vo);
	public boolean updateVO(VirtualObjectModel vo);	
	public boolean deleteVO(String voName);
	public Collection<VirtualObjectModel> VODiscovery(VODiscoveryRequest request);
	
}
