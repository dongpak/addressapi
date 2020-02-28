/**
 * 
 */
package com.churchclerk.addressapi.api;

import org.slf4j.Logger;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


/**
 * 
 * @author dongp
 *
 */
public abstract class BaseApi {

	private Logger logger;
	
	/**
	 * 
	 * @param logger
	 */
	public BaseApi(Logger logger) {
		this.logger = logger;		
	}
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	protected Response generateErrorResponse(Throwable t) {
		Response r = null;
		
		if (t instanceof NotAuthorizedException) {
			r = Response.status(Status.UNAUTHORIZED).build();
		}
		else {
			r = Response.serverError().build();
		}
		
		logger.error("Generating " + r.getStatus() + " " + r.getStatusInfo().getReasonPhrase() + " for "+ t, t);	
		
		return r;
	}
}
