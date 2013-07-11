package samples;

import gr.tns.RestUtil;
import icore.voregistry.api.AccessRight;
import icore.voregistry.api.BillingCost;
import icore.voregistry.api.ICTParameter;
import icore.voregistry.api.InputParameter;
import icore.voregistry.api.MetaFeatureSet;
import icore.voregistry.api.OutputParameter;
import icore.voregistry.api.VODiscoveryRequest;
import icore.voregistry.api.VOFunction;
import icore.voregistry.api.VOFunctionFeature;
import icore.voregistry.api.VOParameter;
import icore.voregistry.api.VORegistryInterface;
import icore.voregistry.api.VORegistryResponse;
import icore.voregistry.api.VirtualObject;

import java.awt.EventQueue;
import java.net.URI;

public class vo_discovery {

	private static String _sparql_query;
	private static String _query_filter ;
	private String VO_BASE_URI, VO_REGISTRY_URI;
	private static VODiscoveryRequest discovery_request;
	private static VORegistryResponse response;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new vo_discovery();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public vo_discovery(){
		discoveryAll();
	  //discoveryUsingVOID();
	  //discoveryUsingFilter();		
	}
	
	
	/*********************** 
	 *   METHODS SAMPLES 
	 ***********************/
	/** -1- Get All Available VOs in the VO Registry as VO_OWNER **/
	public void discoveryAll(){
		
		/** SET VO REGISTRY URI **/
		setVORegistryUri("http://83.212.238.250:8092/vo_registry/");
		
		/************************************************** 
	     * -(Step 1)- CREATE SPARQL QUERY 
	     **************************************************/
    		// In this case it does not important to use a SPARQL Query. Query should be null
			_sparql_query = null;
			
		/********************************************************************** 
		 * -(Step 2)- CREATE THE DISCOVERY REQUEST (based on the SPARQL query)
		 **********************************************************************/
	     //-1.2: Create the Discovery Request using the defined SPARQL expression
	    	discovery_request =  new VODiscoveryRequest("1");
	    	discovery_request.setUserRole("VO_OWNER");		    	
	    	
	    	
	    /************************************************************************** 
		 *-(Step 3)- CONNECT TO THE VO REGISTRY AND SEND THE REGISTRATION REQUEST 
		 **************************************************************************/
		 //-3.1: Connect
	    	VORegistryInterface vo_registry_if = 
	    				RestUtil.getHTTPProxy(URI.create(getVORegistryUri()), VORegistryInterface.class);
			
	     //-3.2: Send the query and Get Response
	    	response = vo_registry_if.discovery(discovery_request);
		   	System.out.println(
		   			response.getStatus().getStatusCode()+": "+response.getStatus().getStatusMessage()
		   	);
	    	
	    //-3.3: Get the results if they are exist
	    	if(response.getStatus().getStatusCode()==200){
	    		//-3.1: Get all available VOs that fullfil the discovery request
	    		if(response.getAllVOs().isEmpty()!=true){
	    			int i = 1; 
	    			for (VirtualObject vo : response.getAllVOs()) {
	    				System.out.println("------------------\nRESULT["+i+"]\n------------------\n\t"); 
	    				System.out.println("Virtual Object{");
	    				System.out.println("\t"+ 
	    						vo.getResourceURI() + " | " + vo.getType() + " | " + vo.getDeploymentInfo() + " | " + vo.getVOStatus() + " | " + vo.isMobileVO() + " \n\t"  
	    				);
	    				//- Get All Functions
	    				if(vo.getVOFunctions()!=null){
		    				for(VOFunction vo_function : vo.getVOFunctions()){
		    					System.out.println("VO Function{");
		    					System.out.print("\t"+
		    							vo_function.getResourceURI() + " | " + vo_function.getName() + " | " + vo_function.getDescription() + " | "
		    					);
		    					//- Get VO Function Input names
		    					if(vo_function.getInputNames()!=null){
			    					System.out.print("[");
			    					for(String input_name : vo_function.getInputNames()){
			    						System.out.print(" "+input_name+" ");
			    					}
			    					System.out.print("]"+ " | ");
		    					}
		    					
		    					//- Get VO Function Output names
		    					if(vo_function.getOutputNames()!=null){
			    					System.out.print("[");
			    					for(String output_name : vo_function.getOutputNames()){
			    						System.out.print(" "+output_name+" ");
			    					}
			    					System.out.print("]"+ "\n\t\t\t");
		    					}
		    					
		    					//- Get VO Function Input Parameters
		    					if(vo_function.getInputParameters()!=null){
			    					for(InputParameter in : vo_function.getInputParameters()){
			    						System.out.print("Input Parameter{ \n\t\t\t\t");
			    						System.out.print(
			    								in.getResourceURI() +" | "+ in.getName()
			    						);
			    						System.out.print("\n\t\t\t\t\t");
			    						//- Get Input Params Meta Features
			    						if(in.getMetaFeatureSet()!=null){
				    						for(MetaFeatureSet mfs : in.getMetaFeatureSet()){		    																	
				    							System.out.print("Input Parameter MetaFeature{ ");
				    							System.out.print(
														mfs.getType() +" , "+ mfs.getValue()												
												);
				    							System.out.print(" }");
				    							System.out.print("\n\t\t\t\t");
											}//end for
			    						}
			    						System.out.print("\n\t\t\t"+"}");
			    					}//end for Input params
		    					}
		    					//- Get VO Function Output Parameters
		    					if(vo_function.getOutputParameters()!=null){
			    					for(OutputParameter out : vo_function.getOutputParameters()){
			    						System.out.print("\n\t\t\tOutput Parameter{ \n\t\t\t\t");
			    						System.out.print(
			    								out.getResourceURI() +" | "+ out.getName()
			    						);		
			    						System.out.print("\n\t\t\t\t\t");
			    						//- Get Output Params Meta Features
			    						if(out.getMetaFeatureSet()!=null){
				    						for(MetaFeatureSet mfs : out.getMetaFeatureSet()){		    																	
				    							System.out.print("Output Parameter MetaFeature{ ");
				    							System.out.print(
														mfs.getType() +" , "+ mfs.getValue()												
												);
				    							System.out.print(" }");
				    							System.out.print("\n\t\t\t\t\t");
											}//end for
			    						}
			    						System.out.print("\n\t\t\t"+"}");
			    					}//end for output params
		    					}//end if check null
		    					
		    					//- Get VO Function Feature
		    					if(vo_function.getVOFunctionFeatures()!=null){
			    					for(VOFunctionFeature voff : vo_function.getVOFunctionFeatures()){
										System.out.print("\n\t\t\t");
			    						System.out.print("VO Function Fature { \n\t\t\t\t");
			    						System.out.print(
			    								voff.getResourceURI() + " | "+ voff.getType() + " | "+ voff.getName() + " | "+ voff.getValue()
			    							);
			    						System.out.println("\n\t\t\t}");
			    					}//end for VO Function features
		    					}
		    					
		    					//- Get VO Function Access Rights
		    					if(vo_function.getAccessRights()!=null){
			    					for(AccessRight vo_ar : vo_function.getAccessRights()){
			    						System.out.print("\n\t\t\t");
			    						System.out.print("VO Function Access Right { \n\t\t\t\t");
			    						System.out.print(
			    								vo_ar.getResourceURI() + " | "+ vo_ar.getType() + " | "+ vo_ar.getValue()
			    								);
			    						System.out.println("\n\t\t\t}");
			    					}//end for VO Access Rights
		    					}
		    					//- Get VO Billing Costs
		    					if(vo_function.getBillingCosts()!=null){
			    					for(BillingCost vo_bc : vo_function.getBillingCosts()){
			    						System.out.print("\n\t\t\t");
			    						System.out.print("VO Function Billing Cost { \n\t\t\t\t");
			    						System.out.print(
			    								vo_bc.getResourceURI() + " | "+ vo_bc.getType() + " | "+ vo_bc.getValue()
			    								);
			    						System.out.println("\n\t\t\t}");
			    					}//end for VO Function Billing Costs
		    					}
		    					
		    				}//end for VO Functions
	    				}//end if check null VO Functions
	    			
	    				//- Get All VO Parameters
	    				if(vo.getVOParameters()!=null){
		    				for(VOParameter vo_par : vo.getVOParameters()){
		    					System.out.print("\tVO Parameter{\n\t\t");
		    					System.out.print(
		    							vo_par.getResourceURI() + " | " + vo_par.getName() + 
		    							"\n\t\t\t"
			    						);		
	    						//- Get VO Param Meta Features
		    					if(vo_par.getMetaFeatureSet()!=null){
		    						for(MetaFeatureSet mfs : vo_par.getMetaFeatureSet()){		    																	
										System.out.print("VO Parameter Meta{ " + mfs.getType() +" , "+ mfs.getValue());
		    							System.out.println("\t\t\t");
									}//end while
		    					}
		    					System.out.println("\t}");
		    				}//end for VO Parameters
	    				}
	    				
	    				//- Get VO Owner
	    				if(vo.getVOOwner()!=null){
		    				System.out.println("\tVO Owner{");
		    				System.out.println("\t\t"+
		    						vo.getVOOwner().getResourceURI() + "\n\t"
				    		);
		    				System.out.println("\t}");
	    				}
	    				
	    				//- Get ICT Object
	    				if(vo.getICTObject()!=null){
		    				System.out.println("\tICT Object{");
		    				System.out.println("\t\t"+
		    						vo.getICTObject().getResourceURI() + "\n\t"
				    		);
	    				
		    				//- Get All ICT Parameters
		    				if(vo.getICTObject().getICTParameters()!=null){
			    				for(ICTParameter ict_par : vo.getICTObject().getICTParameters()){
			    					System.out.print("\t\tICT Parameter{\n\t\t");
			    					System.out.print(
			    							ict_par.getResourceURI() + " | " + ict_par.getName() + 
			    							"\n\t\t\t"
				    						);		
		    						//- Get ICT Param Meta Features
			    					if(ict_par.getMetaFeatureSet()!=null){
			    						for(MetaFeatureSet mfs : ict_par.getMetaFeatureSet()){		    																	
											System.out.print("ICT Parameter Meta{ " + mfs.getType() +" , "+ mfs.getValue() +" }");
			    							System.out.println("\t\t\t");
										}//end for
			    						System.out.println("\t}");
			    					}
			    				}//end for ICT Parameters
		    				}//end if check null
		    				
		    				//- Get ICT GeoLocation
		    				if(vo.getICTObject().getGeoLocation()!=null){
			    				System.out.print("\tICT GeoLocation{ "+
			    					vo.getICTObject().getGeoLocation().getLongitude() + " | " +
				    					vo.getICTObject().getGeoLocation().getLatitude() + " | " +
				    						vo.getICTObject().getGeoLocation().getAltitude()
				    			+" }");
		    				}
		    				//- Get Non-ICT Object
		    				if(vo.getICTObject().getNonICTObject()!=null){
			    				System.out.println("\n\t\tNon-ICT Object{");
			    				System.out.println("\t\t\t"+
			    						vo.getICTObject().getNonICTObject().getResourceURI() + " | " + vo.getICTObject().getNonICTObject().getName() +" | "+ vo.getICTObject().getNonICTObject().getType()
			    				);
			    				//- Get Non-ICT GeoLocation
			    				if(vo.getICTObject().getNonICTObject().getGeoLocation()!=null){
				    				System.out.print("\t\t\t\tNon-ICT GeoLocation{ "+
				    						vo.getICTObject().getNonICTObject().getGeoLocation().getLongitude() + " | " + 
				    							vo.getICTObject().getNonICTObject().getGeoLocation().getLatitude() + " | " + 
				    								vo.getICTObject().getNonICTObject().getGeoLocation().getAltitude()
				    						
			    					+"}");
				    				System.out.println("\t\t}");
			    				}//end if check null
		    				}
		    				System.out.println("\n\t}\n");
	    				}//end if check null ICT
	    				System.out.println("}");
	    				System.out.println("-----------------------------------------------\n\n");
	    				i++;
	    			 }//end for
	    		}//end if check VOs' container
	    	}//end if
	}
	
