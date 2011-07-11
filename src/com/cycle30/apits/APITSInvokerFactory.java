package com.cycle30.apits;

public class APITSInvokerFactory {
	
	private APITSInvokerFactory(){} // This is a factory; ensure an instance cannot be created
	
	public static synchronized APITSInvoker getInvoker(){
		
		return null;
	}
	
	

}
