package iCore;

import iCore.Monitor.Monitor;

/*
 * Copyright (c) 2013 iCore
 * <insert License here>
 */
public class Main {

	 public static void main ( String[] args ) {
		 
		 //TODO: add command line analyzer
		 if(args.length >= 1)
			 Config.readConfig(args[0]);
		 
		 init();
		 
	}
	 
	 public static void init() {
		 
		//Start iCore monitor
		 Monitor monitor = new Monitor();
		 monitor.start();
	 }
		
}
