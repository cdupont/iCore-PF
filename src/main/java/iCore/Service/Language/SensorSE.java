package iCore.Service.Language;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

/*
 * A service request
 */
public class SensorSE<T> extends ServiceExpression<T> {
	public static final String NAME = "SensorSE";
	
	String location = null;
	
	SensorSE(String myLocation) {
		location = myLocation;
	}	
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
