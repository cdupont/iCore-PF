package samples;

import gr.tns.RestUtil;
import icore.voregistry.api.VORegistryInterface;
import icore.voregistry.api.VORegistryResponse;
import icore.voregistry.api.VOUpdateRequest;

import java.awt.EventQueue;
import java.net.URI;

public class vo_update {

	/** PARAMETERS **/
	private static String _sparql_form;
	private String VO_BASE_URI, VO_REGISTRY_URI;
	private static VOUpdateRequest update_request;
	private static VORegistryResponse response;

	
	public void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new vo_update();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//- Constructor
	public vo_update(){
		updateParameterByVoIdExample();
	}
	
	
	/*********************** 
	 *   METHODS SAMPLES 
	 ***********************/
	/** -1- This SPARQL update is used in order to update the status of a VO, using its URI, as "OFF_LINE" **/	
	public void updateParameterByVoIdExample(){
		
		/** SET VO REGISTRY URI **/
		setVORegistryUri("http://83.212.238.250:8092/vo_registry/");

		/** SET VO BASE URI -(Currently defined manually by the user - next version supported by a centralized mechanism)- **/
		setVOBaseUri("http://purl.oclc.org/UPRC/icore/VO/CNET_Emulated_Humidity_Sensor_0001");
		
	   /********************************************************** 
	    * -(Step 1)- CREATE SPARQL UPDATE 
	    ***********************************************************/
    	//-1.1: SPARQL request form (SPARQL expression)
		_sparql_form = 
			"DELETE{ "+ 
			"    <"+getVOBaseUri()+"> icore:hasStatus ?sta . "+  
			"}  "+
			"WHERE{  "+
			"    <"+getVOBaseUri()+"> icore:hasStatus ?sta . "+   
			"};  "+
			"INSERT {  "+
			"    <"+getVOBaseUri()+"> icore:hasStatus \"OFF_LINE\" . "+  
			"}  "+
			"WHERE{ ?s ?p ?o .}  ";
		
		
		/*************************************************************************** 
	     *-(Step 2)- CREATE THE UPDATE REQUEST
	     ***************************************************************************/
    	//-1.2: Create the Update Request using the defined SPARQL expression
    	update_request =  new VOUpdateRequest("1");
    	update_request.setQuery(_sparql_form);
    	update_request.setUserRole("VO_OWNER");
    	update_request.setFilter(null);
	    	
	   	/************************************************************************** 
		 *-(Step 3)- CONNECT TO THE VO REGISTRY AND SEND THE REGISTRATION REQUEST 
		 **************************************************************************/
		 //- 3.1: Connect
	    	VORegistryInterface vo_registry_if = 
					RestUtil.getHTTPProxy(URI.create(getVORegistryUri()), VORegistryInterface.class);
    	 //- 3.2: Send	    	
	    	response =  vo_registry_if.update(update_request);
	    	//-3.2.1: Receive and Printout the VO Registry response
	    		System.out.println(response.getStatus().getStatusCode()+" : "+response.getStatus().getStatusMessage());
	}
	
	
    /**  MANAGE VO BASE URI **/
    public String getVOBaseUri(){
    	return VO_BASE_URI;
    }
    public void setVOBaseUri(String _uri){
    	this.VO_BASE_URI = _uri;
    }

    /**  MANAGE VO REGISTRY URI **/
    public String getVORegistryUri(){
    	return VO_REGISTRY_URI;
    }
    public void setVORegistryUri(String _vr_uri){
    	this.VO_REGISTRY_URI = _vr_uri;
    }
	
}//end of class