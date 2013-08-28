package iCore.Service.Language;

import java.util.Collection;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

/*
 * A service request
 */
public class AverageSE extends ServiceExpression<Integer> {
	public static final String NAME = "AverageSE";
	
	ServiceExpression<Collection<Integer>> values = null;
	
	public AverageSE(ServiceExpression<Collection<Integer>> myValues) {
		values = myValues;
	}

	public ServiceExpression<Collection<Integer>> getValues() {
		return values;
	}

}
