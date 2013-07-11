package samples;

import gr.tns.RestUtil;
import icore.voregistry.api.VODeleteRequest;
import icore.voregistry.api.VORegistryInterface;
import icore.voregistry.api.VORegistryResponse;

import java.awt.EventQueue;
import java.net.URI;

public class vo_delete {
	/** PARAMETERS **/
	private static String _sparql_form;
	private String VO_BASE_URI, VO_REGISTRY_URI;
	private static VODeleteRequest delete_request;
	private static VORegistryResponse response;

	
	public void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new vo_delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	//- Constructor
	public vo_delete(){
		deleteVOParameterExample();
	}
	
	/*********************** 
	 *   METHODS SAMPLES 
	 ***********************/
	/** -1- This SPARQL delete is used in order to delete all VO Parameters that are associated with a VO, using VO URI **/	
	public void deleteVOParameterExample(){
		
		/** SET VO REGISTRY URI **/
		setVORegistryUri("http://83.212.238.250:8092/vo_registry/");

		/** SET VO BASE URI -(Currently defined manually by the user - next version supported by a centralized mechanism)- **/
		setVOBaseUri("http://purl.oclc.org/UPRC/icore/VO/CNET_Emulated_Humidity_Sensor_0001");
		
	   /********************************************************** 
	    * -(Step 1)- CREATE SPARQL UPDATE 
	    ***********************************************************/
    	//-1.1: SPARQL request form (SPARQL expression)
		_sparql_form = 
				"DELETE DATA{ "+ 
    					"<"+getVOBaseUri()+"> rdf:type icore:VO_Parameter ."+
    			"}";
		
		
		/*************************************************************************** 
	     *-(Step 2)- CREATE THE UPDATE REQUEST
	     ***************************************************************************/
    	//-2.1: Create the Update Request using the defined SPARQL expression
		delete_request =  new VODeleteRequest("1");
		delete_request.setQuery(_sparql_form);
		delete_request.setFilter(null);
		delete_request.setUserRole("VO_OWNER");
	    	
	   	/************************************************************************** 
		 *-(Step 3)- CONNECT TO THE VO REGISTRY AND SEND THE REGISTRATION REQUEST 
		 **************************************************************************/
		 //- 3.1: Connect
	    	VORegistryInterface vo_registry_if = 
					RestUtil.getHTTPProxy(URI.create(getVORegistryUri()), VORegistryInterface.class);
    	 //- 3.2: Send	    	
	    	response = vo_registry_if.delete(delete_request);
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

}
