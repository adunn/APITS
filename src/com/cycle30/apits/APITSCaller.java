package com.cycle30.apits;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class APITSCaller {

	private static com.csgsystems.aruba.connection.BSDMSessionContext context = null;
	//TODO externalize this configuration
	private boolean commitOrders = true;
	
	/** Calls the Kenan API TS via the DOM interface
	 * 
	 * @param requestDOM XML DOM that should contain a valid API TS Request
	 * @return XML DOM containing Kenan's response
	 */
	public Document call(Document requestDOM) {
		Document responseDOM = null; // the DOM returned by Kenan's API TS
		XmlConnection connection = null; // Middleware connection to Kenan
		try {
			// create a Tuxedo connection using the settings loaded from configuration files.  
			connection = ConnectionFactory.instance().createXmlConnection(BSDMSettings.getDefault(), getContext());
			if (commitOrders)
				connection.beginTransaction(30, 0); // TODO what is the 30, 0 for? c'n'p from guide
			responseDOM = connection.call(requestDOM); // Call API TS
			if (commitOrders){
				if (validResponse(responseDOM)){
					connection.call(createOrderCommitDOM(responseDOM));
					connection.endTransaction(0); // TODO what value goes here?
				} else
					connection.abortTransaction(0); // TODO what value goes here?
				
			}
		} catch (FxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (TuxError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (FmlError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (connection != null) 
				connection.close();
		}
		return responseDOM;
	}
	
	/** Initializes the <code>BSDMSessionContext</code> for ongoing use.  Once it has been initialized
	 * it can be reused for each subsequent connection.
	 * @return BSDMSessionContext instance including authenticated SecurityManager
	 */
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
	
	private boolean validResponse(Document responseDOM) {
		
		return true;
	}
	
	private Document createOrderCommitDOM(Document responseDOM) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    Document d = null;
	    try {
	    	DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource( new StringReader( ORDER_COMMIT_XML ) );
	    	d = builder.parse( is );
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}
	
	private static String ORDER_COMMIT_XML = "<?xml version='1.0' encoding='UTF-8'?><Request><Header><AccountServer e-dtype='int'>3</AccountServer><OperatorName e-dtype='string'>trainee</OperatorName><ApplicationName e-dtype='string'>DOMExample3.java</ApplicationName></Header><OrderCommit><Order><Key><OrderId e-dtype='numeric'>73003</OrderId></Key></Order></OrderCommit></Request>";

}
