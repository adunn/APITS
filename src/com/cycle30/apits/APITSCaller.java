package com.cycle30.apits;

import java.io.IOException;
import terrapin.tuxedo.FmlError;
import terrapin.tuxedo.TuxError;
import com.csgsystems.aruba.connection.BSDMSessionContext;
import com.csgsystems.aruba.connection.BSDMSettings;
import com.csgsystems.aruba.connection.ConnectionFactory;
import com.csgsystems.aruba.connection.ServiceException;
import com.csgsystems.aruba.connection.XmlConnection;
import com.csgsystems.fx.security.SecurityManagerFactory;
import com.csgsystems.fx.security.SecurityManager;
import com.csgsystems.fx.security.util.AuthenticationException;
import com.csgsystems.fx.security.util.FxException;
import org.w3c.dom.Document;

public class APITSCaller {

	private static com.csgsystems.aruba.connection.BSDMSessionContext context = null;
	
	/** Calls the Kenan API TS via the DOM interface
	 * 
	 * @param requestDOM XML DOM that should contain a valid API TS Request
	 * @return XML DOM containing Kenan's response
	 */
	public Document call(Document requestDOM) {
		System.out.println("Pool size = " + XmlConnection.pool.getSize());
		Document responseDOM = null; // the DOM returned by Kenan's API TS
		XmlConnection connection = null;
		//TODO investigate XmlConnection.pool
		try {
			// create a Tuxedo connection using the settings loaded from configuration files.  
			connection = ConnectionFactory.instance().createXmlConnection(BSDMSettings.getDefault(), getContext());
			responseDOM = connection.call(requestDOM); // Call API TS
		} catch (FxException e) {
			// call
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TuxError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FmlError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) 
				connection.close();
		}
		return responseDOM;
	}
	
	private BSDMSessionContext getContext() {
		// once the context is initially populated it can be reused for every call
		if (null != context) 
			return context;
		
		SecurityManager securityManager = null;
		try {
			securityManager = SecurityManagerFactory.createSecurityManager("KenanFx", "arborsv", "123456", "kenanfx");
		} catch (FxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO must bail at this point; no point calling authenticate() if create failed
		}
		try {
			securityManager.authenticate();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//synchronized (context) { // make sure no other instance grabs a reference to context while we're configuring it
			context = com.csgsystems.aruba.connection.BSDMSessionContext.getDefaultContext();
			context.setSecurityContext(securityManager);
		//}
		return context;
	}

}
