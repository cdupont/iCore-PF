package iCore.Virtual_Object.Dummy;

import iCore.Virtual_Object.VODiscoveryRequest;
import iCore.Virtual_Object.VORegistry;
import iCore.Virtual_Object.VirtualObject;
import iCore.Virtual_Object.VirtualObjectModel;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

public class DummyVORegistry implements VORegistry {
	
	Collection<VirtualObjectModel> VOMs = null; 
	
	public DummyVORegistry() {
		VOMs = new ArrayList<VirtualObjectModel>();
	}
	
	public boolean registerNewVO(VirtualObjectModel vom) {
		return VOMs.add(vom);
	}
	
	public boolean updateVO(VirtualObjectModel vo) {
		return false;
	}
	
	public boolean deleteVO(String voName) {
		return false;
	}
	
	@Override
	public Collection<VirtualObjectModel> VODiscovery(VODiscoveryRequest request) {
		
		Collection<VirtualObjectModel> myVOMs;
		
		if(request instanceof DummyVODiscoveryRequest && VOMs != null) {
		
			final DummyVODiscoveryRequest dummyRequest = (DummyVODiscoveryRequest)request;
			
			Predicate<VirtualObjectModel> isrequested = new Predicate<VirtualObjectModel>() {
		        @Override public boolean apply(VirtualObjectModel vom) { 

		        	      if(vom instanceof DummyVirtualObjectModel) {
		        	    	  DummyVirtualObjectModel myVOM = (DummyVirtualObjectModel)vom;
		        	    	  return myVOM.getVoType().equals(dummyRequest.getVOType());  
		        	      } else {
		        	    	  return false;
		        	      }
		    }};
					    	
		    myVOMs = Collections2.filter(VOMs, isrequested);
			
		} else {
			myVOMs = new ArrayList<VirtualObjectModel>();
		}
		return myVOMs;
		
	}
	
}
