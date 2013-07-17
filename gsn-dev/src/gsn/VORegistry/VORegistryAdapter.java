/**
 * ============================== Header ============================== 
 * file:          VORegistry.java
 * project:       FIT4Green/iCore
 * created:       15 juil. 2013 by cdupont
 * 
 * $LastChangedDate:$ 
 * $LastChangedBy:$
 * $LastChangedRevision:$
 * 
 * short description:
 *   {To be completed}
 * ============================= /Header ==============================
 */
package gsn.VORegistry;

import gr.tns.JSONUtil;
import gr.tns.RestUtil;
import gsn.beans.DataField;
import gsn.beans.InputStream;
import gsn.beans.VSensorConfig;
import icore.voregistry.api.AccessRight;
import icore.voregistry.api.GeoLocation;
import icore.voregistry.api.ICTObject;
import icore.voregistry.api.InputParameter;
import icore.voregistry.api.OutputParameter;
import icore.voregistry.api.VODeleteRequest;
import icore.voregistry.api.VOFunction;
import icore.voregistry.api.VOParameter;
import icore.voregistry.api.VORegistrationRequest;
import icore.voregistry.api.VORegistryInterface;
import icore.voregistry.api.VORegistryResponse;
import icore.voregistry.api.VirtualObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Connector to VO registry
 * 
 * @author cdupont
 */
public class VORegistryAdapter {

	private String VO_BASE_URI, VO_REGISTRY_URI;

	public VORegistryAdapter() {
		/** SET VO REGISTRY URI **/
		setVORegistryUri("http://83.212.238.250:8092/vo_registry/");
		setVOBaseUri("http://purl.oclc.org/UPRC/icore/VO/Humidity_Sensor_DHT11"); //TODO change
	}

