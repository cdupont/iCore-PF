package gsn.http.rest;

import gsn.beans.DataField;
import gsn.beans.StreamElement;
import gsn.beans.VSensorConfig;
import gsn.storage.SQLValidator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class DistributionRequestRenameCols extends DefaultDistributionRequest {

    protected DistributionRequestRenameCols(DeliverySystem deliverySystem, VSensorConfig sensorConfig, String query, long startTime) throws IOException, SQLException {
		this.deliverySystem = deliverySystem;
		vSensorConfig = sensorConfig;
		this.query = query;
		this.startTime = startTime;
	}
    

	public static DistributionRequestRenameCols create(DeliverySystem deliverySystem, VSensorConfig sensorConfig,String query, long startTime) throws IOException, SQLException {
		
		String newQuery = "SELECT TEMPERATURE AS " + sensorConfig.getName() + "_TEMPERATURE FROM " + sensorConfig.getName(); //query
		DataField[] selectedColmnNames = SQLValidator.getInstance().extractSelectColumns(query,sensorConfig);
		ArrayList<DataField> finalColmn = new ArrayList<DataField>();
		for (DataField f : selectedColmnNames) { 
			finalColmn.add(new DataField(sensorConfig.getName() + "_" + f.getName(), f.getType(), f.getDescription()) );
		}
		
		deliverySystem.writeStructure(finalColmn.toArray(new DataField[] {}));
		 
		return new DistributionRequestRenameCols(deliverySystem, sensorConfig, newQuery, startTime);
	}

}
	