	/** -2- Simple SPARQL Query **/
	public void discoveryUsingVOID(){
		/************************************************** 
	     * -(Step 1)- CREATE SPARQL QUERY 
	     **************************************************/
    	//-1: Create Query in SPARQL Form
	    	//-1.1: SPARQL query form (SPARQL expression)
	    	_sparql_query = 
	    		"SELECT DISTINCT ?vo "+
	    		" WHERE{ "+
	    		" ?vo rdf:type icore:Virtual_Object . "+
	    	    " }";
	    	
		/********************************************************************** 
		 * -(Step 2)- CREATE THE DISCOVERY REQUEST (based on the SPARQL query)
		 **********************************************************************/
	     //-1.2: Create the Discovery Request using the defined SPARQL expression
	    	discovery_request =  new VODiscoveryRequest("1");
	    	discovery_request.setQuery(_sparql_query);
	    	discovery_request.setUserRole("VO_OWNER");		    	
	    	
	    	
	    /************************************************************************** 
		 *-(Step 3)- CONNECT TO THE VO REGISTRY AND SEND THE REGISTRATION REQUEST 
		 **************************************************************************/
		 //-3.1: Connect
	    	VORegistryInterface vo_registry_if = 
	    				RestUtil.getHTTPProxy(URI.create("http://localhost:8092/vo_registry/"), VORegistryInterface.class);
			
	     //-3.2: Send the query and Get Response
	    	response = vo_registry_if.discovery(discovery_request);
		   	System.out.println(
		   			response.getStatus().getStatusCode()+": "+response.getStatus().getStatusMessage()
		   	);
	    	
	    //-3.3: Get the results if they are exist
	    	if(response.getStatus().getStatusCode()==200){
	    		//-3.1: Get all available VOs that fullfil the discovery request
	    		if(response.getAllVOs().isEmpty()!=true){
	    			int i = 1; 
	    			for (VirtualObject vo : response.getAllVOs()) {
	    				System.out.println("------------------\nRESULT["+i+"]\n------------------\n\t"); 
	    				System.out.println("Virtual Object{");
	    				System.out.println("\t"+ 
	    						vo.getResourceURI() + " | " + vo.getType() + " | " + vo.getDeploymentInfo() + " | " + vo.getVOStatus() + " | " + vo.isMobileVO() + " \n\t"  
	    				);
	    				//- Get All Functions
	    				if(vo.getVOFunctions()!=null){
		    				for(VOFunction vo_function : vo.getVOFunctions()){
		    					System.out.println("VO Function{");
		    					System.out.print("\t"+
		    							vo_function.getResourceURI() + " | " + vo_function.getName() + " | " + vo_function.getDescription() + " | "
		    					);
		    					//- Get VO Function Input names
		    					if(vo_function.getInputNames()!=null){
			    					System.out.print("[");
			    					for(String input_name : vo_function.getInputNames()){
			    						System.out.print(" "+input_name+" ");
			    					}
			    					System.out.print("]"+ " | ");
		    					}
		    					
		    					//- Get VO Function Output names
		    					if(vo_function.getOutputNames()!=null){
			    					System.out.print("[");
			    					for(String output_name : vo_function.getOutputNames()){
			    						System.out.print(" "+output_name+" ");
			    					}
			    					System.out.print("]"+ "\n\t\t\t");
		    					}
		    					
		    					//- Get VO Function Input Parameters
		    					if(vo_function.getInputParameters()!=null){
			    					for(InputParameter in : vo_function.getInputParameters()){
			    						System.out.print("Input Parameter{ \n\t\t\t\t");
			    						System.out.print(
			    								in.getResourceURI() +" | "+ in.getName()
			    						);
			    						System.out.print("\n\t\t\t\t\t");
			    						//- Get Input Params Meta Features
			    						if(in.getMetaFeatureSet()!=null){
				    						for(MetaFeatureSet mfs : in.getMetaFeatureSet()){		    																	
				    							System.out.print("Input Parameter MetaFeature{ ");
				    							System.out.print(
														mfs.getType() +" , "+ mfs.getValue()												
												);
				    							System.out.print(" }");
				    							System.out.print("\n\t\t\t\t");
											}//end for
			    						}
			    						System.out.print("\n\t\t\t"+"}");
			    					}//end for Input params
		    					}
		    					//- Get VO Function Output Parameters
		    					if(vo_function.getOutputParameters()!=null){
			    					for(OutputParameter out : vo_function.getOutputParameters()){
			    						System.out.print("\n\t\t\tOutput Parameter{ \n\t\t\t\t");
			    						System.out.print(
			    								out.getResourceURI() +" | "+ out.getName()
			    						);		
			    						System.out.print("\n\t\t\t\t\t");
			    						//- Get Output Params Meta Features
			    						if(out.getMetaFeatureSet()!=null){
				    						for(MetaFeatureSet mfs : out.getMetaFeatureSet()){		    																	
				    							System.out.print("Output Parameter MetaFeature{ ");
				    							System.out.print(
														mfs.getType() +" , "+ mfs.getValue()												
												);
				    							System.out.print(" }");
				    							System.out.print("\n\t\t\t\t\t");
											}//end for
			    						}
			    						System.out.print("\n\t\t\t"+"}");
			    					}//end for output params
		    					}//end if check null
		    					
		    					//- Get VO Function Feature
		    					if(vo_function.getVOFunctionFeatures()!=null){
			    					for(VOFunctionFeature voff : vo_function.getVOFunctionFeatures()){
										System.out.print("\n\t\t\t");
			    						System.out.print("VO Function Fature { \n\t\t\t\t");
			    						System.out.print(
			    								voff.getResourceURI() + " | "+ voff.getType() + " | "+ voff.getName() + " | "+ voff.getValue()
			    							);
			    						System.out.println("\n\t\t\t}");
			    					}//end for VO Function features
		    					}
		    					
		    					//- Get VO Function Access Rights
		    					if(vo_function.getAccessRights()!=null){
			    					for(AccessRight vo_ar : vo_function.getAccessRights()){
			    						System.out.print("\n\t\t\t");
			    						System.out.print("VO Function Access Right { \n\t\t\t\t");
			    						System.out.print(
			    								vo_ar.getResourceURI() + " | "+ vo_ar.getType() + " | "+ vo_ar.getValue()
			    								);
			    						System.out.println("\n\t\t\t}");
			    					}//end for VO Access Rights
		    					}
		    					//- Get VO Billing Costs
		    					if(vo_function.getBillingCosts()!=null){
			    					for(BillingCost vo_bc : vo_function.getBillingCosts()){
			    						System.out.print("\n\t\t\t");
			    						System.out.print("VO Function Billing Cost { \n\t\t\t\t");
			    						System.out.print(
			    								vo_bc.getResourceURI() + " | "+ vo_bc.getType() + " | "+ vo_bc.getValue()
			    								);
			    						System.out.println("\n\t\t\t}");
			    					}//end for VO Function Billing Costs
		    					}
		    					
		    				}//end for VO Functions
	    				}//end if check null VO Functions
	    			
	    				//- Get All VO Parameters
	    				if(vo.getVOParameters()!=null){
		    				for(VOParameter vo_par : vo.getVOParameters()){
		    					System.out.print("\tVO Parameter{\n\t\t");
		    					System.out.print(
		    							vo_par.getResourceURI() + " | " + vo_par.getName() + 
		    							"\n\t\t\t"
			    						);		
	    						//- Get VO Param Meta Features
		    					if(vo_par.getMetaFeatureSet()!=null){
		    						for(MetaFeatureSet mfs : vo_par.getMetaFeatureSet()){		    																	
										System.out.print("VO Parameter Meta{ " + mfs.getType() +" , "+ mfs.getValue());
		    							System.out.println("\t\t\t");
									}//end while
		    					}
		    					System.out.println("\t}");
		    				}//end for VO Parameters
	    				}
	    				
	    				//- Get VO Owner
	    				if(vo.getVOOwner()!=null){
		    				System.out.println("\tVO Owner{");
		    				System.out.println("\t\t"+
		    						vo.getVOOwner().getResourceURI() + "\n\t"
				    		);
		    				System.out.println("\t}");
	    				}
	    				
	    				//- Get ICT Object
	    				if(vo.getICTObject()!=null){
		    				System.out.println("\tICT Object{");
		    				System.out.println("\t\t"+
		    						vo.getICTObject().getResourceURI() + "\n\t"
				    		);
	    				
		    				//- Get All ICT Parameters
		    				if(vo.getICTObject().getICTParameters()!=null){
			    				for(ICTParameter ict_par : vo.getICTObject().getICTParameters()){
			    					System.out.print("\t\tICT Parameter{\n\t\t");
			    					System.out.print(
			    							ict_par.getResourceURI() + " | " + ict_par.getName() + 
			    							"\n\t\t\t"
				    						);		
		    						//- Get ICT Param Meta Features
			    					if(ict_par.getMetaFeatureSet()!=null){
			    						for(MetaFeatureSet mfs : ict_par.getMetaFeatureSet()){		    																	
											System.out.print("ICT Parameter Meta{ " + mfs.getType() +" , "+ mfs.getValue() +" }");
			    							System.out.println("\t\t\t");
										}//end for
			    						System.out.println("\t}");
			    					}
			    				}//end for ICT Parameters
		    				}//end if check null
		    				
		    				//- Get ICT GeoLocation
		    				if(vo.getICTObject().getGeoLocation()!=null){
			    				System.out.print("\tICT GeoLocation{ "+
			    					vo.getICTObject().getGeoLocation().getLongitude() + " | " +
				    					vo.getICTObject().getGeoLocation().getLatitude() + " | " +
				    						vo.getICTObject().getGeoLocation().getAltitude()
				    			+" }");
		    				}
		    				//- Get Non-ICT Object
		    				if(vo.getICTObject().getNonICTObject()!=null){
			    				System.out.println("\n\t\tNon-ICT Object{");
			    				System.out.println("\t\t\t"+
			    						vo.getICTObject().getNonICTObject().getResourceURI() + " | " + vo.getICTObject().getNonICTObject().getName() +" | "+ vo.getICTObject().getNonICTObject().getType()
			    				);
			    				//- Get Non-ICT GeoLocation
			    				if(vo.getICTObject().getNonICTObject().getGeoLocation()!=null){
				    				System.out.print("\t\t\t\tNon-ICT GeoLocation{ "+
				    						vo.getICTObject().getNonICTObject().getGeoLocation().getLongitude() + " | " + 
				    							vo.getICTObject().getNonICTObject().getGeoLocation().getLatitude() + " | " + 
				    								vo.getICTObject().getNonICTObject().getGeoLocation().getAltitude()
				    						
			    					+"}");
				    				System.out.println("\t\t}");
			    				}//end if check null
		    				}
		    				System.out.println("\n\t}\n");
	    				}//end if check null ICT
	    				System.out.println("}");
	    				System.out.println("-----------------------------------------------\n\n");
	    				i++;
	    			 }//end for
	    		}//end if check VOs' container
	    	}//end if
	
	}//end of method

