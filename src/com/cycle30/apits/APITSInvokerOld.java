package com.cycle30.apits;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

import terrapin.tuxedo.FmlError;
import terrapin.tuxedo.TuxApplicationError;
import terrapin.tuxedo.TuxError;
import com.csgsystems.aruba.connection.BSDMSessionContext;
import com.csgsystems.aruba.connection.BSDMSettings;
import com.csgsystems.aruba.connection.ConnectionFactory;
import com.csgsystems.aruba.connection.Connection; 
import com.csgsystems.aruba.connection.ServiceException;
import com.csgsystems.aruba.connection.XmlConnection;
import com.csgsystems.bali.connection.ApiMappings;
import com.csgsystems.fx.security.SecurityManagerFactory;
import com.csgsystems.fx.security.util.AuthenticationException;
import com.csgsystems.fx.security.util.FxException;

public class APITSInvokerOld {
	//public String invoke(String input) {
	public String invoke(org.w3c.dom.Document input) {
		System.out.println("input type is " + input.getClass().getName());
		ConnectionFactory factory = null; 
		XmlConnection connection = null;
		int targetAcctInternalId = 88;
		com.csgsystems.fx.security.SecurityManager sm = null;
		BSDMSessionContext context = null;
		factory = ConnectionFactory.instance();
		BSDMSettings settings = BSDMSettings.getDefault();
		String outputString = "user is " + settings.getUser();
		context = BSDMSessionContext.getDefaultContext();
		
		try {
			sm = SecurityManagerFactory.createSecurityManager("KenanFx", "arborsv", "123456", "kenanfx");
		} catch (FxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outputString="FxException";
		}
		try {
			sm.authenticate();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outputString="AuthenticationException";
		} 
		
		context.setSecurityContext(sm);
	
		try {
			//connection = factory.createConnection(settings);
			connection = factory.createXmlConnection(settings, context);
		} catch (TuxError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outputString="TuxError";
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outputString="ServiceException";
		}
		
		
		// --
		// Create a HashMap to contain the AccountLocate object's info 
		HashMap acctLocate = new HashMap();
		// Create another HashMap to contain the Account object's key 
		HashMap acctKey = new HashMap();
		// Put the AccountInternalId of the target Account 
		// in the acctKey HashMap 
		acctKey.put("AccountInternalId", new Integer(targetAcctInternalId));
		// Put the acctKey HashMap in the acctLocate HashMap 
		acctLocate.put("Key", acctKey);
		// Put the acctLocate HashMap in a request HashMap 
		Map request = new HashMap(); 
		request.put("AccountLocate", acctLocate);

		// Get the call name using the ApiMappings class 
		String callName = null; 
		callName = ApiMappings.getCallName("AccountLocateGet");

		// Make the call and store 
		// the server's response in callResponse 
		Document callResponse = null; 
		try {
			// TODO reverify these catches for xml...started out has hashmap
			callResponse = connection.call(input);
			//callResponse = connection.call(context, callName, request);
		} catch (TuxApplicationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TuxError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FmlError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//acctLocate = (HashMap) callResponse.get("AccountLocate"); 
		//Integer serverId = (Integer) acctLocate.get("ServerId"); 
		//System.out.println("ServerId: " + serverId + "\n");
		
		try { 
			sm.logout();
		} catch (FxException e) { 
			e.printStackTrace();
		} finally {
			if (connection != null) { 
//				try {
//					if (connection.getTraceResults() != null) { 
//						System.out.print("TraceResults = "); 
//						System.out.println((Object) connection.getTraceResults()); 
//						connection.close();
//					}
//				} // Handle input/output exceptions 
//				catch (IOException ioe) {
//					ioe.printStackTrace();
//				}
			} 
		}
		//printHashMap(callResponse);
		// --
		
		return outputString;
	}
	
	public static void main(String[] args){
		APITSInvokerOld me = new APITSInvokerOld();
		System.out.println(me.invoke(null));
	}
	
	public static void printHashMap(Map map) { 
		try {
			// Create a BufferedWriter 
			BufferedWriter bw = new BufferedWriter(new PrintWriter(System.out));
			// Pass the map to the ServiceException object's print() method 
			ServiceException.printMap(bw, map, 2);
			// Flush the BufferedWriter 
			bw.flush();
		}
		// Handle input/output exceptions 
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
}
