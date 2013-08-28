package iCore.Service.Language;

import java.util.Collection;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

/*
 * A service request
 */
public class ServiceLanguage {

	
	public static ServiceExpression<Integer> average(ServiceExpression<Collection<Integer>> values) {
		return new AverageSE(values);
	}
	
	public static ServiceExpression<Integer> sensor(String location) {
		return new SensorSE<Integer>(location);
	}
	
	public static ServiceExpression<Collection<Integer>> temperatureSensors(String location) {
		return new TemperatureSensorSE<Collection<Integer>>(location);
	}
	
//	public static ServiceExpression<Collection<Integer>> aggregate(ServiceExpression<Integer> a, ServiceExpression<Integer> b) {
//		return new AggregateSE<Collection<Integer>>(a, b);
//	}
	

}
