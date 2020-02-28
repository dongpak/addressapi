/**
 * 
 */
package com.churchclerk.addressapi;


import com.churchclerk.addressapi.api.AddressApi;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;


@Component
@ApplicationPath("/api")
public class RestApiConfig extends ResourceConfig {
    public RestApiConfig() {
        registerEndpoints();
    }

    private void registerEndpoints() {
        register(AddressApi.class);
    }
}