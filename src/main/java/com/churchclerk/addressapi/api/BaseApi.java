/**
 * 
 */
package com.churchclerk.addressapi.api;

import com.churchclerk.securityapi.SecurityApi;
import com.churchclerk.securityapi.SecurityToken;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Enumeration;


/**
 * 
 * @author dongp
 *
 */
public abstract class BaseApi {

	private Logger logger;

	@Context
	protected HttpServletRequest httpRequest;

	@Value("${jwt.secret}")
	private String	secret;

	protected SecurityToken	authToken	= null;

	/**
	 * 
	 * @param logger
	 */
	public BaseApi(Logger logger) {
		this.logger = logger;		
	}

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public SecurityToken verifyToken() throws Exception {

		SecurityToken	token	= new SecurityToken();
		String 			auth 	= httpRequest.getHeader("Authorization");

		if (auth == null) {
			logger.info("Authorization header required");
			throw new NotAuthorizedException("Authorization reuired");
		}

		token.setSecret(secret);
		token.setJwt(auth.substring(7));

		if (SecurityApi.process(token) == true) {
			if (token.expired()) {
				logger.info("Token expired");
				throw new NotAuthorizedException("Token expired");
			}

			if (token.getLocation().equals(getRemoteAddr()) == false) {
				logger.info("Invalid location: " + getRemoteAddr());

				Enumeration<String> en = httpRequest.getHeaderNames();
				while (en.hasMoreElements()) {
					String key	= en.nextElement();
					String val	= httpRequest.getHeader(key);

					logger.info("Header [" + key + "] -> " + val);
				}
			
				throw new NotAuthorizedException("Invalid location");
			}

			authToken = token;
			return token;
		}

		throw new NotAuthorizedException("Bad token");
	}

	/**
	 *
	 * @return
	 */
	public String getRemoteAddr() {

		String addr = httpRequest.getHeader("x-forwarded-for");
		if ((addr != null) && (addr.trim().isEmpty() == false)) {
			return addr;
		}

		return httpRequest.getRemoteAddr();
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
		else if (t instanceof NotFoundException) {
			r = Response.status(Status.NOT_FOUND).build();
		}
		else {
			r = Response.serverError().build();
			logger.error("Generating " + r.getStatus() + " " + r.getStatusInfo().getReasonPhrase() + " for "+ t, t);
		}

		return r;
	}


}