	/** -3- Discovery using filter **/
	public void discoveryUsingFilter(){
		/************************************************** 
	     * -(Step 1)- CREATE SPARQL QUERY 
	     **************************************************/
    	//-1: Create Query in SPARQL Form
	    	//-1.1: SPARQL query form (SPARQL expression)
	    	_sparql_query = 
	    		"SELECT DISTINCT ?vo "+
	    		" WHERE{ "+
	    		" ?vo rdf:type icore:Virtual_Object; "+
	    		"			   icore:hasStatus ?status . "+
	    	    " }";
	    	//-1.2: Create filter in SPARQL form
	    	_query_filter  = 
	    			"regex(str($status),\"AVAILABLE\")";
	    	
		/********************************************************************** 
		 * -(Step 2)- CREATE THE DISCOVERY REQUEST (based on the SPARQL query)
		 **********************************************************************/
	     //-1.2: Create the Discovery Request using the defined SPARQL expression
	    	discovery_request =  new VODiscoveryRequest("1");
	    	discovery_request.setQuery(_sparql_query);
	    	discovery_request.setFilter(_query_filter );
	    	discovery_request.setUserRole("VO_OWNER");		    	
	    	
	    	
	    /************************************************************************** 
		 *-(Step 3)- CONNECT TO THE VO REGISTRY AND SEND THE REGISTRATION REQUEST 
		 **************************************************************************/
		 //-3.1: Connect
	    	VORegistryInterface vo_registry_if = 
	    				RestUtil.getHTTPProxy(URI.create("http://localhost:8092/vo_registry/"), VORegistryInterface.class);
			
	     //-3.2: Send the query and Get Response
	    	response = vo_registry_if.discovery(discovery_request);
		   	System.out.println(
		   			response.getStatus().getStatusCode()+": "+response.getStatus().getStatusMessage()
		   	);
	    	
	    //-3.3: Get the results if they are exist
	    	if(response.getStatus().getStatusCode()==200){
	    		//-3.1: Get all available VOs that fullfil the discovery request
	    		if(response.getAllVOs().isEmpty()!=true){
	    			int i = 1; 
	    			for (VirtualObject vo : response.getAllVOs()) {
	    				System.out.println("------------------\nRESULT["+i+"]\n------------------\n\t"); 
	    				System.out.println("Virtual Object{");
	    				System.out.println("\t"+ 
	    						vo.getResourceURI() + " | " + vo.getType() + " | " + vo.getDeploymentInfo() + " | " + vo.getVOStatus() + " | " + vo.isMobileVO() + " \n\t"  
	    				);
	    				//- Get All Functions
	    				if(vo.getVOFunctions()!=null){
		    				for(VOFunction vo_function : vo.getVOFunctions()){
		    					System.out.println("VO Function{");
		    					System.out.print("\t"+
		    							vo_function.getResourceURI() + " | " + vo_function.getName() + " | " + vo_function.getDescription() + " | "
		    					);
		    					//- Get VO Function Input names
		    					if(vo_function.getInputNames()!=null){
			    					System.out.print("[");
			    					for(String input_name : vo_function.getInputNames()){
			    						System.out.print(" "+input_name+" ");
			    					}
			    					System.out.print("]"+ " | ");
		    					}
		    					
		    					//- Get VO Function Output names
		    					if(vo_function.getOutputNames()!=null){
			    					System.out.print("[");
			    					for(String output_name : vo_function.getOutputNames()){
			    						System.out.print(" "+output_name+" ");
			    					}
			    					System.out.print("]"+ "\n\t\t\t");
		    					}
		    					
		    					//- Get VO Function Input Parameters
		    					if(vo_function.getInputParameters()!=null){
			    					for(InputParameter in : vo_function.getInputParameters()){
			    						System.out.print("Input Parameter{ \n\t\t\t\t");
			    						System.out.print(
			    								in.getResourceURI() +" | "+ in.getName()
			    						);
			    						System.out.print("\n\t\t\t\t\t");
			    						//- Get Input Params Meta Features
			    						if(in.getMetaFeatureSet()!=null){
				    						for(MetaFeatureSet mfs : in.getMetaFeatureSet()){		    																	
				    							System.out.print("Input Parameter MetaFeature{ ");
				    							System.out.print(
														mfs.getType() +" , "+ mfs.getValue()												
												);
				    							System.out.print(" }");
				    							System.out.print("\n\t\t\t\t");
											}//end for
			    						}
			    						System.out.print("\n\t\t\t"+"}");
			    					}//end for Input params
		    					}
		    					//- Get VO Function Output Parameters
		    					if(vo_function.getOutputParameters()!=null){
			    					for(OutputParameter out : vo_function.getOutputParameters()){
			    						System.out.print("\n\t\t\tOutput Parameter{ \n\t\t\t\t");
			    						System.out.print(
			    								out.getResourceURI() +" | "+ out.getName()
			    						);		
			    						System.out.print("\n\t\t\t\t\t");
			    						//- Get Output Params Meta Features
			    						if(out.getMetaFeatureSet()!=null){
				    						for(MetaFeatureSet mfs : out.getMetaFeatureSet()){		    																	
				    							System.out.print("Output Parameter MetaFeature{ ");
				    							System.out.print(
														mfs.getType() +" , "+ mfs.getValue()												
												);
				    							System.out.print(" }");
				    							System.out.print("\n\t\t\t\t\t");
											}//end for
			    						}
			    						System.out.print("\n\t\t\t"+"}");
			    					}//end for output params
		    					}//end if check null
		    					
		    					//- Get VO Function Feature
		    					if(vo_function.getVOFunctionFeatures()!=null){
			    					for(VOFunctionFeature voff : vo_function.getVOFunctionFeatures()){
										System.out.print("\n\t\t\t");
			    						System.out.print("VO Function Fature { \n\t\t\t\t");
			    						System.out.print(
			    								voff.getResourceURI() + " | "+ voff.getType() + " | "+ voff.getName() + " | "+ voff.getValue()
			    							);
			    						System.out.println("\n\t\t\t}");
			    					}//end for VO Function features
		    					}
		    					
		    					//- Get VO Function Access Rights
		    					if(vo_function.getAccessRights()!=null){
			    					for(AccessRight vo_ar : vo_function.getAccessRights()){
			    						System.out.print("\n\t\t\t");
			    						System.out.print("VO Function Access Right { \n\t\t\t\t");
			    						System.out.print(
			    								vo_ar.getResourceURI() + " | "+ vo_ar.getType() + " | "+ vo_ar.getValue()
			    								);
			    						System.out.println("\n\t\t\t}");
			    					}//end for VO Access Rights
		    					}
		    					//- Get VO Billing Costs
		    					if(vo_function.getBillingCosts()!=null){
			    					for(BillingCost vo_bc : vo_function.getBillingCosts()){
			    						System.out.print("\n\t\t\t");
			    						System.out.print("VO Function Billing Cost { \n\t\t\t\t");
			    						System.out.print(
			    								vo_bc.getResourceURI() + " | "+ vo_bc.getType() + " | "+ vo_bc.getValue()
			    								);
			    						System.out.println("\n\t\t\t}");
			    					}//end for VO Function Billing Costs
		    					}
		    					
		    				}//end for VO Functions
	    				}//end if check null VO Functions
	    			
	    				//- Get All VO Parameters
	    				if(vo.getVOParameters()!=null){
		    				for(VOParameter vo_par : vo.getVOParameters()){
		    					System.out.print("\tVO Parameter{\n\t\t");
		    					System.out.print(
		    							vo_par.getResourceURI() + " | " + vo_par.getName() + 
		    							"\n\t\t\t"
			    						);		
	    						//- Get VO Param Meta Features
		    					if(vo_par.getMetaFeatureSet()!=null){
		    						for(MetaFeatureSet mfs : vo_par.getMetaFeatureSet()){		    																	
										System.out.print("VO Parameter Meta{ " + mfs.getType() +" , "+ mfs.getValue());
		    							System.out.println("\t\t\t");
									}//end while
		    					}
		    					System.out.println("\t}");
		    				}//end for VO Parameters
	    				}
	    				
	    				//- Get VO Owner
	    				if(vo.getVOOwner()!=null){
		    				System.out.println("\tVO Owner{");
		    				System.out.println("\t\t"+
		    						vo.getVOOwner().getResourceURI() + "\n\t"
				    		);
		    				System.out.println("\t}");
	    				}
	    				
	    				//- Get ICT Object
	    				if(vo.getICTObject()!=null){
		    				System.out.println("\tICT Object{");
		    				System.out.println("\t\t"+
		    						vo.getICTObject().getResourceURI() + "\n\t"
				    		);
	    				
		    				//- Get All ICT Parameters
		    				if(vo.getICTObject().getICTParameters()!=null){
			    				for(ICTParameter ict_par : vo.getICTObject().getICTParameters()){
			    					System.out.print("\t\tICT Parameter{\n\t\t");
			    					System.out.print(
			    							ict_par.getResourceURI() + " | " + ict_par.getName() + 
			    							"\n\t\t\t"
				    						);		
		    						//- Get ICT Param Meta Features
			    					if(ict_par.getMetaFeatureSet()!=null){
			    						for(MetaFeatureSet mfs : ict_par.getMetaFeatureSet()){		    																	
											System.out.print("ICT Parameter Meta{ " + mfs.getType() +" , "+ mfs.getValue() +" }");
			    							System.out.println("\t\t\t");
										}//end for
			    						System.out.println("\t}");
			    					}
			    				}//end for ICT Parameters
		    				}//end if check null
		    				
		    				//- Get ICT GeoLocation
		    				if(vo.getICTObject().getGeoLocation()!=null){
			    				System.out.print("\tICT GeoLocation{ "+
			    					vo.getICTObject().getGeoLocation().getLongitude() + " | " +
				    					vo.getICTObject().getGeoLocation().getLatitude() + " | " +
				    						vo.getICTObject().getGeoLocation().getAltitude()
				    			+" }");
		    				}
		    				//- Get Non-ICT Object
		    				if(vo.getICTObject().getNonICTObject()!=null){
			    				System.out.println("\n\t\tNon-ICT Object{");
			    				System.out.println("\t\t\t"+
			    						vo.getICTObject().getNonICTObject().getResourceURI() + " | " + vo.getICTObject().getNonICTObject().getName() +" | "+ vo.getICTObject().getNonICTObject().getType()
			    				);
			    				//- Get Non-ICT GeoLocation
			    				if(vo.getICTObject().getNonICTObject().getGeoLocation()!=null){
				    				System.out.print("\t\t\t\tNon-ICT GeoLocation{ "+
				    						vo.getICTObject().getNonICTObject().getGeoLocation().getLongitude() + " | " + 
				    							vo.getICTObject().getNonICTObject().getGeoLocation().getLatitude() + " | " + 
				    								vo.getICTObject().getNonICTObject().getGeoLocation().getAltitude()
				    						
			    					+"}");
				    				System.out.println("\t\t}");
			    				}//end if check null
		    				}
		    				System.out.println("\n\t}\n");
	    				}//end if check null ICT
	    				System.out.println("}");
	    				System.out.println("-----------------------------------------------\n\n");
	    				i++;
	    			 }//end for
	    		}//end if check VOs' container
	    	}//end if
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