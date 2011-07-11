package com.cycle30.apits;

import java.io.IOException;
import org.w3c.dom.Document;

import terrapin.tuxedo.FmlError;
import terrapin.tuxedo.TuxError;

import com.csgsystems.aruba.connection.BSDMSessionContext;
import com.csgsystems.aruba.connection.BSDMSettings;
import com.csgsystems.aruba.connection.ConnectionFactory;
import com.csgsystems.aruba.connection.ServiceException;
import com.csgsystems.aruba.connection.XmlConnection;
import com.csgsystems.fx.security.util.FxException;

public class APITSInvoker {
	
	private XmlConnection xmlConnection;

	private void audit() { //TODO add auditing of request/response/metrics
		
	}
	
	public APITSInvoker(){
		try {
			xmlConnection = ConnectionFactory.instance().createXmlConnection(
					BSDMSettings.getDefault(), BSDMSessionContext.getDefaultContext());
		} catch (TuxError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			//throw new java.lang.InstantiationError(e);
		}
	}
	
	public Document invoke(Document requestDOM) throws InvocationException {
		Document responseDOM = null; // the DOM returned by Kenan's API TS
		try {
			responseDOM = xmlConnection.call(requestDOM); // Call API TS
		} catch (FxException e) {
			throw new InvocationException(e);
		} catch (TuxError e) {
			throw new InvocationException(e);
		} catch (ServiceException e) {
			throw new InvocationException(e);
		} catch (FmlError e) {
			throw new InvocationException(e);
		} catch (IOException e) {
			throw new InvocationException(e);
		} finally {
			if (xmlConnection != null)
				xmlConnection.close();
		}
		return responseDOM;
	}

	/**
	 * Calls the Kenan API TS via the DOM interface
	 * 
	 * @param requestDOM
	 *            XML DOM that should contain a valid API TS Request
	 * @return XML DOM containing Kenan's response
	 */
	/*public Document callWithCommit(Document requestDOM) {
		Document responseDOM = null; // the DOM returned by Kenan's API TS
		XmlConnection connection = null; // Middleware connection to Kenan
		try {
			// create a Tuxedo connection using the settings loaded from
			// configuration files.
			connection = ConnectionFactory.instance().createXmlConnection(
					BSDMSettings.getDefault(), getContext());
			if (commitOrders)
				connection.beginTransaction(30, 0); // TODO what is the 30, 0
													// for? c'n'p from guide
			responseDOM = connection.call(requestDOM, true); // Call API TS
			if (commitOrders) {
				if (validResponse(responseDOM)) {
					connection.call(createOrderCommitDOM(responseDOM));
					connection.endTransaction(0); // TODO what value goes here?
				} else
					connection.abortTransaction(0); // TODO what value goes
													// here?

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
	}  */
}