	public void registerNewVO(VSensorConfig vs) {
		
		VirtualObject vo;
		if(vs != null){
			try {
				vo = createVO(vs);
				String vo_representation = JSONUtil.toJson(vo);
				
				// Create the Registration request using the corresponding VO representation format
		    	VORegistrationRequest registration_request = new VORegistrationRequest("1", vo_representation, "application/json");
		    				    	
		    	VORegistryInterface vo_registry_if = 
						RestUtil.getHTTPProxy(URI.create(getVORegistryUri()), VORegistryInterface.class);
			    // Send	    	
		    	VORegistryResponse response =  vo_registry_if.registration(registration_request);

		    	//Receive and Printout the VO Registry response
		    	System.out.println(response.getStatus().getStatusCode()+" : "+response.getStatus().getStatusMessage());
					
		    	
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
		
}

	VirtualObject createVO(VSensorConfig vs) throws URISyntaxException {
		VirtualObject vo = new VirtualObject(new URI(getVOBaseUri()));

		vo.setType(new URI("http://purl.oclc.org/NET/ssnx/ssn#Sensor"));
		vo.setDeploymentInfo(new URI(VO_BASE_URI + "/deploy.wsdl"));
		vo.setVOStatus("AVAILABLE");
		vo.setVOMobility(false);

		vo.setICTObject(getIctObject(vs));
		vo.setVOFunctions(getVOFunctions(vs));
        vo.setVOParameters(getVOParams(vs));
		return vo;
	}


	private HashSet<VOParameter> getVOParams(VSensorConfig vs) throws URISyntaxException {
		
		HashSet<VOParameter> VOParams = new HashSet<VOParameter>();
		VOParameter VOParam = new VOParameter();
		VOParam.setName(vs.getName());
		VOParam.setAccessRights(getAccessRights(vs));
		VOParams.add(VOParam);
		return VOParams;
	}

	ICTObject getIctObject(VSensorConfig vs) {

		ICTObject ict_obj = new ICTObject();
		ict_obj.setGeoLocation(getICTGeoLoc(vs));

		return ict_obj;
	}

	GeoLocation getICTGeoLoc(VSensorConfig vs) {

		GeoLocation ict_geo_loc = new GeoLocation();
		if(vs.getAltitude() != null)
			ict_geo_loc.setAltitude(vs.getAltitude());
		if(vs.getLatitude() != null)
			ict_geo_loc.setLatitude(vs.getLatitude());
		if(vs.getLongitude() != null)
			ict_geo_loc.setLongitude(vs.getLongitude());

		return ict_geo_loc;
	}

	HashSet<AccessRight> getAccessRights(VSensorConfig vs)
			throws URISyntaxException {

		HashSet<AccessRight> acc_rights = new HashSet<AccessRight>();

		AccessRight acc_right1 = new AccessRight();
		acc_right1.setName("READ");
		acc_right1.setType(new URI("http://www.iot-icore.eu/ont/AccessRights/READ"));
		HashSet<String> UserRoles = new HashSet<String>();
		UserRoles.add("VO_OWNER");
		UserRoles.add("SIMPLE_USER");
		acc_right1.setValue(UserRoles);
		acc_rights.add(acc_right1);
		
		AccessRight acc_right2 = new AccessRight();
		acc_right2 = new AccessRight();
		acc_right2.setName("WRITE");
		acc_right2.setType(new URI("http://www.iot-icore.eu/ont/AccessRights/WRITE"));
		acc_right2.setValue("VO_OWNER");
		acc_rights.add(acc_right2);
		
		AccessRight acc_right3 = new AccessRight();
		acc_right3.setName("DISCOVER");
		acc_right3.setType(new URI("http://www.iot-icore.eu/ont/AccessRights/DISCOVERY"));
		UserRoles = new HashSet<String>();
		UserRoles.add("VO_OWNER");
		UserRoles.add("SIMPLE_USER");
		acc_right3.setValue(UserRoles);
		acc_rights.add(acc_right3);

		return acc_rights;
	}

	HashSet<VOFunction> getVOFunctions(VSensorConfig vs) throws URISyntaxException {

		HashSet<VOFunction> VOFunctions = new HashSet<VOFunction>();

		VOFunction vo_funct = new VOFunction();
		vo_funct.setName(vs.getDescription());
		vo_funct.setDescription(vs.getDescription());
		
		HashSet<InputParameter> in_params = new HashSet<InputParameter>();
		
		for(InputStream inputStream : vs.getInputStreams()) {
			InputParameter in_param = new InputParameter();
			in_param.setName(inputStream.getInputStreamName()); //TODO this does not map to something sensible 
			in_params.add(in_param);
		}
		
		vo_funct.setInputParameters(in_params);
		
		HashSet<OutputParameter> out_params = new HashSet<OutputParameter>();
		// - VO Function Output Parameters
				
		for(DataField df : vs.getOutputStructure()) {
			OutputParameter out_param = new OutputParameter();
			out_param.setName(df.getName());
			out_params.add(out_param);
        	//TODO: what about the type of the output? 
        }
		
		vo_funct.setOutputParameters(out_params);
		
		VOFunctions.add(vo_funct);
		
		return VOFunctions;
	}
	
	
	//TODO Have to base this on VO name
	public void deleteVO(VSensorConfig configFile){
		
		String _sparql_form = "DELETE DATA{ "+ 
				"<"+getVOBaseUri()+"> rdf:type icore:VO_Parameter ."+
		         "}";
	
		VODeleteRequest delete_request = new VODeleteRequest("1");
		delete_request.setQuery(_sparql_form);
		delete_request.setFilter(null);
		delete_request.setUserRole("VO_OWNER");
	    
	    VORegistryInterface vo_registry_if = 
					RestUtil.getHTTPProxy(URI.create(getVORegistryUri()), VORegistryInterface.class);
    		
	    VORegistryResponse response = vo_registry_if.delete(delete_request);
	    
	    System.out.println(response.getStatus().getStatusCode()+" : "+response.getStatus().getStatusMessage());
	}
	


	/** MANAGE VO BASE URI **/
	public String getVOBaseUri() {
		return VO_BASE_URI;
	}

	public void setVOBaseUri(String _uri) {
		this.VO_BASE_URI = _uri;
	}

	/** MANAGE VO REGISTRY URI **/
	public String getVORegistryUri() {
		return VO_REGISTRY_URI;
	}

	public void setVORegistryUri(String _vr_uri) {
		this.VO_REGISTRY_URI = _vr_uri;
	}

}
