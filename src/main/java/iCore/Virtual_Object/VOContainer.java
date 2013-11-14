package iCore.Virtual_Object;

import iCore.Virtual_Object.Dummy.DummyVirtualObjectModel;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

public class VOContainer{
	
	Collection<AbstractVirtualObject> VOs = null;
	
	public VOContainer() {
		VOs = new ArrayList<AbstractVirtualObject>();
	}
	
	
	public boolean addVO(AbstractVirtualObject AVO) {
		startAVO(AVO);
		return VOs.add(AVO);
	}
	
	public boolean deleteVO(String VOName) {
		
		//TODO
		return true;
	}
	
	public boolean updateVO(AbstractVirtualObject AVO) {
		//TODO
		return true;
	}
	
	
	public Optional<AbstractVirtualObject> getVOByName(final String voName) {
		
		Predicate<AbstractVirtualObject> isName = new Predicate<AbstractVirtualObject>() {
	        @Override public boolean apply(AbstractVirtualObject vom) { 
	        		return vom.getName().equals(voName);
	    }};
				    	
	    Collection<AbstractVirtualObject> myVOs = Collections2.filter(VOs, isName);
	    if(myVOs.size() > 0) {
	    	return Optional.of(Iterables.getFirst(myVOs, null));	
	    } else {
	    	return Optional.absent();
	    }
	    
	    
	}
	
	public void startAVO(AbstractVirtualObject AVO) {
		AVO.start();
	}

}
