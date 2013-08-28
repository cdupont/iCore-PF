package iCore.Service.Language;

import java.util.Collection;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */

/*
 * A service request
 */
public class AggregateSE<T> extends ServiceExpression<T> {
	public static final String NAME = "AggregateSE";
	ServiceExpression<T> a = null; 
	ServiceExpression<T> b = null;
	
	AggregateSE(ServiceExpression<T> a, ServiceExpression<T> b) {
		this.a = a;
		this.b = b;
	}	
	

}
