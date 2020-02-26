/**
 * 
 */
package com.churchclerk.addressapi;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.ws.rs.ApplicationPath;

/**
 * 
 * @author dongp
 *
 */
@SpringBootApplication
@ApplicationPath("/churchclerk/address")
public class AddressApiApplication {

	private static Logger logger = LoggerFactory.getLogger(AddressApiApplication.class);

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(AddressApiApplication.class, args);
	}

}
