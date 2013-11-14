package iCore.Virtual_Object;


/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

public interface VOTemplates {
	
	void addTemplate(VirtualObject vo);	
	void removeTemplate(String voName);
	VirtualObject getTemplate(String voName);
	
}
