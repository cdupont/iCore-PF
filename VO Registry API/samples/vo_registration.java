package samples;

import gr.tns.JSONUtil;
import gr.tns.RestUtil;
import icore.voregistry.api.AccessRight;
import icore.voregistry.api.BillingCost;
import icore.voregistry.api.GeoLocation;
import icore.voregistry.api.ICTObject;
import icore.voregistry.api.ICTParameter;
import icore.voregistry.api.InputParameter;
import icore.voregistry.api.MetaFeatureSet;
import icore.voregistry.api.NonICTObject;
import icore.voregistry.api.OutputParameter;
import icore.voregistry.api.VOFunction;
import icore.voregistry.api.VOFunctionFeature;
import icore.voregistry.api.VOOwner;
import icore.voregistry.api.VOParameter;
import icore.voregistry.api.VORegistrationRequest;
import icore.voregistry.api.VORegistryInterface;
import icore.voregistry.api.VORegistryResponse;
import icore.voregistry.api.VirtualObject;

import java.awt.EventQueue;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;


public class vo_registration {

	/** PARAMETER FOR THE CREATION OF THE VO DESCRIPTION BASED ON THE VO INFORMATION MODEL **/
	private VirtualObject vo;
    //- VO Parameters
	private VOParameter vo_param;	
		private HashSet<VOParameter> vo_params = new HashSet<VOParameter>();
		private HashSet<AccessRight> vo_param_acc_rights = new HashSet<AccessRight>();
		private HashSet<BillingCost> vo_param_bill_costs = new HashSet<BillingCost>();
	/** VO Functions **/
	private static VOFunction vo_funct;
		private HashSet<VOFunction> vo_functs= new HashSet<VOFunction>();
    /** VO Function Input parameters **/
	private static InputParameter in_param;
		private HashSet<InputParameter> in_params = new HashSet<InputParameter>();
    /** VO Function Output parameters **/
	private OutputParameter out_param;
		private HashSet<OutputParameter> out_params = new HashSet<OutputParameter>();
    /** VO Function Features **/
	private VOFunctionFeature vo_fun_feat;
		private HashSet<VOFunctionFeature> vo_fun_feats = new HashSet<VOFunctionFeature>();
    /** VO Function Inputs/Outputs description **/
		private HashSet<String>input_names;
		private HashSet<String>output_names;
    /** Access Rights **/
	private AccessRight acc_right;
		private HashSet<AccessRight> vo_function_acc_rights = new HashSet<AccessRight>();
    /** Billing Costs **/
	private BillingCost bill_cost;
		private HashSet<BillingCost> vo_function_bill_costs = new HashSet<BillingCost>();
	/** ICT **/
	private static ICTObject ict_obj;
		/** ICT Parameters **/
		private ICTParameter ict_param;
			private HashSet<ICTParameter> ict_params = new HashSet<ICTParameter>();
				/** Meta Features **/
				private MetaFeatureSet meta_feature_set;
					private HashSet<MetaFeatureSet> meta_feature_sets;
		/** ICT GeoLocation **/
		private GeoLocation non_ict_geo_loc;
	/** NON-ICT **/
	private NonICTObject non_ict_obj;
    /** NON-ICT GeoLocation **/
	private GeoLocation ict_geo_loc;
	/** VO owner **/	
	private VOOwner vo_owner_user;
	/** Assisted Parameters**/
	private HashSet<String> UserRoles = new HashSet<String>();
	private String VO_BASE_URI, VO_REGISTRY_URI;
	private VORegistrationRequest registration_request;	
	private String vo_representation;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new vo_registration();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public vo_registration(){
		registrationExample();
	}
	
	
	/** DUMMY METHOD FOR THE EXECUTION OF THE REQUEST - YOU CAN USE YOUR OWN CUSTOM METHOD/CLASS **/
	public void registrationExample(){
	   
		/** SET VO REGISTRY URI **/
		setVORegistryUri("http://83.212.238.250:8092/vo_registry/");
		
	   /*********************************************************************** 
	    * -1- CREATE THE  REPRESENTATION  OF THE VO  - (based on the VO Model)
	    ************************************************************************/		
	    /** SET VO BASE URI -(Currently defined manually by the user - next version supported by a centralized mechanism)- **/
		setVOBaseUri("http://purl.oclc.org/UPRC/icore/VO/Humidity_Sensor_DHT11");
		try {
			vo = new VirtualObject(new URI(getVOBaseUri()));
			vo.setType(new URI("http://purl.oclc.org/NET/ssnx/ssn#Sensor"));
			vo.setDeploymentInfo(new URI(VO_BASE_URI+"/deploy.wsdl"));
			vo.setVOStatus("AVAILABLE");
			vo.setVOMobility(false);
				//- ICT
				ict_obj = new ICTObject();
				//- ICT Parameter 1
				ict_param = new ICTParameter();
				ict_param.setName("Sensor Measurement Range");
					meta_feature_sets = new HashSet<MetaFeatureSet>();
						meta_feature_set = new MetaFeatureSet();
							meta_feature_set.setType(new URI("http://purl.oclc.org/NET/ocg/minValue"));
							meta_feature_set.setValue("35%RH");
					meta_feature_sets.add(meta_feature_set);
						meta_feature_set = new MetaFeatureSet();
							meta_feature_set.setType(new URI("http://dbpedia.org/ontology/maxValue"));
							meta_feature_set.setValue("110%RH");
				meta_feature_sets.add(meta_feature_set);
				//- Add all MetaFeature Set on the ICT Parameter		
				ict_param.setMetaFeatureSet(meta_feature_sets);				
				ict_params.add(ict_param);
				
				/** Add ICT Parameters to ICT Object **/
				ict_obj.setICTParameters(ict_params);
				//- ICT Location
				ict_geo_loc = new GeoLocation();
				ict_geo_loc.setAltitude(0.0);
				ict_geo_loc.setLatitude(300.0);
				ict_geo_loc.setLongitude(300.0);
			/** Add Location to ICT Object **/
			ict_obj.setGeoLocation(ict_geo_loc);
				//- NON_ICT
				non_ict_obj = new NonICTObject();
				non_ict_obj.setName("Sarahs Home");
				non_ict_obj.setType(new URI("http://www.loa.istc.cnr.it/ontologies/OWN/OWN.owl#Room"));
				//- NON-ICT Location (in this case is the same as ICT Location)
				non_ict_geo_loc = new GeoLocation();
					non_ict_geo_loc.setAltitude(0.0);
					non_ict_geo_loc.setLatitude(37.941881);
					non_ict_geo_loc.setLongitude(23.65006);
					//-- Alternatively we could define: "non_ict_geo_loc = ict_geo_loc", since they have the same location
			/** Add Location to non-ICT Object **/
			non_ict_obj.setGeoLocation(non_ict_geo_loc);
			/** Add non-ICT Object to ICT Object **/
			ict_obj.setNonICTObject(non_ict_obj);
				
				//- VO Parameters: (1..*)
				//-1st: VO Parameter
				vo_param = new VOParameter();
				vo_param.setName("Communication Protocol 1");
				//- Add MetaFeature sets on the VO Parameter
					meta_feature_sets = new HashSet<MetaFeatureSet>();
						meta_feature_set = new MetaFeatureSet();
							meta_feature_set.setType(new URI("http://www.iot-icore.eu/ontologies/CommunicationInterfaces/Protocols.owl#Protocol_Type"));
							meta_feature_set.setValue("REST");
					meta_feature_sets.add(meta_feature_set);
						meta_feature_set = new MetaFeatureSet();
							meta_feature_set.setType(new URI("http://www.iot-icore.eu/ontologies/CommunicationInterfaces/Protocols.owl#Protocol_Base"));
							meta_feature_set.setValue("HTTP");
							
					//- Add all MetaFeature Set on the VO Parameter		
					vo_param.setMetaFeatureSet(meta_feature_sets);
				
				//- VO Function Access Rights -  1 Type many Values
				//- 1st
				acc_right = new AccessRight();
					acc_right.setName("READ");
					acc_right.setType(new URI("http://www.iot-icore.eu/ont/AccessRights/READ"));
						// Add more than one values
						UserRoles = new HashSet<String>();
						UserRoles.add("VO_OWNER");
						UserRoles.add("SIMPLE_USER");
					acc_right.setValue(UserRoles);
				vo_param_acc_rights.add(acc_right);
				//- 2nd
				acc_right = new AccessRight();
					acc_right.setName("WRITE");
					acc_right.setType(new URI("http://www.iot-icore.eu/ont/AccessRights/WRITE"));
					acc_right.setValue("VO_OWNER");
				vo_param_acc_rights.add(acc_right);
				//- 3rd
				acc_right = new AccessRight();
					acc_right.setName("DISCOVER");
					acc_right.setType(new URI("http://www.iot-icore.eu/ont/AccessRights/DISCOVERY"));
						// Add more than one values
						UserRoles = new HashSet<String>();
						UserRoles.add("VO_OWNER");
						UserRoles.add("SIMPLE_USER");
				acc_right.setValue(UserRoles);
				vo_param_acc_rights.add(acc_right);
				/** Add Access Rights on the VO Parameter **/
				vo_param.setAccessRights(vo_param_acc_rights);

				//- VO Function Billing Costs - 1 Type & 1 Value
				//- 1st
				bill_cost = new BillingCost();
					bill_cost.setName("Price per Read");
					bill_cost.setType(new URI("http://www.iot-icore.eu/ont/Billing#Price_per_Read"));
					bill_cost.setValue("0.00");
				vo_param_bill_costs.add(bill_cost);
				/** Add Billing Costs on the VO Parameter **/
				vo_param.setBillingCosts(vo_param_bill_costs);					
				//- Add Param to VO params container
				vo_params.add(vo_param);
								
				/** Add VO Parameter on the VO **/
				vo.setVOParameters(vo_params);
	
				//- VO Functions: (1..*)			
				//- Function 1: Temperature_Monitoring
					//- Main Description
					vo_funct = new VOFunction();
					vo_funct.setName("Emulated Humidity Monitoring");
					vo_funct.setDescription("This function monitors the environmental humidity.");
					//- Inputs Names 
					input_names = new HashSet<String>();
						input_names.add("Environmental Humidity");
						/** Add Input Names on the VO Function **/
						vo_funct.setInputNames(input_names);				
					//- Outputs Names
					output_names = new HashSet<String>();
						output_names.add("Emulated Humidity");				
					/** Add Output Names on the VO Function **/
					vo_funct.setOutputNames(output_names);			
					
					//- VO Function Input Parameters
						//- IN 1
						in_param = new InputParameter();
						in_param.setName("Environmental Humidity");
						//- Meta Features
						meta_feature_sets = new HashSet<MetaFeatureSet>();
							meta_feature_set = new MetaFeatureSet();
							meta_feature_set.setType(new URI("http://opengeospatial.org/envir_conditions.owl#"));
							meta_feature_set.setValue("Environmental_Humidity");
						meta_feature_sets.add(meta_feature_set);
						//- Add all MetaFeature Set on the Input Parameter		
							in_param.setMetaFeatureSet(meta_feature_sets);
						in_params.add(in_param);
					
					/** Add Input Parameters on the VO Function **/
					vo_funct.setInputParameters(in_params); 
					
					//- VO Function Output Parameters
						out_param = new OutputParameter();
						out_param.setName("Emulated Humidity");
						//- Meta Features
						meta_feature_sets = new HashSet<MetaFeatureSet>();
							meta_feature_set = new MetaFeatureSet();
							meta_feature_set.setType(new URI("http://purl.obolibrary.org/obo/uo.owl#"));
							meta_feature_set.setValue("%RH");
						meta_feature_sets.add(meta_feature_set);
							meta_feature_set = new MetaFeatureSet();
							meta_feature_set.setType(new URI("http://www.iot-icore.eu/ontologies/CommunicationInterfaces/Protocols.owl#Protocol_Type"));
							meta_feature_set.setValue("REST");
						meta_feature_sets.add(meta_feature_set);
					
						//- Add all MetaFeature Set on the Input Parameter		
						out_param.setMetaFeatureSet(meta_feature_sets);
					out_params.add(out_param);						
					/** Add Output Parameters on the VO Function **/
					vo_funct.setOutputParameters(out_params);
					
					//- VO Function Feature
					//- 1st
						vo_fun_feat = new VOFunctionFeature();
						vo_fun_feat.setType(new URI("http://www.iot-icore.eu/ontologies/features.owl#Cost"));
						vo_fun_feat.setName("Energy");
						vo_fun_feat.setValue(3.0);
					vo_fun_feats.add(vo_fun_feat);	
						vo_fun_feat = new VOFunctionFeature();
						vo_fun_feat.setType(new URI("http://www.iot-icore.eu/ontologies/features.owl#Cost"));
						vo_fun_feat.setName("Network");
						vo_fun_feat.setValue(3.0);
					vo_fun_feats.add(vo_fun_feat);	
						vo_fun_feat = new VOFunctionFeature();
						vo_fun_feat.setType(new URI("http://www.iot-icore.eu/ontologies/features.owl#Cost"));
						vo_fun_feat.setName("Expenditure");
						vo_fun_feat.setValue(3.0);
					vo_fun_feats.add(vo_fun_feat);	
					//- 2nd
						vo_fun_feat = new VOFunctionFeature();
						vo_fun_feat.setType(new URI("http://www.iot-icore.eu/ontologies/features.owl#Utility"));
						vo_fun_feat.setName("Quality");
						vo_fun_feat.setValue(1.0);
					vo_fun_feats.add(vo_fun_feat);	
						vo_fun_feat = new VOFunctionFeature();
						vo_fun_feat.setType(new URI("http://www.iot-icore.eu/ontologies/features.owl#Utility"));
						vo_fun_feat.setName("Performance");
						vo_fun_feat.setValue(1.0);
					vo_fun_feats.add(vo_fun_feat);	
						vo_fun_feat = new VOFunctionFeature();
						vo_fun_feat.setType(new URI("http://www.iot-icore.eu/ontologies/features.owl#Utility"));
						vo_fun_feat.setName("Security");
						vo_fun_feat.setValue(1.0);
					vo_fun_feats.add(vo_fun_feat);	
					/** Add VO Function Features on the VO Function **/
					vo_funct.setVOFunctionFeatures(vo_fun_feats);				

					//- VO Function Access Rights -  1 Type many Values
					//- 1st
					acc_right = new AccessRight();
						acc_right.setName("READ");
						acc_right.setType(new URI("http://www.iot-icore.eu/ont/AccessRights/READ"));
							// Add more than one values
							UserRoles = new HashSet<String>();
							UserRoles.add("VO_OWNER");
							UserRoles.add("SIMPLE_USER");
						acc_right.setValue(UserRoles);
						vo_function_acc_rights.add(acc_right);
					//- 2nd
					acc_right = new AccessRight();
						acc_right.setName("WRITE");
						acc_right.setType(new URI("http://www.iot-icore.eu/ont/AccessRights/WRITE"));
						acc_right.setValue("VO_OWNER");
					vo_function_acc_rights.add(acc_right);
					//- 3rd
					acc_right = new AccessRight();
						acc_right.setName("DISCOVER");
						acc_right.setType(new URI("http://www.iot-icore.eu/ont/AccessRights/DISCOVERY"));
							// Add more than one values
							UserRoles = new HashSet<String>();
							UserRoles.add("VO_OWNER");
							UserRoles.add("SIMPLE_USER");
					acc_right.setValue(UserRoles);
					vo_function_acc_rights.add(acc_right);
					/** Add Access Rights on the VO Function **/
					vo_funct.setAccessRights(vo_function_acc_rights);

					//- VO Function Billing Costs - 1 Type & 1 Value
					//- 1st
					bill_cost = new BillingCost();
						bill_cost.setName("Price per Function Call");
						bill_cost.setType(new URI("http://www.iot-icore.eu/ont/Billing#Price_per_Function_Call"));
						bill_cost.setValue("0.01");
					vo_function_bill_costs.add(bill_cost);
					//- 2nd
					bill_cost = new BillingCost();
						bill_cost.setName("Price per Minute");
						bill_cost.setType(new URI("http://www.iot-icore.eu/ont/Billing#Price_per_Minute"));
						bill_cost.setValue("0.00");
					vo_function_bill_costs.add(bill_cost);
					/** Add Billing Costs on the VO Function **/
					vo_funct.setBillingCosts(vo_function_bill_costs);			
				
					//- Insert function to VO's Function list
					vo_functs.add(vo_funct);
					
					/** Add VO Functions on the VO **/
					vo.setVOFunctions(vo_functs);
					
					//- VO Owner
					vo_owner_user = new VOOwner(new URI("http://www.iot-icore.eu/Users/UPRC_MEMBER"));
					/** Add VO Owner on the VO **/
					vo.setVOOwner(vo_owner_user);
					
					/** Add ICT Object on the VO **/
					vo.setICTObject(ict_obj);
		} catch (URISyntaxException e) {e.printStackTrace();}
    	
		
		/*************************************************************************** 
	     *-(Step 2)- CREATE THE REGISTRATION REQUEST & WRAP-UP IT INTO XML or JSON
	     ***************************************************************************/
		 //-2: Create the Request
    		//-2.1: Wrap-up the VO Java Object into XML
			//vo_representation = XMLUtil.toXML(vo);
				 /*(Request in JSON Format):*/ vo_representation =  JSONUtil.toJson(vo);
			
			//-2.2: Create the Registration request using the corresponding VO representation format
	    	registration_request = 
	    			new VORegistrationRequest("1", vo_representation, "text/xml");
	    			//- /*(Request in JSON Format):*/  new VORegistrationRequest("1", vo_representation, "application/json");
	    	
	   	/************************************************************************** 
		 *-(Step 3)- CONNECT TO THE VO REGISTRY AND SEND THE REGISTRATION REQUEST 
		 **************************************************************************/
		 //- 3.1: Connect
	    	VORegistryInterface vo_registry_if = 
					RestUtil.getHTTPProxy(URI.create(getVORegistryUri()), VORegistryInterface.class);
    	 //- 3.2: Send	    	
	    	VORegistryResponse response =  vo_registry_if.registration(registration_request);
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